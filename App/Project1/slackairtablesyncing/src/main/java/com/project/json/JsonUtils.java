package com.project.json;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {
    public static String findIdInJsonArray(String id, JSONArray jsonArray) {
    	for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.has("fields")) {
                JSONObject fieldsObject = jsonObject.getJSONObject("fields");
                if (fieldsObject.has("id") && fieldsObject.getString("id").equals(id)) {
                    return fieldsObject.getString("id");
                }
            }
        }
        return null;
    }
}
