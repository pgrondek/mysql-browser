package info.nerull7.mysqlbrowser;

import android.content.Context;
import android.content.Intent;

import info.nerull7.mysqlbrowser.db.AsyncDatabaseConnector;

/**
 * Created by nerull7 on 14.07.14.
 */
public class Static {
    public static AsyncDatabaseConnector asyncDatabaseConnector = null;

    public static void startSettings(Context context){
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }
}
