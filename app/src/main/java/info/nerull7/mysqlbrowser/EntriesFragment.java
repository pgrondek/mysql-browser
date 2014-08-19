package info.nerull7.mysqlbrowser;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.nerull7.mysqlbrowser.db.AsyncDatabaseConnector;

/**
 * Created by nerull7 on 15.07.14.
 *
 * Fragment for showing elements
 */
public class EntriesFragment extends Fragment implements AsyncDatabaseConnector.MatrixReturnListener, AsyncDatabaseConnector.ListReturnListener, AsyncDatabaseConnector.IntegerReturnListener, View.OnClickListener {
    private static final int HEADER_PADDING_TOP = 15;
    private static final int HEADER_PADDING_BOTTOM = 15;
    private static final int HEADER_PADDING_LEFT = 15;
    private static final int HEADER_PADDING_RIGHT = 15;
    private static final int ENTRIES_PADDING_TOP = 30;
    private static final int ENTRIES_PADDING_BOTTOM = 30;
    private static final int ENTRIES_PADDING_LEFT = 15;
    private static final int ENTRIES_PADDING_RIGHT = 15;

    private TableLayout entriesTable;
    private ScrollView entriesScrollView;
    private FrameLayout headerFrame;
    private HorizontalScrollView horizontalScrollView;
    private TableRow.LayoutParams layoutParams;

    private String databaseName;
    private String tableName;
    private int entriesLimit;
    private int page;
    private int pageCount;
    private int rowCount;
    private ProgressBar progressBar;
    private CustomScrollView fakeScrollView;
    private View dummyView;

    private Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entries, container, false);

        initArguments();
        initViewItems(rootView);

        Static.asyncDatabaseConnector.setIntegerReturnListener(this);
        Static.asyncDatabaseConnector.setListReturnListener(this);
        Static.asyncDatabaseConnector.setMatrixReturnListener(this);
        Static.asyncDatabaseConnector.getFields(tableName);
        Static.asyncDatabaseConnector.getEntriesCount(tableName);

        return rootView;
    }

    private void initArguments(){
        databaseName = getArguments().getString(Static.DATABASE_NAME_ARG);
        tableName = getArguments().getString(Static.TABLE_NAME_ARG);
        page = 1;

        entriesLimit = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(SettingsFragment.ENTRIES_PAGE_LIMIT, SettingsFragment.ENTRIES_PAGE_LIMIT_DEF);
    }

    private void initViewItems(View rootView){
        headerFrame = (FrameLayout) rootView.findViewById(R.id.headerFrame);
        entriesTable = (TableLayout) rootView.findViewById(R.id.entriesTable);
        entriesScrollView = (ScrollView) rootView.findViewById(R.id.entriesScrollView);
        fakeScrollView = (CustomScrollView) rootView.findViewById(R.id.fakeScroll);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loginProgressBar);
        dummyView = rootView.findViewById(R.id.dummyView);
        horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView);

        fakeScrollView.setOnTouchEventListener(new CustomScrollView.OnTouchEventListener() {
            @Override
            public boolean onTouchEvent(MotionEvent ev) {
                ev.offsetLocation(0, headerFrame.getHeight());
                horizontalScrollView.dispatchTouchEvent(ev);
                return true;
            }
        });

        layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        headerFrame.setVisibility(View.INVISIBLE);
        entriesTable.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.entries_activity_actions, menu);

        menu.findItem(R.id.action_previous).setVisible(false); // hide previous
        if(pageCount<=1)
            menu.findItem(R.id.action_next).setVisible(false); // hide next if we don't have any more pages
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void changeMenus(int page){
        if(page==1){
            menu.findItem(R.id.action_previous).setVisible(false);
        } else if (page==2){
            menu.findItem(R.id.action_previous).setVisible(true);
        }
        if (page==(pageCount-1)) {
            menu.findItem(R.id.action_next).setVisible(true);
        } else if (page==pageCount) {
            menu.findItem(R.id.action_next).setVisible(false);
        }
    }

    private void setLoading(boolean isLoading){
        if(menu != null) {
            menu.findItem(R.id.action_next).setEnabled(!isLoading);
            menu.findItem(R.id.action_previous).setEnabled(!isLoading);
        }
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }

    private void loadAnotherPage(){
        changeMenus(page);
        entriesTable.removeAllViews(); // clean table

        setLoading(true);
        Static.asyncDatabaseConnector.getRows(tableName, entriesLimit, page); // get new entries
    }

    private void addNewElement(){
        Intent intent = new Intent(getActivity(), ElementActivity.class);
        intent.putExtra(Static.DATABASE_NAME_ARG,databaseName);
        intent.putExtra(Static.TABLE_NAME_ARG,tableName);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Static.isNetworkConnected(getActivity())) {
            switch (item.getItemId()) {
                case R.id.action_previous:
                    page--;
                    loadAnotherPage();
                    break;
                case R.id.action_next:
                    page++;
                    loadAnotherPage();
                    break;
                case R.id.action_add:
                    addNewElement();
                    break;
            }
        } else {
            Static.showErrorAlert(getResources().getString(R.string.no_connection), getActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMatrixReturn(List<List<String>> rows) {
        // Now we get Rows
        if(rows!=null) {
            int background;
            for (int i = 0; i < rows.size(); i++) {
                List<String> elements = rows.get(i);
                TableRow newRow = new TableRow(getActivity());

                if( i%2 == 0 ){ // Two backgrounds for lines for better visibility
                    background=R.drawable.entries_element_1;
                } else {
                    background=R.drawable.entries_element_2;
                }

                for (int j = 0; j < elements.size(); j++) { // elements.size can be the same as in header so maybe some one number or not
                    TextView textView = new TextView(getActivity());
                    textView.setText(elements.get(j));
                    textView.setLayoutParams(layoutParams);
                    textView.setPadding(ENTRIES_PADDING_LEFT, ENTRIES_PADDING_TOP, ENTRIES_PADDING_RIGHT, ENTRIES_PADDING_BOTTOM);
                    textView.setBackgroundResource(background);
                    textView.setId(j);
                    newRow.addView(textView);
                }
                newRow.setClickable(true);
                newRow.setOnClickListener(this);
                entriesTable.addView(newRow);

                syncWidths();
                fakeScroll();
            }
        } else {
            TextView errorMessage = new TextView(getActivity());
            errorMessage.setText(R.string.error_no_entries);
            errorMessage.setTypeface(null, Typeface.ITALIC);
            errorMessage.setClickable(false);
            entriesScrollView.removeView(entriesTable);
            headerFrame.setVisibility(View.VISIBLE);
            entriesScrollView.addView(errorMessage);
        }

        setLoading(false);
    }

    @Override
    public void onListReturn(List<String> fieldList) {
        // First we need header
        TableRow headerRow;
        headerRow = new TableRow(getActivity());
        headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        rowCount = fieldList.size();
        for (String aFieldList : fieldList) {
            TextView textView = new TextView(getActivity());
            textView.setText(aFieldList);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setLayoutParams(layoutParams);
            textView.setBackgroundResource(R.drawable.background_header);
            textView.setPadding(HEADER_PADDING_LEFT, HEADER_PADDING_TOP, HEADER_PADDING_RIGHT, HEADER_PADDING_BOTTOM);
            headerRow.addView(textView);
        }
        headerFrame.addView(headerRow);

        Static.asyncDatabaseConnector.getRows(tableName, entriesLimit, page);
    }

    @Override
    public void onIntegerReturn(int result) {
        pageCount = result/entriesLimit;
        if( result%entriesLimit > 0)
            pageCount++;

        setHasOptionsMenu(true);
    }

    private void syncWidths(){ // TODO: Merge with adding columns maybe? Loops -= 3 should be quicker
        TableRow headerRow = (TableRow) headerFrame.getChildAt(0);
        int maxWidth[]= new int[headerRow.getChildCount()];
        for(int i=0;i<headerRow.getChildCount();i++){
            TextView textView = (TextView) headerRow.getChildAt(i);
            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            maxWidth[i] = textView.getMeasuredWidth();
        }

        for(int i=0;i<entriesTable.getChildCount();i++){
            TableRow tableRow = (TableRow) entriesTable.getChildAt(i);
            for(int j=0;j<tableRow.getChildCount();j++){
                TextView textView = (TextView) tableRow.getChildAt(j);
                textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int width = textView.getMeasuredWidth();
                if(width>maxWidth[j])
                    maxWidth[j]=width;
            }
        }

        for(int i=0;i<headerRow.getChildCount();i++){
            TableRow entriesRow = (TableRow) entriesTable.getChildAt(0);

            TextView tmpEntries = (TextView) entriesRow.getChildAt(i);
            TextView tmpHeader = (TextView) headerRow.getChildAt(i);

            tmpEntries.setWidth(maxWidth[i]);
            tmpHeader.setWidth(maxWidth[i]);
        }

        headerFrame.setVisibility(View.VISIBLE);
        entriesTable.setVisibility(View.VISIBLE);
    }

    private void fakeScroll(){
        entriesScrollView.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        int height = entriesScrollView.getMeasuredHeight();
        dummyView.setMinimumHeight(height);

        RelativeLayout.LayoutParams fakeScrollLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fakeScrollLayout.setMargins(0,headerFrame.getHeight(),0,0);

        fakeScrollView.setLayoutParams(fakeScrollLayout);
    }

    @Override
    public void onClick(View view) {
        ArrayList<String> values = new ArrayList<String>();
        for(int i=0;i<rowCount;i++){
            TextView element =  (TextView)view.findViewById(i);
            values.add(element.getText().toString());
//            element.setBackgroundResource(android.R.color.holo_blue_bright);
        }

        Intent intent = new Intent(getActivity(), ElementActivity.class);
        intent.putExtra(Static.DATABASE_NAME_ARG,databaseName);
        intent.putExtra(Static.TABLE_NAME_ARG,tableName);
        intent.putExtra(ElementFragment.EDIT_ELEMENT, true);
        intent.putStringArrayListExtra(ElementFragment.EDIT_LIST, values);
        startActivity(intent);
    }
}
