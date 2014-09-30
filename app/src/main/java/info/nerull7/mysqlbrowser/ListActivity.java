package info.nerull7.mysqlbrowser;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Bundle extras = getIntent().getExtras();
        String fragmentName = extras.getString(Static.FRAGMENT_TO_START);
        Fragment fragment = null;
        if(fragmentName.compareTo(Static.FRAGMENT_DATABASE)==0){
            fragment = new DatabaseFragment();
            setTitle(R.string.title_fragment_database);
        } else if (fragmentName.compareTo(Static.FRAGMENT_TABLE)==0) {
            fragment = new TableFragment();
            setTitle(extras.getString(Static.DATABASE_NAME_ARG));
        }
        fragment.setArguments(getIntent().getExtras());
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logged, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Static.startSettings(this);
            return true;
        } else if (id == R.id.action_sql){
            Static.startSQL(this);
        }
        return super.onOptionsItemSelected(item);
    }

}
