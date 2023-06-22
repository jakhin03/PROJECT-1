package com.project.airtableAPI;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.project.json.JsonUtils;

public class AirTableAPI {
	
	private static final String API_KEY = "patAI3AeWkid1KoUX.76729d81c87bb327102a6667ce1fb87b0db5e26fa1436553ec56d8dfc0831c25";
	private static final String BASE_ID = "appNRtbTlWVuJ1lRQ";
	private static final String TABLE_USERS_ID = "tblgH8hhOc3S6o3T5";
	private static final String TABLE_CHANNELS_ID = "tbl5zr74HLsR1phRV";

	public static void createOrUpdateUser(JSONObject user) throws IOException {
        String userId = user.getString("id");   
        JSONArray listUsers = listRecords(TABLE_USERS_ID);
        String existingUser = JsonUtils.findIdInJsonArray(userId, listUsers);
        if (existingUser != null) {
            updateRecord(TABLE_USERS_ID, userId, user);
        } else {
            createRecord(TABLE_USERS_ID, user);
        }


    }
	public static void createOrUpdateChannel(JSONObject channel) throws IOException{
        String channelId = channel.getString("id");
        JSONArray listChannels = listRecords(TABLE_CHANNELS_ID);
        String existingChannel = JsonUtils.findIdInJsonArray(channelId, listChannels);
        if (existingChannel != null) {
            updateRecord(TABLE_CHANNELS_ID, channelId, channel);
        } else {
            createRecord(TABLE_CHANNELS_ID, channel);
        }
	}
	
	public static JSONObject createRecord(String tableName, JSONObject fields) throws IOException {
		String url = "https://api.airtable.com/v0/" + BASE_ID + "/" + tableName;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setHeader("Authorization", "Bearer " + API_KEY);
		httpPost.setHeader("Content-Type", "application/json");
		
		JSONObject body = new JSONObject();
		body.put("fields", fields);
		
		StringEntity entity = new StringEntity(body.toString());
		httpPost.setEntity(entity);
		
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity responseEntity = response.getEntity();
		String responseString = EntityUtils.toString(responseEntity, "UTF-8");
		
		JSONObject jsonResponse = new JSONObject(responseString);
		return jsonResponse;
	}
	
	public static JSONObject retrieveRecord(String tableName, String recordId) throws IOException {
		String url = "https://api.airtable.com/v0/" + BASE_ID + "/" + tableName + "/" + recordId;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.setHeader("Authorization", "Bearer " + API_KEY);
		
		HttpResponse response = httpClient.execute(httpGet);
		HttpEntity responseEntity = response.getEntity();
		String responseString = EntityUtils.toString(responseEntity, "UTF-8");
		
		JSONObject jsonResponse = new JSONObject(responseString);
		return jsonResponse;
	}
	
	public static JSONObject updateRecord(String tableName, String recordId, JSONObject fields) throws IOException {
		String url = "https://api.airtable.com/v0/" + BASE_ID + "/" + tableName + "/" + recordId;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPatch httpPatch = new HttpPatch(url);
		
		httpPatch.setHeader("Authorization", "Bearer " + API_KEY);
		httpPatch.setHeader("Content-Type", "application/json");
		
		JSONObject body = new JSONObject();
		body.put("fields", fields);
		
		StringEntity entity = new StringEntity(body.toString());
		httpPatch.setEntity(entity);
		
		HttpResponse response = httpClient.execute(httpPatch);
		HttpEntity responseEntity = response.getEntity();
		String responseString = EntityUtils.toString(responseEntity, "UTF-8");
		
		JSONObject jsonResponse = new JSONObject(responseString);
		return jsonResponse;
	}
	
	public static void deleteRecord(String tableName, String recordId) throws IOException {
		String url = "https://api.airtable.com/v0/" + BASE_ID + "/" + tableName + "/" + recordId;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpDelete httpDelete = new HttpDelete(url);
		
		httpDelete.setHeader("Authorization", "Bearer " + API_KEY);
		
		httpClient.execute(httpDelete);
	}
	
	public static JSONArray listRecords(String tableName) throws IOException {
		String url = "https://api.airtable.com/v0/" + BASE_ID + "/" + tableName;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.setHeader("Authorization", "Bearer " + API_KEY);
		
		List<JSONObject> records = new ArrayList<JSONObject>();
		String offset = null;
		do {
			if (offset != null) {
				httpGet.setURI(URI.create(url + "?offset=" + offset));
			}
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity responseEntity = response.getEntity();
			String responseString = EntityUtils.toString(responseEntity, "UTF-8");
			
			JSONObject jsonResponse = new JSONObject(responseString);
			JSONArray recordsArray = jsonResponse.getJSONArray("records");
			for (int i = 0; i < recordsArray.length(); i++) {
				JSONObject record = recordsArray.getJSONObject(i);
				records.add(record);
			}
			offset = jsonResponse.optString("offset", null);
		} while (offset != null);
		
		JSONArray result = new JSONArray(records);
		return result;
	}

}