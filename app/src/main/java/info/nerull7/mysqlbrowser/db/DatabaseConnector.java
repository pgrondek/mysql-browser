package info.nerull7.mysqlbrowser.db;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import info.nerull7.mysqlbrowser.R;

/**
 * Created by nerull7 on 07.07.14.
 * Database connector using Async calls
 */
public class DatabaseConnector {
    public static final String ACTION_LOGIN = "login";
    public static final String ACTION_DATABASE_LIST = "dblist";
    public static final String ACTION_TABLE_LIST = "tablelist";
    public static final String ACTION_FIELD_LIST = "fieldlist";
    public static final String ACTION_ENTRIES_COUNT = "numrows";
    public static final String ACTION_DATA_MATRIX = "getrows";
    public static final String ACTION_ADD_ELEMENT = "addelement";
    public static final String ACTION_UPDATE_ELEMENT = "updateelement";
    public static final String ACTION_REMOVE_ELEMENT = "removeelement";
    public static final String ACTION_SQL_QUERY = "query";

    private String login;
    private String password;
    private String url;

    private String database;

    private final Resources resources;

    private BooleanReturnListener booleanReturnListener;
    private IntegerReturnListener integerReturnListener;
    private StringReturnListener stringReturnListener;
    private ListReturnListener listReturnListener;
    private MatrixReturnListener matrixReturnListener;

    public static String errorMsg;
    private Downloader.OnPostExecuteListener onPostExecuteListener;

    public DatabaseConnector(String login, String password, String url, Resources resources){
        this.login = login;
        this.password = password;
        this.url = url;
        this.resources = resources;

        booleanReturnListener=null;
        stringReturnListener=null;
        listReturnListener=null;
        matrixReturnListener=null;
    }

    private Request requestBuilder(String action){
        Request request = new Request(url);
        String urlData   = "u="+login
                         + "&p="+password
                         + "&a="+action;
        request.data = urlData;

        return request;
    }

    private Request actionUrlBuilder(String action, String argument, String value){
        ArrayList<String> arguments = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();
        arguments.add(argument);
        values.add(value);
        return this.actionUrlBuilder(action, arguments, values);
    }

    private Request actionUrlBuilder(String action, List<String> arguments, List<String> values){ // TODO Better UrlBuilder this is shit only for use
        Request urlBuilder = requestBuilder(action);
        for (int i = 0; i < arguments.size(); i++) {
            try {
                urlBuilder.data += "&" + arguments.get(i) + "=" + URLEncoder.encode(values.get(i), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
//        Log.d("URLBuilder", urlBuilder);
        return urlBuilder;
    }

    public void setDatabaseInUse(String database){
        this.database = database;
    }

    private void getList(Request urlQuery){
        Downloader downloader = new Downloader(new Downloader.OnFinishedListener() {
            @Override
            public void onFinished(String data, String error) {
                List<String>list = null;
                try {
                    list = parseListFromJSON(data);
                } catch (JSONException e) { e.printStackTrace(); }
                if(listReturnListener!=null) {
                    listReturnListener.onListReturn(list);
                }
                errorMsg = error;
            }
        }, onPostExecuteListener, resources);
        downloader.execute(urlQuery);
    }

    private void getMatrix(Request urlQuery){
        Downloader downloader = new Downloader(new Downloader.OnFinishedListener() {
            @Override
            public void onFinished(String data, String error) {
                List<List<String>> list = null;
                try {
                    list = parseMatrixFromJSON(data);
                } catch (JSONException e) { e.printStackTrace(); }
                if(matrixReturnListener!=null)
                    matrixReturnListener.onMatrixReturn(list);
                errorMsg = error;
            }
        }, onPostExecuteListener, resources);
        downloader.execute(urlQuery);
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
            public void onFinished(String data, String error) {
                boolean listenerData;
                if(data == null) {
                    listenerData = false;
                    errorMsg = error;
                } else if( data.compareTo("OK") == 0){
                    listenerData = true;
                } else {
                    errorMsg = data;
                    listenerData = false;
                }
                booleanReturnListener.onBooleanReturn(listenerData);
            }
        }, onPostExecuteListener, resources);
        downloader.execute(requestBuilder(ACTION_LOGIN));
        return false;
    }

    public void getDatabases(){
        getList(requestBuilder(ACTION_DATABASE_LIST));
    }

    public void getTables(){
        getList(actionUrlBuilder(ACTION_TABLE_LIST, "d", database));
    }

    public void getFields(String table){
        ArrayList<String> args = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();

        args.add("d");
        values.add(database);

        args.add("t");
        values.add(table);

        getList(actionUrlBuilder(ACTION_FIELD_LIST, args, values));
    }

    public void getRows(String table, int count, int page){
        int limitStart = (page-1) * count;
        ArrayList<String> args = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();

        args.add("d");
        values.add(database);

        args.add("t");
        values.add(table);

        args.add("s");
        values.add(String.valueOf(limitStart));

        args.add("l");
        values.add(String.valueOf(count));

        getMatrix(actionUrlBuilder(ACTION_DATA_MATRIX, args, values));
    }

    public void getEntriesCount(String table){
        ArrayList<String> args = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();

        args.add("d");
        values.add(database);

        args.add("t");
        values.add(table);

        Request urlQuery = actionUrlBuilder(ACTION_ENTRIES_COUNT, args, values);
        Downloader downloader = new Downloader(new Downloader.OnFinishedListener() {
            @Override
            public void onFinished(String data, String error) {
                if(integerReturnListener!=null)
                    integerReturnListener.onIntegerReturn(Integer.parseInt(data));
                errorMsg = error;
            }
        }, onPostExecuteListener, resources);
        downloader.execute(urlQuery);
    }

    public void addNewElement(String table, List<String> header, List<String> values) {
        this.updateElement(table, header, null, values);
    }

    public void updateElement(String table, List<String> header, List<String> oldValues, List<String> newValues){
        JSONArray headerJSON = new JSONArray();
        JSONArray newValuesJSON = new JSONArray();
        Request request;

        ArrayList<String> args = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();

        args.add("d");
        values.add(database);

        args.add("t");
        values.add(table);

        for (String aHeader : header) {
            headerJSON.put(aHeader);
        }

        for (String newValue : newValues) {
            newValuesJSON.put(newValue);
        }

        args.add("h");
        values.add(headerJSON.toString());

        args.add("v");
        values.add(newValuesJSON.toString());

        if(oldValues!=null){
            JSONArray oldValuesJSON = new JSONArray();
            for(int i=0;i<newValues.size();i++){
                oldValuesJSON.put(oldValues.get(i));
            }
            args.add("o");
            values.add(oldValuesJSON.toString());
            request = actionUrlBuilder(ACTION_UPDATE_ELEMENT, args, values);
        } else
            request = actionUrlBuilder(ACTION_ADD_ELEMENT, args, values);

        Downloader downloader = new Downloader(new Downloader.OnFinishedListener() {
            @Override
            public void onFinished(String data, String error) {
                if(stringReturnListener!=null){
                    stringReturnListener.onStringReturn(data);
                }
                errorMsg = error;
            }
        }, onPostExecuteListener, resources);
        downloader.execute(request);
    }

    public void removeElement(String table, List<String> header, List<String> values) {
        JSONArray headerJSON = new JSONArray();
        JSONArray valuesJSON = new JSONArray();
        Request request;

        ArrayList<String> args = new ArrayList<String>();
        ArrayList<String> argValues = new ArrayList<String>();

        args.add("d");
        argValues.add(database);

        args.add("t");
        argValues.add(table);

        for (String aHeader : header) {
            headerJSON.put(aHeader);
        }

        for (String value : values) {
            valuesJSON.put(value);
        }

        args.add("h");
        argValues.add(headerJSON.toString());

        args.add("v");
        argValues.add(valuesJSON.toString());

        request = actionUrlBuilder(ACTION_REMOVE_ELEMENT, args, argValues);

        Downloader downloader = new Downloader(new Downloader.OnFinishedListener() {
            @Override
            public void onFinished(String data, String error) {
                if(stringReturnListener!=null){
                    stringReturnListener.onStringReturn(data);
                }
                errorMsg = error;
            }
        }, onPostExecuteListener, resources);
        downloader.execute(request);
    }

    public void executeSQL(String database, String query){
        ArrayList<String> args = new ArrayList();
        ArrayList<String> values = new ArrayList();
        final Request request;

        if(database!=null){
            args.add("d");
            values.add(database);
        }
        args.add("q");
        values.add(query);

        request = actionUrlBuilder(ACTION_SQL_QUERY, args, values);

        Downloader downloader = new Downloader(new Downloader.OnFinishedListener() {
            @Override
            public void onFinished(String data, String error) {
                String []response = data.split("\n");

                if(response[0].equals("OK")) {
                    List<String> headerList = null;
                    try {
                        headerList = parseListFromJSON(response[1]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (listReturnListener != null) {
                        listReturnListener.onListReturn(headerList);
                    }

                    List<List<String>> dataMatrix = null;
                    try {
                        dataMatrix = parseMatrixFromJSON(response[2]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (matrixReturnListener != null)
                        matrixReturnListener.onMatrixReturn(dataMatrix);
                } else {
                    if(stringReturnListener!=null)
                        stringReturnListener.onStringReturn(data);
                }
            }
        }, onPostExecuteListener, resources);
        downloader.execute(request);
    }

    public void setBooleanReturnListener(BooleanReturnListener booleanReturnListener){
        this.booleanReturnListener = booleanReturnListener;
    }

    public void setStringReturnListener(StringReturnListener stringReturnListener) {
        this.stringReturnListener = stringReturnListener;
    }

    public void setIntegerReturnListener(IntegerReturnListener integerReturnListener){
        this.integerReturnListener = integerReturnListener;
    }

    public void setListReturnListener(ListReturnListener listReturnListener) {
        this.listReturnListener = listReturnListener;
    }

    public void setMatrixReturnListener(MatrixReturnListener matrixReturnListener) {
        this.matrixReturnListener = matrixReturnListener;
    }

    public void setOnPostExecuteListener(Downloader.OnPostExecuteListener onPostExecuteListener){
        this.onPostExecuteListener = onPostExecuteListener;
    }

    public static interface BooleanReturnListener{
        public void onBooleanReturn(boolean result);
    }

    public static interface IntegerReturnListener{
        public void onIntegerReturn(int result);
    }

    public static interface StringReturnListener{
        public void onStringReturn(String data);
    }

    public static interface ListReturnListener {
        public void onListReturn(List<String> data);
    }

    public static interface MatrixReturnListener{
        public void onMatrixReturn(List<List<String>> data);
    }



    private static class Downloader extends AsyncTask<Request, Void, String> {
        private OnFinishedListener onFinishedListener;
        private OnPostExecuteListener onPostExecuteListener;
        private String errorString;
        private Resources resources;

        public static final String CONNECTION_REQUEST_METHOD = "POST";
        public static final int CONNECTION_TIMEOUT = 15000; // ms
        public static final int READ_TIMEOUT = 10000; // ms

        private Downloader(OnFinishedListener onFinishedListener, OnPostExecuteListener onPostExecuteListener, Resources resources){
            this.onFinishedListener = onFinishedListener;
            this.onPostExecuteListener = onPostExecuteListener;
            this.resources = resources;
            errorString = null;
        }

        private String httpRequest(Request urlRequest) throws IOException {
            URL url = new URL(urlRequest.url);
            InputStream inputStream = null;
            String response;

//            Log.d("URL REQUEST", urlRequest);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();  // TODO Handling no connection
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setRequestMethod(CONNECTION_REQUEST_METHOD);

            OutputStream outputStream = urlConnection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(urlRequest.data);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            outputStream.close();

            try {
                urlConnection.connect();

                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    try {
                        inputStream = urlConnection.getInputStream();
                        response = readStream(inputStream);
                    } finally {
                        if (inputStream != null)
                            inputStream.close();
                        urlConnection.disconnect();
                    }
                    return response;
                } else {
                    errorString = "ERROR " + urlConnection.getResponseCode() + ": " + urlConnection.getResponseMessage();
                }
            } catch (Exception e) {
                parseException(e);
            }
            return null;
        }

        private void parseException(Exception e){
            if(e instanceof SocketException){
                if(e.getMessage().contains("ECONNREFUSED")) {
                    errorString = resources.getString(R.string.error_connection_refused);
                } else if(e.getMessage().contains("EHOSTUNREACH")) {
                    errorString = resources.getString(R.string.error_connection_unreachable);
                } else {
                    errorString = "Exception: " + e.getClass();
                }
            } else if (e instanceof  SocketTimeoutException){
                errorString = resources.getString(R.string.error_connection_timeout);
            } else {
                errorString = "Exception: " + e.getClass();
            }
        }

        private String readStream(InputStream in) throws IOException {
            String streamOutput = "";
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    streamOutput += line + '\n';
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
            return streamOutput.substring(0, streamOutput.length()-1); // Remove last \n
        }

        @Override
        protected String doInBackground(Request... requests) {
            try {
                String data = httpRequest(requests[0]);
                onFinishedListener.onFinished(data, errorString); // Can't be null cos we demand listener in constructor
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            if (onPostExecuteListener!=null)
                onPostExecuteListener.onPostExecute();
        }

        public void setOnPostExecuteListener(OnPostExecuteListener onPostExecuteListener) {
            this.onPostExecuteListener = onPostExecuteListener;
        }

        private static interface OnFinishedListener {
            void onFinished(String data, String error);
        }

        public static interface OnPostExecuteListener {
            void onPostExecute();
        }
    }

    class Request{
        String url;
        String data;

        Request(String url){
            this.url = url;
        }
    }
}
