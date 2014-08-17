package info.nerull7.mysqlbrowser;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Base64;

/**
 * Created by nerull7 on 18.07.14.
 */
public class SettingsFragment extends PreferenceFragment implements NumberPickerDialog.OnNumberSetListener, Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    public static final String ENTRIES_PAGE_LIMIT = "entries_limit";
    public static final String SAVE_SERVER_CREDENTIALS = "save_credentials_enabled";
    public static final String URL_CREDENTIALS = "url";
    public static final String LOGIN_CREDENTIALS = "login";
    public static final String PASSWORD_CREDENTIALS = "password";

    public static final int ENTRIES_PAGE_LIMIT_DEF = 20;
    public static final int ENTRIES_MIN_PAGE = 20;
    public static final int ENTRIES_MAX_PAGE = 100;

    private SharedPreferences preferences;
    private Preference mEntriesLimit;
    private CheckBoxPreference saveCredentials;
    private EditTextPreference connectorUrlCredentials;
    private EditTextPreference loginCredentials;
    private EditTextPreference passwordCredentials;

    private Crypto crypto;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        preferences = getPreferenceManager().getDefaultSharedPreferences(getActivity());
        crypto = new Crypto(getActivity());

        loadPrefs();
    }

    private void loadPrefs(){
        addPreferencesFromResource(R.xml.settings);

        // Getting fields
        mEntriesLimit = findPreference(ENTRIES_PAGE_LIMIT);
        saveCredentials = (CheckBoxPreference) findPreference(SAVE_SERVER_CREDENTIALS);
        connectorUrlCredentials = (EditTextPreference) findPreference(URL_CREDENTIALS);
        loginCredentials = (EditTextPreference) findPreference(LOGIN_CREDENTIALS);
        passwordCredentials = (EditTextPreference) findPreference(PASSWORD_CREDENTIALS); // TODO: Some encryption

        // Settings fields
        setEntriesPageLimitSummary();
        setPasswordCredentials();

        // Settings Listener
        saveCredentials.setOnPreferenceClickListener(this);
        passwordCredentials.setOnPreferenceChangeListener(this);
    }

    private void setSaveServerCredentials(boolean isEnabled){
        SharedPreferences.Editor editor = preferences.edit();

        if(!isEnabled){ // Cleaning Credentials
            editor.remove(URL_CREDENTIALS);
            editor.remove(LOGIN_CREDENTIALS);
            editor.remove(PASSWORD_CREDENTIALS);
        }
        editor.putBoolean(SAVE_SERVER_CREDENTIALS, isEnabled);
        editor.apply();
        editor.commit();

        if(!isEnabled)
            reloadLoginPrefsView();
    }

    private void reloadLoginPrefsView(){ // Update values
        saveCredentials.setChecked(preferences.getBoolean(SAVE_SERVER_CREDENTIALS, false));
        connectorUrlCredentials.setText(preferences.getString(URL_CREDENTIALS, null));
        loginCredentials.setText(preferences.getString(LOGIN_CREDENTIALS, null));
        passwordCredentials.setText(preferences.getString(PASSWORD_CREDENTIALS, null));;
    }

    private int getEntriesPageLimit(){
        return preferences.getInt(ENTRIES_PAGE_LIMIT, ENTRIES_PAGE_LIMIT_DEF);
    }

    private void setEntriesPageLimitSummary(){
        mEntriesLimit.setSummary(getString(R.string.entries_summary, getEntriesPageLimit()));
    }

    private void setPasswordCredentials(){
        String password;
        password = preferences.getString(PASSWORD_CREDENTIALS, null);
        if(password != null)
            try {
                passwordCredentials.setText(crypto.decryptBase64(password));
            } catch (Exception e) { e.printStackTrace();  } // TODO: Something useful
    }

    private void savePassword(String password){
        try {
            SharedPreferences.Editor editor = preferences.edit();
            byte [] encryptedPassword;
            encryptedPassword = crypto.encrypt(password);
            String passwordBase64 = Base64.encodeToString(encryptedPassword, Base64.DEFAULT);
            editor.putString(PASSWORD_CREDENTIALS, passwordBase64);
            editor.apply();
        } catch (Exception e) { e.printStackTrace(); } // TODO: Something useful
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

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference==saveCredentials){
            setSaveServerCredentials(saveCredentials.isChecked());
        }
        else
            return false;

        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(preference == passwordCredentials){
            savePassword((String) newValue);
        }
        return false;
    }
}
