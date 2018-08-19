package com.example.no1.photongallery.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JSONParser {

    private static InputStream is = null;
    private static JSONObject jObj = null;
    private static String json = "";

    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET method
    public JSONObject makeHttpRequest(String url, String method,
                                      JSONObject params, Context context) {
        // Making HTTP request
        try {

            // check for request method
            if (method.equals("POST")) {
                // request method is POST
                // defaultHttpClient
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setSoTimeout(httpParams, 5000);
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
                HttpPost httpPost = new HttpPost(url);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                String token = sp.getString("token", "N/A");
                if (!token.equals("N/A"))
                    httpPost.addHeader("Authorization", "Token " + token);
                //httpPost.addHeader("Authorization", "Token " + "978dbc5f4007bd28d33615689b90b78d2d0e0311");
                StringEntity se = new StringEntity(params.toString(), "UTF-8");
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(se);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "US-ASCII"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public JSONObject makeHttpRequest(String url, String method,
                                      List<NameValuePair> params, Context context) {
        // Making HTTP request
        try {

            // check for request method
            if (method.equals("POST")) {
                // request method is POST
                // defaultHttpClient
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                HttpConnectionParams.setSoTimeout(httpParams, 5000);
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
                HttpPost httpPost = new HttpPost(url);
//                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//                String token = sp.getString("token", "N/A");
//                if (!token.equals("N/A"))
                    httpPost.addHeader("Authorization", "Token " + "b4dd0d9e86ce1e10ca7481758716ddf98ad4f46d");
                //httpPost.addHeader("Authorization", "Token " + "978dbc5f4007bd28d33615689b90b78d2d0e0311");
                httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } else if (method.equals("GET")) {
                // request method is GET
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                HttpConnectionParams.setSoTimeout(httpParams, 5000);
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
                String paramString = URLEncodedUtils.format(params, "utf-8");
                if (paramString.length() > 0)
                    url += "?" + paramString;
//                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//                String token = sp.getString("token", "N/A");
                HttpGet httpGet = new HttpGet(url);
//                if (!token.equals("N/A"))
                    httpGet.addHeader("Authorization", "Token " + "b4dd0d9e86ce1e10ca7481758716ddf98ad4f46d");
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public int makeHttpDelete(String url, String method,
                              List<NameValuePair> params, Context context) {
        int result = -1;
        try {
            if (method.equals("DELETE")) {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                HttpConnectionParams.setSoTimeout(httpParams, 5000);
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
                String paramString = URLEncodedUtils.format(params, "utf-8");
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                String token = sp.getString("token", "N/A");
                HttpDelete httpDelete = new HttpDelete(url);
                if (!token.equals("N/A"))
                    httpDelete.addHeader("Authorization", "Token " + token);
                HttpResponse httpResponse = httpClient.execute(httpDelete);
                StatusLine statusLine = httpResponse.getStatusLine();
                result = statusLine.getStatusCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int makeHttpRate(String url, String method,
                            List<NameValuePair> params, Context context) {
        int result = -1;
        try {
            if (method.equals("POST")) {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                HttpConnectionParams.setSoTimeout(httpParams, 5000);
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
                HttpPost httpPost = new HttpPost(url);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                String token = sp.getString("token", "N/A");
                if (!token.equals("N/A"))
                    httpPost.addHeader("Authorization", "Token " + token);
                httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                StatusLine statusLine = httpResponse.getStatusLine();
                result = statusLine.getStatusCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

