package info.nerull7.mysqlbrowser.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nerull7 on 07.07.14.
 */
public class DatabaseConnector {
    private String login;
    private String password;
    private String url;

    private String database;
    private String table;

    public static String errorMsg;

    public DatabaseConnector(String login, String password, String url){
        this.login = login;
        this.password = password;
        this.url = url;
    }

    // TODO Real checking
    public static boolean checkLogin(String login, String password, String url){
        if (login.compareTo("nerull7")==0)
            return true;
        if(password.compareTo("")==0)
            errorMsg = "No Password";
        else
            errorMsg = "I don't know";
        return false;
    }

    public void setDatabaseInUse(String database){
        this.database = database;
    }

    // TODO Real connection
    public List<String> getDatabases(){
        List<String> stringList = new ArrayList<String>();
        stringList.add("Wordpress");
        stringList.add("DB1");
        stringList.add("owncloud");
        Collections.sort(stringList);
        return stringList;
    }

    // TODO Real getTables
    public List<String> getTables(){
        if(database==null) return null; // if database is not chosen return null

        List<String> stringList = new ArrayList<String>();
        stringList.add(database + ".Table1");
        stringList.add(database + ".Table2");
        stringList.add(database + ".Table3");
        return  stringList;
    }

    // TODO Real getFields
    public List<String> getFields(String table){
        if(database==null) return null; // if database is not chosen return null

        List<String> stringList = new ArrayList<String>();
        stringList.add("Field 1");
        stringList.add("Field 2");
        stringList.add("Field 3");
        stringList.add("Field 4");
        return stringList;
    }

    // TODO Real getRows
    public List<String> getRows(int count){
        if(database==null) return null; // if database is not chosen return null

        List<String> stringList = new ArrayList<String>();
        stringList.add("Data 1");
        stringList.add("Data 2");
        stringList.add("Data 3");
        stringList.add("Data 4");
        return stringList;
    }
}
