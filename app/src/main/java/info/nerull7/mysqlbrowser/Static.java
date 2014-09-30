package info.nerull7.mysqlbrowser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import info.nerull7.mysqlbrowser.db.AsyncDatabaseConnector;

/**
 * Created by nerull7 on 14.07.14.
 */
public class Static {
    public static final String DATABASE_NAME_ARG = "DatabaseName";
    public static final String TABLE_NAME_ARG = "TableName";

    public static final String FRAGMENT_TO_START = "FragmentStarter";
    public static final String FRAGMENT_DATABASE = "DatabaseFragment";
    public static final String FRAGMENT_TABLE = "TableFragment";

    public static AsyncDatabaseConnector asyncDatabaseConnector = null;

    public static void startSettings(Context context){
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            return true;
        } else
            return false;
    }

    public static void showErrorAlert(String errorMessage, Context context){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(errorMessage);
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

    public static void startSQL(Context context) {
        Intent intent = new Intent(context, SQLActivity.class);
        context.startActivity(intent);
    }
}
