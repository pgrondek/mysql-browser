package info.nerull7.mysqlbrowser;

import info.nerull7.mysqlbrowser.db.DatabaseConnector;

/**
 * Created by nerull7 on 14.07.14.
 */
public class Static {
    public static DatabaseConnector databaseConnector = null;

    public static boolean isDatabaseConnectorActive(){
        if (databaseConnector==null)
            return false;
        return true;
    }
}
