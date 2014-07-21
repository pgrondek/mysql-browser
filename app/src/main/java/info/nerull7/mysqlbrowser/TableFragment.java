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
public class TableFragment extends Fragment implements AdapterView.OnItemClickListener {
    private String databaseName;
    private ListView tablesList;
    private ListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_table, container, false);
        databaseName = getArguments().getString("DatabaseName");
        tablesList = (ListView) rootView.findViewById(R.id.tableList);
        setupList();
        return rootView;
    }

    private void setupList(){
        listAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, Static.databaseConnector.getTables());
        // TODO No tables handling
        tablesList.setAdapter(listAdapter);
        tablesList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String choosenTable =  (String) listAdapter.getItem(position);
        listAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), EntriesActivity.class);
        intent.putExtra("DatabaseName",databaseName);
        intent.putExtra("TableName",choosenTable);
        startActivity(intent);
    }
}
