package com.example.no1.photongallery.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyJsonArray extends JSONArray {
    JSONArray object;

    public MyJsonArray(JSONArray parent){object=parent;}

    public JSONObject getJSONObjectSafe(int index){
        JSONObject result = null;
        try {
            result = object.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isNull() {
        return object == null;
    }

    @Override
    public int length() {
        return object.length();
    }
}
