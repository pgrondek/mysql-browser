package info.nerull7.mysqlbrowser;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import info.nerull7.mysqlbrowser.db.AsyncDatabaseConnector;

/**
 * Created by nerull7 on 15.07.14.
 */
public class EntriesFragment extends Fragment implements AsyncDatabaseConnector.MatrixReturnListener, AsyncDatabaseConnector.ListReturnListener{
    private TableLayout entriesTable;
    private ScrollView entriesScrollView;
    private FrameLayout headerFrame;
    private RelativeLayout rootView;
    private TableRow.LayoutParams layoutParams;
    private TableRow headerRow;

    private String databaseName;
    private String tableName;
    private int entriesLimit;
    private int page;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_entries, container, false);
        databaseName = getArguments().getString("DatabaseName");
        tableName = getArguments().getString("TableName");
        entriesTable = (TableLayout) rootView.findViewById(R.id.entriesTable);
        entriesScrollView = (ScrollView) rootView.findViewById(R.id.entriesScrollView);
        headerFrame = (FrameLayout) rootView.findViewById(R.id.headerFrame);
        entriesLimit = getActivity().getSharedPreferences(SettingsFragment.PREFERENCE_FILE, Context.MODE_PRIVATE).getInt(SettingsFragment.ENTRIES_PAGE_LIMIT, SettingsFragment.ENTRIES_PAGE_LIMIT_DEF);
        this.rootView = (RelativeLayout) rootView;
        page = getArguments().getInt("Page");
        progressBar = (ProgressBar) rootView.findViewById(R.id.loginProgressBar);
//        setupActionBar();

        headerRow = new TableRow(getActivity());
        layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        Static.asyncDatabaseConnector.setListReturnListener(this);
        Static.asyncDatabaseConnector.getFields(tableName);

        return rootView;
    }

//    private void setupActionBar() {
//        ActionBar actionBar = getActivity().getActionBar();
//        actionBar.
//    }

    @Override
    public void onMatrixReturn(List<List<String>> rows) {
        // Now we get Rows
        if(rows!=null) {
            for (int i = 0; i < rows.size(); i++) {
                List<String> elements = rows.get(i);
                TableRow newRow = new TableRow(getActivity());
                for (int j = 0; j < elements.size(); j++) { // elements.size can be the same as in header so maybe some one number or not
                    TextView textView = new TextView(getActivity());
                    textView.setText(elements.get(j));
                    textView.setLayoutParams(layoutParams);
                    newRow.addView(textView);
                }
                entriesTable.addView(newRow);
            }
            entriesTable.addView(headerRow);
        } else {
            TextView errorMessage = new TextView(getActivity());
            errorMessage.setText(R.string.error_no_entries);
            errorMessage.setTypeface(null, Typeface.ITALIC);
            errorMessage.setClickable(false);
            entriesScrollView.removeView(entriesTable);
            rootView.addView(errorMessage);
            headerFrame.addView(headerRow);
            headerRow.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onListReturn(List<String> fieldList) { // TODO: Fix bug not showing header
        // First we need header
        headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        for(int i =0;i<fieldList.size();i++){
            TextView textView = new TextView(getActivity());
            textView.setText(fieldList.get(i));
            textView.setTypeface(null, Typeface.BOLD);
            textView.setLayoutParams(layoutParams);
            headerRow.addView(textView);
        }
        headerRow.setVisibility(View.INVISIBLE);

        View fakeHeaderView = new View(getActivity()){
            @Override
            public void draw(Canvas canvas) {
                headerRow.draw(canvas);
            }

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int width = headerRow.getMeasuredWidth();
                int height = headerRow.getMeasuredHeight();

                widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        };
        headerFrame.addView(fakeHeaderView);

        Static.asyncDatabaseConnector.setMatrixReturnListener(this);
        Static.asyncDatabaseConnector.getRows(tableName, entriesLimit, page);
    }
}
