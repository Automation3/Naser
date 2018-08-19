package com.example.no1.photongallery.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyJsonObject extends JSONObject {
    JSONObject object;

    public MyJsonObject(JSONObject parent){object=parent;}

    public String getStringSafe(String name){
        String result=null;
        try {
            result = object.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getIntSafe(String name){
        int result=-1;
        try {
            result = object.getInt(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public JSONArray getJSONArraySafe(String name){
        JSONArray result=null;
        try {
            result= object.getJSONArray(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject getJSONObjectSafe(String name){
        JSONObject result = null;
        try {
            result = object.getJSONObject(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isNull(){return object==null;}
}
