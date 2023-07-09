package com.project.json;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtils {
	
	private JsonUtils() {
		throw new IllegalStateException(" Utility class");
	}
	
	
    public static String findIdInAirtableJsonArray(String id, JSONArray jsonArray) {
    	for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.has("fields")) {
                JSONObject fieldsObject = jsonObject.getJSONObject("fields");                
                if (fieldsObject.has("id") && fieldsObject.getString("id").equals(id)) {            	
                	return jsonObject.getString("id");
                }
            }
        }
        return null;
    }
    public static String findIdInSlackJsonArray(String id, JSONArray jsonArray) {
    	for (int i = 0; i < jsonArray.length(); i++) {
    		JSONObject jsonObject = jsonArray.getJSONObject(i);
    		if (jsonObject.has("id") && jsonObject.getString("id").equals(id)) {
    			return id;
    		}
    	}
    	return null;
    }
}
