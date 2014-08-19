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
 *
 * Fragment for showing list of Available Databases for user
 */
public class DatabaseFragment extends Fragment implements AdapterView.OnItemClickListener, AsyncDatabaseConnector.ListReturnListener, AsyncDatabaseConnector.OnPostExecuteListener {
    private ListView databasesListView;
    private ListAdapter listAdapter;
    private RelativeLayout rootView;
    private ProgressBar progressBar;
    private List<String> databases;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_database, container, false);
        databasesListView = (ListView) rootView.findViewById(R.id.databaseList);
        this.rootView = (RelativeLayout) rootView;
        progressBar = (ProgressBar) rootView.findViewById(R.id.loginProgressBar);

        Static.asyncDatabaseConnector.setListReturnListener(this);
        Static.asyncDatabaseConnector.setOnPostExecuteListener(this);
        Static.asyncDatabaseConnector.getDatabases();
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if(Static.isNetworkConnected(getActivity())) {
            String chosenDatabase = (String) listAdapter.getItem(position);
            listAdapter.getItem(position);
            Intent intent = new Intent(getActivity(), ListActivity.class);
            intent.putExtra(Static.FRAGMENT_TO_START, Static.FRAGMENT_TABLE);
            intent.putExtra(Static.DATABASE_NAME_ARG, chosenDatabase);
            Static.asyncDatabaseConnector.setDatabaseInUse(chosenDatabase);
            startActivity(intent);
        } else {
            Static.showErrorAlert(getResources().getString(R.string.no_connection), getActivity());
        }
    }

    @Override
    public void onListReturn(List<String> databases) {
        this.databases = databases;
    }

    @Override
    public void onPostExecute() {
        if(databases!= null) {
            listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, databases);
            databasesListView.setAdapter(listAdapter);
            databasesListView.setOnItemClickListener(this);
        } else {
            TextView errorMessage = new TextView(getActivity());
            errorMessage.setText(R.string.error_no_databases);
            errorMessage.setTypeface(null, Typeface.ITALIC);
            errorMessage.setClickable(false);
            rootView.addView(errorMessage);
            rootView.removeView(databasesListView);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
}
