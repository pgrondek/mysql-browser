package info.nerull7.mysqlbrowser.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nerull7 on 07.07.14.
 */
public class FakeDatabaseConnector implements DatabaseConnector {
    private String login;
    private String password;
    private String url;

    private String database;
    private String table;

    public static String errorMsg;

    public FakeDatabaseConnector(String login, String password, String url){
        this.login = login;
        this.password = password;
        this.url = url;
    }

    // TODO Real checking
    public static boolean checkLogin(String login, String password, String url){
//        if (login.compareTo("nerull7")==0)
            return true;
//        if(password.compareTo("")==0)
//            errorMsg = "No Password";
//        else
//            errorMsg = "I don't know";
//        return false;
    }

    @Override
    public void setDatabaseInUse(String database){
        this.database = database;
    }

    // TODO Real connection
    @Override
    public List<String> getDatabases(){
        List<String> stringList = new ArrayList<String>();
        stringList.add("Wordpress");
        stringList.add("DB1");
        stringList.add("owncloud");
        Collections.sort(stringList);
        return stringList;
    }

    // TODO Real getTables
    @Override
    public List<String> getTables(){
        if(database==null) return null; // if database is not chosen return null

        List<String> stringList = new ArrayList<String>();
        stringList.add(database + ".Table1");
        stringList.add(database + ".Table2");
        stringList.add(database + ".Table3");
        return  stringList;
    }

    // TODO Real getFields
    @Override
    public List<String> getFields(String table){
        if(database==null) return null; // if database is not chosen return null

        List<String> stringList = new ArrayList<String>();
        stringList.add("Field 1");
        stringList.add("Field 2");
        stringList.add("Field 3");
        stringList.add("Field 4");
        stringList.add("Field 5");
        return stringList;
    }

    // TODO Real getRows
    @Override
    public List<List<String>> getRows(int count){
        if(database==null) return null; // if database is not chosen return null
        List<List<String>> stringListList = new ArrayList<List<String>>();

        for(int i=0;i<count;i++) {
            List<String> stringList = new ArrayList<String>();
            stringList.add("Data 1"+i);
            stringList.add("Data 2"+i);
            stringList.add("Data 3"+i);
            stringList.add("Data 4"+i);
            stringList.add("Field aaa  aaaaaaaa  aaaaaa  aaaaa4"+i);
            stringListList.add(stringList);
        }
        return stringListList;
    }
}
