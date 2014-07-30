package info.nerull7.mysqlbrowser;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.zip.Inflater;

public class EntriesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);
        Bundle bundle = getIntent().getExtras();
        String titleName = bundle.getString(Static.TABLE_NAME_ARG);
        setTitle(titleName);

        EntriesFragment entriesFragment = new EntriesFragment();
        entriesFragment.setArguments(bundle);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, entriesFragment)
                    .commit();
        }
    }

}
