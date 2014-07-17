package info.nerull7.mysqlbrowser.db;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nerull7 on 07.07.14.
 */
public class RealDatabaseConnector implements DatabaseConnector {
    private String login;
    private String password;
    private String url;

    private String database;
    private String table;

    public static String errorMsg;

    public RealDatabaseConnector(String login, String password, String url){
        this.login = login;
        this.password = password;
        this.url = url;
    }

    private static void disableStrictMode(){ // TODO THIS NEEDS TO NOT BE USED, WE CAN GET
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private static String httpRequest(String urlRequest) throws IOException {
        disableStrictMode(); // FIXME
        URL url = new URL(urlRequest);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = null;
        String response;
        if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                response = readStream(inputStream);
            } finally {
                urlConnection.disconnect();
            }
            return response;
        }
        else {
//            try {
//                inputStream = new BufferedInputStream(urlConnection.getErrorStream());
//                errorMsg = readStream(inputStream);
//            } finally {
//                urlConnection.disconnect();
//            }
//            return null;
            errorMsg = "ERROR: "+urlConnection.getResponseCode();
            return null;
        }
    }

    private static String readStream(InputStream in) {
        String streamOutput = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                streamOutput += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return streamOutput;
    }

    private static String actionUrlBuilder(String login, String password, String url, String action){
        String urlBuilder = url;
        urlBuilder += "?u="+login;
        urlBuilder += "&p="+password;
        urlBuilder += "&a="+action;
        Log.d("URLBuilder", urlBuilder);
        return urlBuilder;
    }

    // TODO Real checking
    public static boolean checkLogin(String login, String password, String url){
        errorMsg = null;
        try {
            String response = httpRequest(actionUrlBuilder(login,password,url,"login"));
            if(response==null)
                return false;
            if(response.compareTo("OK")==0){
                return true;
            }
            else {
                errorMsg = response;
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        stringList.add("Field 5");
        return stringList;
    }

    // TODO Real getRows
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
