package info.nerull7.mysqlbrowser;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nerull7 on 15.07.14.
 */
public class EntriesFragment extends Fragment {
    private String databaseName;
    private String tableName;
    private TableLayout entriesTable;
    private ScrollView entriesScrollView;
    private FrameLayout headerFrame;
    private int entriesLimit;

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
        setupTable();
        return rootView;
    }

    private void setupTable(){ // TODO Empty table handling
        List<String> fieldList = Static.databaseConnector.getFields(tableName);

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        // First we need header
        final TableRow headerRow = new TableRow(getActivity());
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

        // Now we get Rows
        List<List<String>> rows = Static.databaseConnector.getRows(tableName, entriesLimit);
        for(int i=0;i<rows.size();i++){
            List<String> elements = rows.get(i);
            TableRow newRow = new TableRow(getActivity());
            for(int j=0;j<elements.size();j++) { // elements.size can be the same as in header so maybe some one number or not
                TextView textView = new TextView(getActivity());
                textView.setText(elements.get(j));
                textView.setLayoutParams(layoutParams);
                newRow.addView(textView);
            }
            entriesTable.addView(newRow);
        }

        entriesTable.addView(headerRow);
    }
}
