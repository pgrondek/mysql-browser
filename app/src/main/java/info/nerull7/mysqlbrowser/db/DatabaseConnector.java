package info.nerull7.mysqlbrowser.db;

import java.util.List;

/**
 * Created by nerull7 on 17.07.14.
 */
public interface DatabaseConnector {
    void setDatabaseInUse(String database);

    List<String> getDatabases();

    List<String> getTables();

    List<String> getFields(String table);

    List<List<String>> getRows(String table, int count);
}
