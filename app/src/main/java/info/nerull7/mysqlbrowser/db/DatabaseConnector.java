package info.nerull7.mysqlbrowser.db;

import java.util.List;

/**
 * Created by nerull7 on 17.07.14.
 */
public interface DatabaseConnector {
    void setDatabaseInUse(String database);

    // TODO Real connection
    List<String> getDatabases();

    // TODO Real getTables
    List<String> getTables();

    // TODO Real getFields
    List<String> getFields(String table);

    // TODO Real getRows
    List<List<String>> getRows(String table, int count);
}
