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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //TODO Handling menu
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
