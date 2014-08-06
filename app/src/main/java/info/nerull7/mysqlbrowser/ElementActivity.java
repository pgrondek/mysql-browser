package info.nerull7.mysqlbrowser;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import info.nerull7.mysqlbrowser.R;

public class ElementActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);
        Bundle bundle = getIntent().getExtras();
        String titleName = bundle.getString(Static.TABLE_NAME_ARG);
        setTitle(titleName);

        ElementFragment elementFragment = new ElementFragment();
        elementFragment.setArguments(bundle);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, elementFragment)
                    .commit();
        }
    }
}
