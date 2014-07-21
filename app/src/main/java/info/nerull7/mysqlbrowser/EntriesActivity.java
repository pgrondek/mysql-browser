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
        bundle.putInt("Page", 0);
        String titleName = bundle.getString("DatabaseName")+"->"+bundle.getString("TableName");
        setTitle(titleName);

        EntriesFragment entriesFragment = new EntriesFragment();
        entriesFragment.setArguments(bundle);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, entriesFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_previous:
            case R.id.action_next:
//                item.setEnabled(!item.isEnabled());
//                item.setVisible(!item.isVisible());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.entries_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
