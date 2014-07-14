package info.nerull7.mysqlbrowser;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by nerull7 on 14.07.14.
 */
public class TableFragment extends Fragment implements AdapterView.OnItemClickListener {
    String databaseName;
    ListView tablesList;
    ListAdapter listAdapter;

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
        tablesList.setAdapter(listAdapter);
        tablesList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

    }
}
