import java.io.IOException;
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

public class AirTableAPI {
	
	private String apiKey;
	private String baseId;
	
	public AirtableHttpClient(String apiKey, String baseId) {
		this.apiKey = apiKey;
		this.baseId = baseId;
	}
	
	public JSONObject createRecord(String tableName, JSONObject fields) throws IOException {
		String url = "https://api.airtable.com/v0/" + baseId + "/" + tableName;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		
		httpPost.setHeader("Authorization", "Bearer " + apiKey);
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
	
	public JSONObject retrieveRecord(String tableName, String recordId) throws IOException {
		String url = "https://api.airtable.com/v0/" + baseId + "/" + tableName + "/" + recordId;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.setHeader("Authorization", "Bearer " + apiKey);
		
		HttpResponse response = httpClient.execute(httpGet);
		HttpEntity responseEntity = response.getEntity();
		String responseString = EntityUtils.toString(responseEntity, "UTF-8");
		
		JSONObject jsonResponse = new JSONObject(responseString);
		return jsonResponse;
	}
	
	public JSONObject updateRecord(String tableName, String recordId, JSONObject fields) throws IOException {
		String url = "https://api.airtable.com/v0/" + baseId + "/" + tableName + "/" + recordId;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPatch httpPatch = new HttpPatch(url);
		
		httpPatch.setHeader("Authorization", "Bearer " + apiKey);
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
	
	public void deleteRecord(String tableName, String recordId) throws IOException {
		String url = "https://api.airtable.com/v0/" + baseId + "/" + tableName + "/" + recordId;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpDelete httpDelete = new HttpDelete(url);
		
		httpDelete.setHeader("Authorization", "Bearer " + apiKey);
		
		httpClient.execute(httpDelete);
	}
	
	public JSONArray listRecords(String tableName) throws IOException {
		String url = "https://api.airtable.com/v0/" + baseId + "/" + tableName;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		
		httpGet.setHeader("Authorization", "Bearer " + apiKey);
		
		List<JSONObject> records = new ArrayList<JSONObject>();
		String offset = null;
		do {
			if (offset != null) {
				httpGet.setURI(url + "?offset=" + offset);
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