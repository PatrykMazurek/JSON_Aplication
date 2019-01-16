package com.example.patryyyk21.json_aplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class JSONAsyncTask extends AsyncTask<String, String, Boolean> {

    private String regUrl;
    private URL url = null;
    private Activity context;
    private ListView lvList;
    private HttpURLConnection conn;
    public ArrayList<HashMap<String, String>> itemPost;


    public JSONAsyncTask(Activity cont){
        this.context = cont;
        this.lvList = (ListView)this.context.findViewById(R.id.JSON_List);
        itemPost = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        regUrl = "http://pmazurek.sk5.eu/android/json/post/";
        //regUrl = "http://ux.up.krakow.pl/~pmazurek/JSON/post/";
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean result = false;
        conn = Connect(params[0]);
        try {
            switch (params[0]) {
                case "read":
                    ReadPost();
                    result = true;
                    break;
                case "add":
                    AddPost("1010", params[1], params[2]);
                    result = false;
                    break;
                case "update":
                    UpdatePost("1010", params[1], params[2],params[3]);
                    result = false;
                    break;
                case "delete":
                    DeletePost(params[1], params[2]);
                    result = false;
                    break;
                    default:
                        publishProgress("Nie wybrano opcji");
                        conn.disconnect();
                        result = false;
                        break;
            }
        }catch (Exception e) {
            e.printStackTrace();
            conn.disconnect();
        }
        return result;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Toast.makeText(this.context.getApplicationContext(), values[0], Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        super.onPostExecute(bool);
        if(bool){
            SimpleAdapter sAdapter = new SimpleAdapter(this.context.getApplicationContext(), itemPost, android.R.layout.simple_list_item_2,
                    new String[] {"title", "post"}, new int[]{android.R.id.text1, android.R.id.text2});
            lvList.setAdapter(sAdapter);
        }
    }

    private HttpURLConnection Connect(String action) {
        HttpURLConnection httpConn = null;
        try {
            url = new URL(regUrl + action + ".php");
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setConnectTimeout(15000);
            httpConn.setReadTimeout(15000);
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("charset", "utf-8");
            httpConn.setRequestMethod("POST");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
            httpConn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            httpConn.disconnect();
        }
        return httpConn;
    }

    private void ReadPost(){
        try {
            if(conn.getResponseCode() == 200){
                JsonReader jsonReader = new JsonReader(new InputStreamReader(
                        conn.getInputStream()));
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                if(sb.length() > 1){
                    FromJsonToList(sb.toString());
                }else{
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    publishProgress(jsonObject.getString("message"));
                }
                conn.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
        } catch (JSONException e) {
            e.printStackTrace();
            conn.disconnect();
        }
    }

    private void AddPost(String indeks, String nick, String text){

    }

    private void UpdatePost(String indeks, String id, String nick, String text){

    }

    private void DeletePost(String indeks, String id){

    }

    private void FromJsonToList(String jsonStr) {
        try{
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObj.getJSONArray("post");
            HashMap<String, String> itemList;
            for(int i = 0; i<jsonArray.length(); i++) {
                JSONObject p = jsonArray.optJSONObject(i);
                itemList = new HashMap<>();
                itemList.put("title", "(" + p.getString("id") +") " + p.getString("nick") + " " + p.getString("data"));
                itemList.put("post", p.getString("text"));
                itemPost.add(itemList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ShowMessage(InputStream input) throws IOException {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(
                input));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                input));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null){
            sb.append(line);
        }
        Log.d("problem", sb.toString());
        try {
            JSONObject jsonObject = new JSONObject(sb.toString());
            publishProgress(jsonObject.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
