package info.nerull7.mysqlbrowser;

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

    public DatabaseConnector(String login, String password, String url){
        this.login = login;
        this.password = password;
        this.url = url;
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
}
