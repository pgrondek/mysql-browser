package info.nerull7.mysqlbrowser;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
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

import java.util.List;

import info.nerull7.mysqlbrowser.db.DatabaseConnector;

/**
 * Created by nerull7 on 15.07.14.
 *
 * Fragment for showing elements
 */
public class SQLEntriesFragment extends Fragment implements DatabaseConnector.MatrixReturnListener, DatabaseConnector.ListReturnListener, DatabaseConnector.OnPostExecuteListener, DatabaseConnector.StringReturnListener {
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

    private ProgressBar progressBar;
    private CustomScrollView fakeScrollView;
    private View dummyView;
    private boolean showError;

    private Menu menu;
    private TableRow headerRow;
    private int[] maxWidth;

    private boolean isFirstCreate;
    private String errorMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entries, container, false);
        initViewItems(rootView);
        showError = false;
        return rootView;
    }

    private void initViewItems(View rootView){
        headerFrame = (FrameLayout) rootView.findViewById(R.id.headerFrame);
        entriesScrollView = (ScrollView) rootView.findViewById(R.id.entriesScrollView);
        fakeScrollView = (CustomScrollView) rootView.findViewById(R.id.fakeScroll);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loginProgressBar);
        dummyView = rootView.findViewById(R.id.dummyView);
        horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView);
        entriesTable = new TableLayout(getActivity());

        fakeScrollView.setOnTouchEventListener(new CustomScrollView.OnTouchEventListener() {
            @Override
            public boolean onTouchEvent(MotionEvent ev) {
                ev.offsetLocation(0, headerFrame.getHeight());
                horizontalScrollView.dispatchTouchEvent(ev);
                return true;
            }
        });

        layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
    }

    private void setLoading(boolean isLoading){
        if(menu != null) {
            menu.findItem(R.id.action_next).setEnabled(!isLoading);
            menu.findItem(R.id.action_previous).setEnabled(!isLoading);
        }
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
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
                newRow.setClickable(false);
                entriesTable.addView(newRow);
                syncWidthsFirstStage();
            }
        } else {
            entriesTable = null;
        }

    }

    @Override
    public void onListReturn(List<String> fieldList) {
        // First we need header
        headerRow = new TableRow(getActivity());
        headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        for (String aFieldList : fieldList) {
            TextView textView = new TextView(getActivity());
            textView.setText(aFieldList);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setLayoutParams(layoutParams);
            textView.setBackgroundResource(R.drawable.background_header);
            textView.setPadding(HEADER_PADDING_LEFT, HEADER_PADDING_TOP, HEADER_PADDING_RIGHT, HEADER_PADDING_BOTTOM);
            headerRow.addView(textView);
        }
    }

    private void syncWidthsFirstStage() { // TODO: Merge with adding columns maybe? Loops -= 3 should be quicker
        maxWidth = new int[headerRow.getChildCount()];
        for (int i = 0; i < headerRow.getChildCount(); i++) {
            TextView textView = (TextView) headerRow.getChildAt(i);
            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            maxWidth[i] = textView.getMeasuredWidth();
        }

        for (int i = 0; i < entriesTable.getChildCount(); i++) {
            TableRow tableRow = (TableRow) entriesTable.getChildAt(i);
            for (int j = 0; j < tableRow.getChildCount(); j++) {
                TextView textView = (TextView) tableRow.getChildAt(j);
                textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                int width = textView.getMeasuredWidth();
                if (width > maxWidth[j])
                    maxWidth[j] = width;
            }
        }
    }

    private void syncWidthsSecondStage() {
        for(int i=0;i<headerRow.getChildCount();i++){
            TableRow entriesRow = (TableRow) entriesTable.getChildAt(0);

            TextView tmpEntries = (TextView) entriesRow.getChildAt(i);
            TextView tmpHeader = (TextView) headerRow.getChildAt(i);

            tmpEntries.setWidth(maxWidth[i]);
            tmpHeader.setWidth(maxWidth[i]);
        }
    }

    private void fakeScroll(){
        entriesScrollView.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        int height = entriesScrollView.getMeasuredHeight();
        dummyView.setMinimumHeight(height);

        RelativeLayout.LayoutParams fakeScrollLayout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerFrame.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        fakeScrollLayout.setMargins(0,headerFrame.getMeasuredHeight(),0,0);

        fakeScrollView.setLayoutParams(fakeScrollLayout);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if(!isFirstCreate) {
//            getActivity().finish();
//        } else {
//            isFirstCreate = false;
//        }
//    }

    @Override
    public void onPostExecute() {
        if(!showError) {
            if (headerFrame.getChildCount() == 0) // You can have only one child
                headerFrame.addView(headerRow);
            if (entriesTable != null) {
                syncWidthsSecondStage();
                entriesScrollView.addView(entriesTable);
                fakeScroll();
            } else {
                TextView errorMessage = new TextView(getActivity());
                errorMessage.setText(R.string.error_no_entries);
                errorMessage.setTypeface(null, Typeface.ITALIC);
                errorMessage.setClickable(false);
                entriesScrollView.addView(errorMessage);
            }
            setLoading(false);
        } else {
            setLoading(false);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(errorMessage);
            builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().finish();
                }
            });
            builder.setTitle(R.string.info);
            builder.setIcon(R.drawable.ic_action_info);
            builder.create();
            builder.show();
        }
    }

    @Override
    public void onStringReturn(String data) {
        errorMessage = data;
        showError = true;
    }
}
