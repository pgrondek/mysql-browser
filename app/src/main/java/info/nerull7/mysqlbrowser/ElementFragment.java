package info.nerull7.mysqlbrowser;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import info.nerull7.mysqlbrowser.db.AsyncDatabaseConnector;

/**
 * Created by nerull7 on 2014-08-06.
 *
 * Fragment for editing/adding elements
 */
public class ElementFragment extends Fragment implements AsyncDatabaseConnector.ListReturnListener, AsyncDatabaseConnector.StringReturnListener, AsyncDatabaseConnector.OnPostExecuteListener {
    public static final String EDIT_ELEMENT = "edit_element";
    public static final String EDIT_LIST = "edit_element_list";

    private static final int POST_EXECUTE_NONE = 0;
    private static final int POST_EXECUTE_GET_FIELDS = 1;
    private static final int POST_EXECUTE_UPDATE_ELEMENT = 2;
    private static final int POST_EXECUTE_ADD_ELEMENT = 3;

    private String tableName;
    private ElementArrayAdapter listAdapter;

    private ProgressBar progressBar;
    private ListView listView;

    private List<String> values;
    private String message;
    private int postExecute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_element, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        listView = (ListView) rootView.findViewById(R.id.listView);

        initArguments();

        postExecute = POST_EXECUTE_NONE;
        Static.asyncDatabaseConnector.setListReturnListener(this);
        Static.asyncDatabaseConnector.setOnPostExecuteListener(this);
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
        postExecute = POST_EXECUTE_GET_FIELDS;

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
    public void onStringReturn(String data) {
        message = data;
        postExecute = POST_EXECUTE_ADD_ELEMENT;
    }

    @Override
    public void onPostExecute() {
        switch (postExecute){
            case POST_EXECUTE_GET_FIELDS:
                listView.setAdapter(listAdapter);
                progressBar.setVisibility(View.INVISIBLE);
                setHasOptionsMenu(true);
                break;
            case POST_EXECUTE_ADD_ELEMENT:
            case POST_EXECUTE_UPDATE_ELEMENT:
                showInfo(message);
                break;
        }
        // Clean after execute
        postExecute = POST_EXECUTE_NONE;
    }

    private void showInfo(String info){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(info);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        });
        /*builder.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Nothing to do just get back
            }
        });*/
        builder.setTitle(R.string.status);
        builder.setIcon(R.drawable.ic_action_warning); //TODO Change Icon
        builder.create();
        builder.show();
    }
}
