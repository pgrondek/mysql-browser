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

import info.nerull7.mysqlbrowser.db.FakeDatabaseConnector;
import info.nerull7.mysqlbrowser.db.RealDatabaseConnector;

/**
 * Created by nerull7 on 07.07.14.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    EditText urlTextbox;
    EditText loginTextbox;
    EditText passwordTextbox; // TODO: Mega super epic security (RSA/AES maybe?)

    public LoginFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        rootView.findViewById(R.id.buttonLogin).setOnClickListener(this);

        this.urlTextbox = (EditText) rootView.findViewById(R.id.editURL);
        this.loginTextbox = (EditText) rootView.findViewById(R.id.editLogin);
        this.passwordTextbox = (EditText) rootView.findViewById(R.id.editPassowrd);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        checkLogin();
    }

    private void checkLogin(){
        String login, password, url;
        login = loginTextbox.getText().toString();
        password = passwordTextbox.getText().toString();
        url = urlTextbox.getText().toString();
        if(RealDatabaseConnector.checkLogin(login, password, url)) {
            Static.databaseConnector = new RealDatabaseConnector(login, password, url);
            Intent intent = new Intent(getActivity(), DatabaseActivity.class);
            startActivity(intent);
        }
        else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(RealDatabaseConnector.errorMsg);
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
    }
}
