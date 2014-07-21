package info.nerull7.mysqlbrowser.db;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

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
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();  // TODO Handling no connection
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

    private String actionUrlBuilder(String action){
        String urlBuilder = actionUrlBuilder(login, password, url, action);
        return urlBuilder;
    }

    public static boolean checkLogin(String login, String password, String url){
        errorMsg = null;
        try {
            String response = httpRequest(actionUrlBuilder(login,password,url,"login")); // TODO Redefine as public static final
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

    private List<String> getList(String urlQuery){
        try {
            String response = httpRequest(urlQuery);
            if(response==null)
                return null;
            JSONArray jsonArray = new JSONArray(response);
            List<String> databaseStringList = new ArrayList<String>();
            for(int i=0;i<jsonArray.length();i++){
                databaseStringList.add(jsonArray.getString(i));
            }
            return databaseStringList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getDatabases(){
        return getList(actionUrlBuilder("dblist")); // TODO Redefine as public static final
    }

    public List<String> getTables(){
        return getList(actionUrlBuilder("tablelist")+"&d="+database); // TODO Redefine as public static final
    }

    public List<String> getFields(String table){
        return getList(actionUrlBuilder("fieldlist")+"&d="+database+"&t="+table); // TODO Redefine as public static final
    }

    private List<List<String>> getMatrix(String urlQuery){
        try {
            String response = httpRequest(urlQuery);
            if(response==null)
                return null;
            JSONArray jsonMatrix = new JSONArray(response);
            List<List<String>> matrix = new ArrayList<List<String>>();
            for(int i=0;i<jsonMatrix.length();i++){
                JSONArray jsonArray = jsonMatrix.getJSONArray(i);
                List<String> list = new ArrayList<String>();
                for(int j=0;j<jsonArray.length();j++){
                    list.add(jsonArray.getString(j));
                }
                matrix.add(list);
            }
            return matrix;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<List<String>> getRows(String table, int count, int page){
        int limitStart = page * count;
        return getMatrix(actionUrlBuilder("getrows")+"&d="+database+"&t="+table+"&s="+limitStart+"&l="+count); //FIXME
    }
}
