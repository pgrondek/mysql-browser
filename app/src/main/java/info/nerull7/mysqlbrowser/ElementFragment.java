package info.nerull7.mysqlbrowser;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.nerull7.mysqlbrowser.db.AsyncDatabaseConnector;

/**
 * Created by nerull7 on 2014-08-06.
 */
public class ElementFragment extends Fragment implements AsyncDatabaseConnector.ListReturnListener, AsyncDatabaseConnector.StringReturnListener {
    public static final String EDIT_ELEMENT = "edit_element";
    public static final String EDIT_LIST = "edit_element_list";

    private String databaseName;
    private String tableName;
    private ElementArrayAdapter listAdapter;

    private ProgressBar progressBar;
    private ListView listView;

    private List<String> values;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_element, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        listView = (ListView) rootView.findViewById(R.id.listView);

        initArguments();

        Static.asyncDatabaseConnector.setListReturnListener(this);
        Static.asyncDatabaseConnector.getFields(tableName);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save ){
            List<String> fields = listAdapter.getFieldArray();

            Static.asyncDatabaseConnector.setStringReturnListener(this);
            if(getArguments().getBoolean(EDIT_ELEMENT))
                Static.asyncDatabaseConnector.updateElement(tableName, fields, values, getNewValues());
            else
                Static.asyncDatabaseConnector.addNewElement(tableName, fields, getNewValues());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void initArguments() {
        databaseName = getArguments().getString(Static.DATABASE_NAME_ARG);
        tableName = getArguments().getString(Static.TABLE_NAME_ARG);
        if(getArguments().getBoolean(EDIT_ELEMENT))
            values = getArguments().getStringArrayList(EDIT_LIST);
        else
            values = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.element, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onListReturn(List<String> fields) {
        listAdapter = new ElementArrayAdapter(getActivity(), R.layout.list_item_element_simple, fields, values);
        listView.setAdapter(listAdapter);
//        databasesListView.setAdapter(listAdapter);
//        databasesListView.setOnItemClickListener(this);
        progressBar.setVisibility(View.INVISIBLE);

        setHasOptionsMenu(true);
    }

    private List<String> getNewValues(){
        List<String> newValues = new ArrayList<String>();

        for(int i=0; i<listView.getChildCount();i++){
            EditText editText = (EditText) listView.getChildAt(i).findViewById(R.id.editFieldValue);
            newValues.add(String.valueOf(editText.getText()));
        }
        return newValues;
    }

    @Override
    public void onStringReturn(String data) { //TODO Better UI handling
        Static.showErrorAlert(data,getActivity());
        getActivity().finish();
    }
}
