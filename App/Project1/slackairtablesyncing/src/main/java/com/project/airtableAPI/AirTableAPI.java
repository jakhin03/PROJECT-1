package com.project.airtableAPI;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	
	public static void createLogs(String description) {
		LocalDateTime now = LocalDateTime.now();		//Get current timme
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String formattedDateTime = now.format(formatter);
		
		JSONObject logsObject = new JSONObject();
		logsObject.put("Time", formattedDateTime);
		logsObject.put("Description", description);	
		try {
			createRecord(Secrets.getTableLogsId(), logsObject);
		} catch (IOException e) {
			System.out.println("Cant create logs!");
		}
	}

	public static void checkDeletedRecord(String airtableTableID, JSONObject airtableRecordObject, JSONArray slackRecordArray) throws IOException {
		boolean is_deleted = true;
		String airtableRecordId = airtableRecordObject.getString("id");   //AirTable users
		JSONObject existingRecordObject = airtableRecordObject.getJSONObject("fields");
		String existingRecordId = existingRecordObject.getString("id");
        String is_existed = JsonUtils.findIdInSlackJsonArray(existingRecordId, slackRecordArray); //check if user_id slack in airtable
        if (is_existed != null) {
            is_deleted = false;
        }
        if (is_deleted==true) {
        	JSONObject deletedSlackRecordrObject = existingRecordObject.put("is_deleted", "1");
        	updateRecord(airtableTableID, airtableRecordId, deletedSlackRecordrObject);
        }
	}
	
	public static void createOrUpdateRecord(String airtableTableID, JSONObject slackObject, JSONArray airtableArray) throws IOException{
        String slackObjectId = slackObject.getString("id");
        String existingRecordId = JsonUtils.findIdInAirtableJsonArray(slackObjectId, airtableArray);
        if (existingRecordId != null) {
            updateRecord(airtableTableID, existingRecordId, slackObject);
        } else {
            createRecord(airtableTableID, slackObject);
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