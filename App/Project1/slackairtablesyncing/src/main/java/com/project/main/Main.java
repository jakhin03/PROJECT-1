package com.project.main;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.project.airtableAPI.AirTableAPI;

public class Main {
	public static void main(String[] args) throws IOException {
//		Slack slack = Slack.getInstance();
//		MethodsClient methods = slack.methods();
//
//		// Fetch channels
//		List<Conversation> channels = fetchChannels(methods);
//		if (channels != null) {
//			writeChannelsToCSV(channels, "I:/HUST/HUST-2022-2/Project 1/SlackAirtableSync/channels.csv");
//		}
//
//		// Fetch users
//		List<User> users = fetchUsers(methods);
//		if (users != null) {
//			writeUsersToCSV(users, "I:/HUST/HUST-2022-2/Project 1/SlackAirtableSync/users.csv");
//		}

		String apiKey = "patZfsZBLOgxbJqSi.ed6210cfb2e0b2049bd8e7fa0e69f26a3a704412af6fcbd93a097c42507fc892";
		String baseId = "appNRtbTlWVuJ1lRQ";
		String tableNameOrId = "tbl9zdg4EZZvfFAtz";
		String recordId = "recAZ9l3YNMwN3mUJ";
		String jsonData = "{Status:abccb,Notes:d,Name:asdfsdf}";
		AirTableAPI airtableAPI = new AirTableAPI(apiKey,baseId);
		
		JSONObject fields = new JSONObject();
        fields.put("Status", "John Doe");
        fields.put("Notes", "john.doe@example.com");
        fields.put("Name", "555-1234");
        System.out.println(fields.toString());
        airtableAPI.createRecord(tableNameOrId, fields);
		
		JSONArray result3 = airtableAPI.listRecords(tableNameOrId);
		System.out.println(result3.toString());
	}
	
	public static void createChannel() {
		
	}
	
	public static void addUser() {
		
	}
	
	
}
