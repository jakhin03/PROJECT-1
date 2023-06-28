package com.project.airtableAPI;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
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
import com.project.secrets.Secrets;

public class AirTableAPI {
	
	static String apiKey = Secrets.getAPIKey();
	static String baseID = Secrets.getBaseID();
	static String tableUsersID = Secrets.getTableUsersID();
	static String tableChannelsID = Secrets.getTableChannelsID();

	public static void createOrUpdateUser(JSONObject user) throws IOException {
        String userId = user.getString("id");   //slack users
        JSONArray listUsers = listRecords(tableUsersID); //airtable users
        String existingUser = JsonUtils.findIdInJsonArray(userId, listUsers); //check if user_id slack in airtable
        if (existingUser != null) {
            updateRecord(tableUsersID, userId, user);
        } else {
            createRecord(tableUsersID, user);
        }
    }
	public static void createOrUpdateChannel(JSONObject channel) throws IOException{
        String channelId = channel.getString("id");
        JSONArray listChannels = listRecords(tableChannelsID);
        String existingChannel = JsonUtils.findIdInJsonArray(channelId, listChannels);
        if (existingChannel != null) {
            updateRecord(tableChannelsID, channelId, channel);
        } else {
            createRecord(tableChannelsID, channel);
        }
	}
	
	public static JSONObject createRecord(String tableName, JSONObject fields) throws IOException {
		String url = "https://api.airtable.com/v0/" + baseID + "/" + tableName;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setHeader("Authorization", "Bearer " + apiKey);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build());
		
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
		String url = "https://api.airtable.com/v0/" + baseID + "/" + tableName + "/" + recordId;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.setHeader("Authorization", "Bearer " + apiKey);
		httpGet.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build());
		
		HttpResponse response = httpClient.execute(httpGet);
		HttpEntity responseEntity = response.getEntity();
		String responseString = EntityUtils.toString(responseEntity, "UTF-8");
		
		JSONObject jsonResponse = new JSONObject(responseString);
		return jsonResponse;
	}
	
	public static JSONObject updateRecord(String tableName, String recordId, JSONObject fields) throws IOException {
		String url = "https://api.airtable.com/v0/" + baseID + "/" + tableName + "/" + recordId;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPatch httpPatch = new HttpPatch(url);
		
		httpPatch.setHeader("Authorization", "Bearer " + apiKey);
		httpPatch.setHeader("Content-Type", "application/json");
		httpPatch.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build());
		
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
		String url = "https://api.airtable.com/v0/" + baseID + "/" + tableName + "/" + recordId;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpDelete httpDelete = new HttpDelete(url);
		
		httpDelete.setHeader("Authorization", "Bearer " + apiKey);
		httpDelete.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build());
		
		httpClient.execute(httpDelete);
	}
	
	public static JSONArray listRecords(String tableName) throws IOException {
		String url = "https://api.airtable.com/v0/" + baseID + "/" + tableName;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.setHeader("Authorization", "Bearer " + apiKey);
		httpGet.setConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build());
		
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