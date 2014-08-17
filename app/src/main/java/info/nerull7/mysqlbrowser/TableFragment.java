package info.nerull7.mysqlbrowser;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import info.nerull7.mysqlbrowser.db.AsyncDatabaseConnector;

/**
 * Created by nerull7 on 14.07.14.
 */
public class TableFragment extends Fragment implements AdapterView.OnItemClickListener, AsyncDatabaseConnector.ListReturnListener{
    private String databaseName;
    private ListView tablesList;
    private ListAdapter listAdapter;
    private RelativeLayout rootView;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_table, container, false);
        databaseName = getArguments().getString(Static.DATABASE_NAME_ARG);
        tablesList = (ListView) rootView.findViewById(R.id.tableList);
        this.rootView = (RelativeLayout) rootView;
        progressBar = (ProgressBar) rootView.findViewById(R.id.loginProgressBar);
        Static.asyncDatabaseConnector.setListReturnListener(this);
        Static.asyncDatabaseConnector.getTables();
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if(Static.isNetworkConnected(getActivity())) {
            String chosenTable =  (String) listAdapter.getItem(position);
            listAdapter.getItem(position);
            Intent intent = new Intent(getActivity(), EntriesActivity.class);
            intent.putExtra(Static.DATABASE_NAME_ARG,databaseName);
            intent.putExtra(Static.TABLE_NAME_ARG,chosenTable);
            startActivity(intent);
        } else {
            Static.showErrorAlert(getResources().getString(R.string.no_connection), getActivity());
        }

    }

    @Override
    public void onListReturn(List<String> tables) {
        if(tables != null) {
            listAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, tables);
            tablesList.setAdapter(listAdapter);
            tablesList.setOnItemClickListener(this);
        } else {
            TextView errorMessage = new TextView(getActivity());
            errorMessage.setText(R.string.error_no_tables);
            errorMessage.setTypeface(null, Typeface.ITALIC);
            errorMessage.setClickable(false);
            rootView.addView(errorMessage);
            rootView.removeView(tablesList);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
}
