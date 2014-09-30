package info.nerull7.mysqlbrowser;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by nerull7 on 30.09.14.
 */
public class SQLActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new SQLFragment())
                    .commit();
        }
    }

}
