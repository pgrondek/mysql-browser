package info.nerull7.mysqlbrowser;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import info.nerull7.mysqlbrowser.db.AsyncDatabaseConnector;

/**
 * Created by nerull7 on 30.09.14.
 */
public class SQLFragment extends Fragment implements AsyncDatabaseConnector.OnPostExecuteListener {
    private EditText sqlEditText;
    private InputMethodManager inputMethodManager;
    private SQLEntriesFragment sqlEntriesFragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sql, container, false);
        sqlEditText = (EditText) rootView.findViewById(R.id.sqlQueryText);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(sqlEditText, InputMethodManager.SHOW_FORCED);
        sqlEditText.requestFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_execute:
                actionExecute();
                break;
            case R.id.action_cancel:
                actionCancel();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sql, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void actionCancel(){
        inputMethodManager.hideSoftInputFromWindow(sqlEditText.getWindowToken(), 0);
        getActivity().finish();
    }

    private void actionExecute(){
        String sqlQuery = String.valueOf(sqlEditText.getText());
        Log.d("SQLQUERY", sqlQuery);

        fragmentTransaction = getFragmentManager().beginTransaction();
        sqlEntriesFragment = new SQLEntriesFragment();
        fragmentTransaction.replace(R.id.container, sqlEntriesFragment);
        fragmentTransaction.commit();

        Static.asyncDatabaseConnector.setStringReturnListener(sqlEntriesFragment);
        Static.asyncDatabaseConnector.setListReturnListener(sqlEntriesFragment);
        Static.asyncDatabaseConnector.setMatrixReturnListener(sqlEntriesFragment);
        Static.asyncDatabaseConnector.setOnPostExecuteListener(this);
        Static.asyncDatabaseConnector.executeSQL(getActivity().getIntent().getExtras().getString(Static.DATABASE_NAME_ARG), sqlQuery);
    }

    @Override
    public void onPostExecute() {
        sqlEntriesFragment.onPostExecute();
    }
}
