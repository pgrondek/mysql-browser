package info.nerull7.mysqlbrowser;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import info.nerull7.mysqlbrowser.db.DatabaseConnector;

/**
 * Created by nerull7 on 14.07.14.
 */
public class DatabaseFragment extends Fragment {
    ListView databasesListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.fragment_database, container, false);
        databasesListView = (ListView)V.findViewById(R.id.databaseList);
        setupListViewDatabase();
        return V;
    }

    private void setupListViewDatabase(){
        // TODO not local constuction!!!
        DatabaseConnector databaseConnector = new DatabaseConnector("FIXME","FIXME","FIXME");
        ListAdapter listAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, databaseConnector.getDatabases());
        databasesListView.setAdapter(listAdapter);
    }
}
