package info.nerull7.mysqlbrowser.db;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nerull7 on 07.07.14.
 */
public class AsyncDatabaseConnector {
    private String login;
    private String password;
    private String url;

    private String database;
    private String table;

    private BooleanReturnListener booleanReturnListener;
    private StringReturnListener stringReturnListener;
    private ListReturnListener listReturnListener;
    private MatrixReturnListener matrixReturnListener;

    public static String errorMsg;

    public AsyncDatabaseConnector(String login, String password, String url){
        this.login = login;
        this.password = password;
        this.url = url;

        booleanReturnListener=null;
        stringReturnListener=null;
        listReturnListener=null;
        matrixReturnListener=null;
    }

    private String actionUrlBuilder(String action){
        String urlBuilder = url;
        urlBuilder += "?u="+login;
        urlBuilder += "&p="+password;
        urlBuilder += "&a="+action;
        Log.d("URLBuilder", urlBuilder);
        return urlBuilder;
    }

    public void setDatabaseInUse(String database){
        this.database = database;
    }

    private void getList(String urlQuery){
        Downloader downloader = new Downloader(new Downloader.OnFinishedListener() {
            @Override
            public void onFinished(String data) {
                List<String>list = null;
                try {
                    list = parseListFromJSON(data);
                } catch (JSONException e) { e.printStackTrace(); }
                if(listReturnListener!=null)
                    listReturnListener.onReturn(list);
            }
        });
    }

    private void getMatrix(String urlQuery){
        Downloader downloader = new Downloader(new Downloader.OnFinishedListener() {
            @Override
            public void onFinished(String data) {
                List<List<String>> list = null;
                try {
                    list = parseMatrixFromJSON(data);
                } catch (JSONException e) { e.printStackTrace(); }
                if(matrixReturnListener!=null)
                    matrixReturnListener.onReturn(list);
            }
        });
    }

    private List<String> parseListFromJSON(String jsonListString) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonListString);
        List<String> databaseStringList = new ArrayList<String>();
        for(int i=0;i<jsonArray.length();i++){
            databaseStringList.add(jsonArray.getString(i));
        }
        return databaseStringList;
    }

    private List<List<String>> parseMatrixFromJSON(String jsonMatrixString) throws JSONException {
        JSONArray jsonMatrix = new JSONArray(jsonMatrixString);
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
    }

    public boolean checkLogin(){
        Downloader downloader = new Downloader(new Downloader.OnFinishedListener() {
            @Override
            public void onFinished(String data) {
                List<String>list = null;
                if(booleanReturnListener!=null)
                    booleanReturnListener.onReturn(data.compareTo("OK")==0);
            }
        });
        return false;
    }

    public void getDatabases(){
        getList(actionUrlBuilder("dblist")); // TODO Redefine as public static final
    }

    public void getTables(){
        getList(actionUrlBuilder("tablelist")+"&d="+database); // TODO Redefine as public static final
    }

    public void getFields(String table){
        getList(actionUrlBuilder("fieldlist")+"&d="+database+"&t="+table); // TODO Redefine as public static final
    }

    public void getRows(String table, int count, int page){
        int limitStart = page * count;
        getMatrix(actionUrlBuilder("getrows")+"&d="+database+"&t="+table+"&s="+limitStart+"&l="+count); //FIXME
    }

    public void setBooleanReturnListener(BooleanReturnListener booleanReturnListener){
        this.booleanReturnListener = booleanReturnListener;
    }

    public void setStringReturnListener(StringReturnListener stringReturnListener) {
        this.stringReturnListener = stringReturnListener;
    }

    public void setListReturnListener(ListReturnListener listReturnListener) {
        this.listReturnListener = listReturnListener;
    }

    public void setMatrixReturnListener(MatrixReturnListener matrixReturnListener) {
        this.matrixReturnListener = matrixReturnListener;
    }

    public static interface BooleanReturnListener{
        public void onReturn(boolean result);
    }
    
    public static interface StringReturnListener{
        public void onReturn(String data);
    }

    public static interface ListReturnListener {
        public void onReturn(List<String> data);
    }

    public static interface MatrixReturnListener{
        public void onReturn(List<List<String>> data);
    }

    private static class Downloader extends AsyncTask<String, Void, String> {
        OnFinishedListener onFinishedListener;

        Downloader(OnFinishedListener onFinishedListener){
            this.onFinishedListener = onFinishedListener;
        }

        private String httpRequest(String urlRequest) throws IOException {
            URL url = new URL(urlRequest);
            InputStream inputStream = null;
            String response;

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();  // TODO Handling no connection
            urlConnection.setReadTimeout(10000 /* miliseconds FIXME*/);
            urlConnection.setConnectTimeout(15000 /* miliseconds FIXME */);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true); // TODO what it does?
            urlConnection.connect();

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try {
                    inputStream = urlConnection.getInputStream();
                    response = readStream(inputStream);
                } finally {
                    if(inputStream!=null)
                        inputStream.close();
                    urlConnection.disconnect();
                }
                return response;
            }
            else {
                errorMsg = "ERROR "+urlConnection.getResponseCode()+": "+urlConnection.getResponseMessage();
                return null;
            }
        }

        private String readStream(InputStream in) throws IOException {
            String streamOutput = "";
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    streamOutput += line;
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
            return streamOutput;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                return httpRequest(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            onFinishedListener.onFinished(data); // Can't be null cos we demand listener in constructor
        }

        private static interface OnFinishedListener {
            void onFinished(String data);
        }
    }
}
