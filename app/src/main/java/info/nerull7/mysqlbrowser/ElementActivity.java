package info.nerull7.mysqlbrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.error_no_save));
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setTitle(R.string.warning);
        builder.setIcon(R.drawable.ic_action_warning);
        builder.create();
        builder.show();
    }
}
