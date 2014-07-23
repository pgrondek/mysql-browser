package info.nerull7.mysqlbrowser;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import info.nerull7.mysqlbrowser.db.AsyncDatabaseConnector;

/**
 * Created by nerull7 on 07.07.14.
 */
public class LoginFragment extends Fragment implements View.OnClickListener, AsyncDatabaseConnector.BooleanReturnListener {
    private EditText urlTextbox;
    private EditText loginTextbox;
    private EditText passwordTextbox; // TODO: Mega super epic security (RSA/AES maybe?)
    private ProgressBar progressBar;

    AsyncDatabaseConnector asyncDatabaseConnector;

    public LoginFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        rootView.findViewById(R.id.buttonLogin).setOnClickListener(this);

        urlTextbox = (EditText) rootView.findViewById(R.id.editURL);
        loginTextbox = (EditText) rootView.findViewById(R.id.editLogin);
        passwordTextbox = (EditText) rootView.findViewById(R.id.editPassowrd);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loginProgressBar);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        checkLogin();
    }

    private void checkLogin(){
        String login, password, url;
        login = loginTextbox.getText().toString();
        password = passwordTextbox.getText().toString();
        url = urlTextbox.getText().toString();
        asyncDatabaseConnector = new AsyncDatabaseConnector(login, password, url);
        asyncDatabaseConnector.setBooleanReturnListener(this);
        asyncDatabaseConnector.checkLogin();
    }

    @Override
    public void onBooleanReturn(boolean result) { //TODO: FIX that Strings
        String login, password, url;
        login = loginTextbox.getText().toString();
        password = passwordTextbox.getText().toString();
        url = urlTextbox.getText().toString();

        if(result) {
            Static.asyncDatabaseConnector = asyncDatabaseConnector;
            Intent intent = new Intent(getActivity(), DatabaseActivity.class);
            startActivity(intent);
        }
        else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(Static.asyncDatabaseConnector.errorMsg);
            builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Nothing to do here
                    // Cleaning inputs is stupid
                }
            });
            builder.setTitle(R.string.error);
            builder.setIcon(R.drawable.ic_action_warning);
            builder.create();
            builder.show();
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
}
