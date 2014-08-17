package info.nerull7.mysqlbrowser;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by nerull7 on 18.07.14.
 *
 * Simple activity for SettingsFragment
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new SettingsFragment())
                    .commit();
        }
    }

}
