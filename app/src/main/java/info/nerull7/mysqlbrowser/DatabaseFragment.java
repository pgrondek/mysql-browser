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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nerull7 on 14.07.14.
 */
public class DatabaseFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView databasesListView;
    private ListAdapter listAdapter;
    private RelativeLayout rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_database, container, false);
        databasesListView = (ListView) rootView.findViewById(R.id.databaseList);
        this.rootView = (RelativeLayout) rootView;

        setupListViewDatabase();
        return rootView;
    }

    private void setupListViewDatabase(){
        List<String> databases = Static.databaseConnector.getDatabases();
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
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String choosenDatabase =  (String) listAdapter.getItem(position);
        listAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), TableActivity.class);
        intent.putExtra("DatabaseName",choosenDatabase);
        Static.databaseConnector.setDatabaseInUse(choosenDatabase);
        startActivity(intent);
    }
}
