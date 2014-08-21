package info.nerull7.mysqlbrowser;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import info.nerull7.mysqlbrowser.db.AsyncDatabaseConnector;

/**
 * Created by nerull7 on 07.07.14.
 *
 * Fragment for login
 */
public class LoginFragment extends Fragment implements View.OnClickListener, AsyncDatabaseConnector.BooleanReturnListener, AsyncDatabaseConnector.OnPostExecuteListener {
    private EditText urlTextbox;
    private EditText loginTextbox;
    private EditText passwordTextbox;
    private ProgressBar progressBar;
    private Button loginButton;

    AsyncDatabaseConnector asyncDatabaseConnector;

    private boolean result;

    public LoginFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = (Button) rootView.findViewById(R.id.buttonLogin);
        urlTextbox = (EditText) rootView.findViewById(R.id.editURL);
        loginTextbox = (EditText) rootView.findViewById(R.id.editLogin);
        passwordTextbox = (EditText) rootView.findViewById(R.id.editPassowrd);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loginProgressBar);

        loginButton.setOnClickListener(this);
        processCredentials();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        processCredentials();
    }

    private void processCredentials() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(sharedPreferences.getBoolean(SettingsFragment.SAVE_SERVER_CREDENTIALS, false)){
            urlTextbox.setText(sharedPreferences.getString(SettingsFragment.URL_CREDENTIALS, null));
            loginTextbox.setText(sharedPreferences.getString(SettingsFragment.LOGIN_CREDENTIALS, null));
            Crypto crypto = new Crypto(getActivity());
            String password = sharedPreferences.getString(SettingsFragment.PASSWORD_CREDENTIALS, null);
            if(password!=null) {
                try {
                    passwordTextbox.setText(crypto.decryptBase64(password));
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false); // Blocks multiple clicks
        checkLogin();
    }

    private void checkLogin(){
        String login, password, url;
        login = loginTextbox.getText().toString();
        password = passwordTextbox.getText().toString();
        url = urlTextbox.getText().toString();

        if(Static.isNetworkConnected(getActivity())) {
            asyncDatabaseConnector = new AsyncDatabaseConnector(login, password, url, getActivity().getResources());
            asyncDatabaseConnector.setBooleanReturnListener(this);
            asyncDatabaseConnector.setOnPostExecuteListener(this);
            asyncDatabaseConnector.checkLogin();
        } else {
            Static.showErrorAlert(getResources().getString(R.string.no_connection), getActivity());
            loginButton.setEnabled(true); // Now we can click button again
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBooleanReturn(boolean result) {
        this.result = result;
    }


    @Override
    public void onPostExecute() {
        if(result) {
            Static.asyncDatabaseConnector = asyncDatabaseConnector;
            Intent intent = new Intent(getActivity(), ListActivity.class);
            intent.putExtra(Static.FRAGMENT_TO_START, Static.FRAGMENT_DATABASE);
            startActivity(intent);
        }
        else {
            Static.showErrorAlert(AsyncDatabaseConnector.errorMsg, getActivity());
        }

        loginButton.setEnabled(true); // Now we can click button again
        progressBar.setVisibility(View.INVISIBLE);
    }
}
