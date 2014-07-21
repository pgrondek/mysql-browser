package info.nerull7.mysqlbrowser;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class EntriesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);
        String titleName = getIntent().getStringExtra("DatabaseName")+"->"+getIntent().getStringExtra("TableName");
        setTitle(titleName);

        EntriesFragment entriesFragment = new EntriesFragment();
        entriesFragment.setArguments(getIntent().getExtras());
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, entriesFragment)
                    .commit();
        }
    }

}
