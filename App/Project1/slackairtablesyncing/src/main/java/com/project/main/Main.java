package com.project.main;

import com.project.airtableAPI.*;
import com.project.slackdatafetching.*;

import java.io.IOException;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsListRequest;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.User;

public class Main {
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		int buff = 0;

		System.out.println(
				"Slack management program\nEnter your choice:\n[1] Show Slack's channels\n[2] Show Slack user's information\n[3] Create a channels\n[4] User management\n[0] Exit");
		int option = sc.nextInt();
		while (option != 0) {
			buff++;
			switch (option) {
			case 1:
				showChannels();
				break;
			case 2:
				showUsers();
				break;
			case 3:
				createChannel();
				break;
			case 4:
				manageUsers();
				break;
			default:
				System.out.println("Invalid input!");
				break;
			}
			if (buff >= 20) {
				System.out.println("Maximum requests!");
				break;
			}
			System.out.println(
					"------------------------------------------------------------\nEnter your choice:\n[1] Browse Slack's channels\n[2] Browse Slack user's information\n[3] Create a channels\n[4] User management\n[0] Exit");
			option = sc.nextInt();

		}
		System.out.println("Program ended");
		sc.close();
	}

	public static void showUsers() throws IOException {
		String apiKey = "patPgcLpaEVhtwBoz.b7e85982f76bec4edeebac2ad85286eb8652f660f8942123e15ae9aa2b0773e4";
		String baseId = "appNRtbTlWVuJ1lRQ";
		String tableNameOrId = "tbl9zdg4EZZvfFAtz";
		AirTableAPI airtableAPI = new AirTableAPI(apiKey,baseId);
		JSONArray records = airtableAPI.listRecords(tableNameOrId);
		for (int i=0; i<records.length(); i++) {
			JSONObject userObject = records.getJSONObject(i);
			String createdTime = userObject.getString("createdTime");
			String id = userObject.getString("id");
            String status = "";
            String notes = "";
            String name = "";
			if (userObject.has("fields")) {
                JSONObject fieldsObject = userObject.getJSONObject("fields");
                if (fieldsObject.has("Status")) {
                    status = fieldsObject.getString("Status");
                }
                if (fieldsObject.has("Notes")) {
                    notes = fieldsObject.getString("Notes");
                }
                if (fieldsObject.has("Name")) {
                    name = fieldsObject.getString("Name");
                }
            }
            System.out.println("[+]User " + (i + 1) + ":");
            System.out.println(" createdTime: " + createdTime);
            System.out.println(" id: " + id);
            System.out.println(" status: " + status);
            System.out.println(" notes: " + notes);
            System.out.println(" name: " + name);
            System.out.println();
		}
	}

	public static void showChannels() throws IOException {
		String apiKey = "patPgcLpaEVhtwBoz.b7e85982f76bec4edeebac2ad85286eb8652f660f8942123e15ae9aa2b0773e4";
		String baseId = "appNRtbTlWVuJ1lRQ";
		String tableNameOrId = "tbl9zdg4EZZvfFAtz";
		AirTableAPI airtableAPI = new AirTableAPI(apiKey,baseId);
		JSONArray records = airtableAPI.listRecords(tableNameOrId);
		for (int i=0; i<records.length(); i++) {
			JSONObject userObject = records.getJSONObject(i);
			String createdTime = userObject.getString("createdTime");
			String id = userObject.getString("id");
            String status = "";
            String notes = "";
            String name = "";
			if (userObject.has("fields")) {
                JSONObject fieldsObject = userObject.getJSONObject("fields");
                if (fieldsObject.has("Status")) {
                    status = fieldsObject.getString("Status");
                }
                if (fieldsObject.has("Notes")) {
                    notes = fieldsObject.getString("Notes");
                }
                if (fieldsObject.has("Name")) {
                    name = fieldsObject.getString("Name");
                }
            }
            System.out.println("[+]Channel " + (i + 1) + ":");
            System.out.println(" createdTime: " + createdTime);
            System.out.println(" id: " + id);
            System.out.println(" status: " + status);
            System.out.println(" notes: " + notes);
            System.out.println(" name: " + name);
            System.out.println();
		}
	}

	public static void createChannel() {
		//for debug
		System.out.println("createChanenl");
		// create chanenl bang slack API

		// Them option them user sau khi da tao channel
		addUser();
	}

	public static void addUser() {
		//for debug
		System.out.println("addUser");
	}

	public static void manageUsers() {
		//for debug
		System.out.println("manageUsers");
	}

//	public static void fetchChannel() {
//		Slack slack = Slack.getInstance();
//		MethodsClient methods = slack.methods();
//		SlackDataFetching fetching = new SlackDataFetching();
//		
//		// Fetch channels
//		List<Conversation> channels = fetchChannels(methods);
//		if (channels != null) {
//			//Write channels to AirTable
//		}
//
//		// Fetch users
//		List<User> users = fetchUsers(methods);
//		if (users != null) {
//			//Write users to AirTable
//		}
//	}

}
