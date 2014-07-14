package info.nerull7.mysqlbrowser;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by nerull7 on 07.07.14.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    EditText uri;
    EditText login;
    EditText password; // TODO: Mega super epic security (RSA/AES maybe?)

    public LoginFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        rootView.findViewById(R.id.buttonLogin).setOnClickListener(this);

        this.uri = (EditText) rootView.findViewById(R.id.editURL);
        this.login = (EditText) rootView.findViewById(R.id.editLogin);
        this.password = (EditText) rootView.findViewById(R.id.editPassowrd);

        return rootView;
    }

    @Override
    public void onClick(View view) {
//        DatabaseFragment databaseFragment = DatabaseFragment.newInstance(login.getText().toString(),
//                password.getText().toString(),
//                uri.getText().toString());
//
//        getFragmentManager().beginTransaction()
//                .add(R.id.container, databaseFragment)
//                .remove(this)
//                .commit();
        Intent intent = new Intent(getActivity(), DatabaseActivity.class);
        startActivity(intent);
    }
}
