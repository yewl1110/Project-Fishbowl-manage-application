package com.example.fish;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class NetworkConnect extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    Context context;
    URL url;
    String id;
    String serial;

    public NetworkConnect(Context context, URL url, String id, String serial){
        this.context = context;
        this.url = url;
        this.id = id;
        this.serial = serial;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();

        progressDialog = ProgressDialog.show(context, "읽어오는중", null, true, true);
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);

        progressDialog.dismiss();
        Log.d(TAG, "POST response  - " + result);
    }

    @Override
    protected String doInBackground(String... params){
        String id = (String)params[1];
        String serial = (String)params[2];

        String serverURL = (String)params[0];
        String postParameters = "id=" + id + "&serial=" + serial;


        try {

            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "POST response code - " + responseStatusCode);

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }


            bufferedReader.close();


            return sb.toString();


        } catch (Exception e) {

            Log.d(TAG, "InsertData: Error ", e);
            Toast.makeText(context, "오류", Toast.LENGTH_SHORT).show();

            return null;
        }
    }
}
