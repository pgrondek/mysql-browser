package info.nerull7.mysqlbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

/**
 * Created by nerull7 on 18.07.14.
 */
public class SettingsFragment extends PreferenceFragment implements NumberPickerDialog.OnNumberSetListener {
    public static final String PREFERENCE_FILE = "preferences";
    public static final String ENTRIES_PAGE_LIMIT = "entries_limit";

    public static final int ENTRIES_PAGE_LIMIT_DEF = 20;
    public static final int ENTRIES_MIN_PAGE = 20;
    public static final int ENTRIES_MAX_PAGE = 100;

    private Activity parent;
    private SharedPreferences preferences;
    private Preference mEntriesLimit;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        loadPrefs();
    }

    private void loadPrefs(){
        addPreferencesFromResource(R.xml.settings);

        mEntriesLimit = findPreference(ENTRIES_PAGE_LIMIT);
        setEntriesPageLimitSummary();
    }

    private int getEntriesPageLimit(){
        return preferences.getInt(ENTRIES_PAGE_LIMIT, ENTRIES_PAGE_LIMIT_DEF);
    }

    private void setEntriesPageLimitSummary(){
        mEntriesLimit.setSummary(getString(R.string.entries_summary, getEntriesPageLimit()));
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if(preference == mEntriesLimit){
            new NumberPickerDialog(getActivity(), this, getEntriesPageLimit(), ENTRIES_MIN_PAGE, ENTRIES_MAX_PAGE, R.string.entries_limit).show();
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onNumberSet(int number) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ENTRIES_PAGE_LIMIT, number);
        editor.apply();
        setEntriesPageLimitSummary();
    }
}
