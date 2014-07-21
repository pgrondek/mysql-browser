package info.nerull7.mysqlbrowser;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by nerull7 on 14.07.14.
 */
public class DatabaseFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView databasesListView;
    private ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_database, container, false);
        databasesListView = (ListView) rootView.findViewById(R.id.databaseList);

        setupListViewDatabase();
        return rootView;
    }

    private void setupListViewDatabase(){ // TODO: Handle no databases available problem
        listAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, Static.databaseConnector.getDatabases());
        databasesListView.setAdapter(listAdapter);
        databasesListView.setOnItemClickListener(this);
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
