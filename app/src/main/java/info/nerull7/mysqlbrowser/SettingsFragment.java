package info.nerull7.mysqlbrowser;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

/**
 * Created by nerull7 on 18.07.14.
 */
public class SettingsFragment extends PreferenceFragment{
    public final static String ENTRIES_PAGE_LIMIT = "entries_limit";

    Preference mEntriesLimit;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        loadPrefs();
    }

    private void loadPrefs(){
        addPreferencesFromResource(R.xml.settings);

        mEntriesLimit = findPreference(ENTRIES_PAGE_LIMIT);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if(preference == mEntriesLimit){
            new NumberPickerDialog
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}
