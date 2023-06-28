package com.project.slackdatafetching;

import com.google.gson.GsonBuilder;
import com.project.airtableAPI.AirTableAPI;
import com.project.main.ProgressBar;
import com.project.secrets.Secrets;
import com.google.gson.Gson;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsListRequest;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.User;

import org.json.JSONArray;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SlackDataFetching {
    // Slack API credentials
	
	static String slackToken = Secrets.getSlackBotToken();
	
    public static void airtableFetching() throws IOException{
		//get listUsers and listChannels in Slack
        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods();
        
        //get listChannels
        List<Conversation> channels = fetchChannels(methods);
        String channelsString = null;
        JSONArray channelsJSON = null;
        if (channels != null) {
            channelsString = convertToString(extractChannelData(channels));
            channelsJSON = new JSONArray(channelsString);
        }
        
        //get listUsers
        List<User> users = fetchUsers(methods);
        String usersString = "";
        JSONArray usersJSON = new JSONArray();
        if (users != null) {
            usersString = convertToString(extractUserData(users));
            usersJSON = new JSONArray (usersString);
        }
 
        //create record data in airtable or update if exist
        	for (int i=0; i<usersJSON.length(); i++) {
            	JSONObject userObject = usersJSON.getJSONObject(i);
        		try {
    				AirTableAPI.createOrUpdateUser(userObject);
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        		ProgressBar.printProgressBar("Syncing users data", "In progess...",i+1, usersJSON.length());
        	}
        	for (int j=0; j<channelsJSON.length(); j++) {
        		JSONObject channelObject = channelsJSON.getJSONObject(j);
            	try {
    				AirTableAPI.createOrUpdateChannel(channelObject);
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            	ProgressBar.printProgressBar("Syncing channels data", "In progress...", j+1, channelsJSON.length());
        	}
       
    }
    
    public static void printChannels() {
        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods();

        List<Conversation> channels = fetchChannels(methods);
        if (channels != null) {
            String channelsString = convertToString(extractChannelData(channels));
            JSONArray channelsJSON = new JSONArray(channelsString);
            System.out.format("%-30s %-20s %-20s %-20s %-10s %-10s %-30s %-50s\n",
                    "Name", "ID", "Creator", "Create Date", "Privacy", "Status","Topic", "Description");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            // Print the data
            for (int i = 0; i < channelsJSON.length(); i++) {
                JSONObject channel = channelsJSON.getJSONObject(i);
                String name = channel.optString("name", "");
                String id = channel.optString("id", "");
                String topic = channel.optString("topic", "");
                if (topic.length() > 25) { // litmit maximum length of characters
                    topic = topic.substring(0, 25) + "...";
                }
                String description = channel.optString("description", "");
                if (description.length() > 50) { // litmit maximum length of characters
                    description = description.substring(0, 50) + "...";
                }
                String creator = channel.optString("creator", "");
                String createDate = channel.optString("createDate", "");
                String privacy = channel.optString("privacy", "");
                String status = channel.optString("status", "");
                System.out.format("%-30s %-20s %-20s %-20s %-10s %-10s %-30s %-50s\n",
                        name, id, creator, createDate, privacy, status,topic, description);
            }
        }
    }
    
    public static void printUsers() {
    	Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods();
        
        List<User> users = fetchUsers(methods);
        if (users != null) {
            String usersString = convertToString(extractUserData(users));
            JSONArray usersJSON = new JSONArray(usersString);
            System.out.format("%-20s %-20s %-30s %-20s %-20s %-10s %-10s %-20s %-20s\n",
                    "Name", "ID", "Email", "Display Name", "Full Name", "Status", "Role",
                    "User Create Date", "Status Change Date");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            for (int i = 0; i < usersJSON.length(); i++) {
                JSONObject user = usersJSON.getJSONObject(i);
                String name = user.optString("name", "");
                String id = user.optString("id", "");
                String email = user.optString("email", "");
                String displayName = user.optString("displayName", "");
                String fullName = user.optString("fullName", "");
                String status = user.optString("status", "");
                String role = user.optString("role", "");
                String userCreateDate = user.optString("userCreateDate", "");
                String statusChangeDate = user.optString("statusChangeDate", "");
                System.out.format("%-20s %-20s %-30s %-20s %-20s %-10s %-10s %-20s %-20s\n",
                        name, id, email, displayName, fullName, status, role,
                        userCreateDate, statusChangeDate);
            }
        }
    }

    public static List<Conversation> fetchChannels(MethodsClient methods) {
        try {
            ConversationsListRequest request = ConversationsListRequest.builder()
                    .token(slackToken)
                    .build();
            ConversationsListResponse response = methods.conversationsList(request);
            if (response.isOk()) {
                return response.getChannels();
            } else {
                System.out.println("Failed to fetch channels: " + response.getError());
            }
        } catch (IOException | SlackApiException e) {
            System.out.println("Error occurred while fetching channels: " + e.getMessage());
        }
        return null;
    }

    public static List<User> fetchUsers(MethodsClient methods) {
        try {
            UsersListRequest request = UsersListRequest.builder()
                    .token(slackToken)
                    .build();
            UsersListResponse response = methods.usersList(request);
            if (response.isOk()) {
                return response.getMembers();
            } else {
                System.out.println("Failed to fetch users: " + response.getError());
            }
        } catch (IOException | SlackApiException e) {
            System.out.println("Error occurred while fetching users: " + e.getMessage());
        }
        return null;
    }

    public static List<ChannelData> extractChannelData(List<Conversation> channels) {
        List<ChannelData> channelDataList = new ArrayList<>();
        for (Conversation channel : channels) {
            ChannelData channelData = new ChannelData();
            channelData.setName(channel.getName());
            channelData.setId(channel.getId());
            channelData.setTopic(channel.getTopic().getValue());
            channelData.setDescription(channel.getPurpose().getValue());
            channelData.setCreator(channel.getCreator());
            channelData.setCreateDate(formatDate(channel.getCreated()));
            channelData.setPrivacy(channel.isPrivate() ? "Private" : "Public");
            channelData.setStatus(channel.isArchived() ? "Archived" : "Active");
            channelDataList.add(channelData);
        }
        return channelDataList;
    }

    public static List<UserData> extractUserData(List<User> users) {
        List<UserData> userDataList = new ArrayList<>();
        for (User user : users) {
            UserData userData = new UserData();
            userData.setName(user.getName());
            userData.setId(user.getId());
            userData.setEmail(user.getProfile().getEmail());
            userData.setDisplayName(user.getProfile().getDisplayName());
            userData.setFullName(user.getProfile().getRealName());
            userData.setStatus(user.getProfile().getStatusText());
            userData.setRole(user.isOwner() ? "Owner" : "Member");
            userData.setUserCreateDate(formatDate(user.getUpdated()));
            userData.setStatusChangeDate(formatDate(user.getProfile().getStatusExpiration()));
            userDataList.add(userData);
        }
        return userDataList;
    }

    private static String formatDate(Number timestamp) {
        if (timestamp != null && timestamp.longValue() > 0) {
            long timestampValue = timestamp.longValue();
            Date date = new Date(timestampValue * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return sdf.format(date);
        }
        return "N/A";
    }

    private static String convertToString(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    private static class ChannelData {
        private String name;
        private String id;
        private String topic;
        private String description;
        private String creator;
        private String createDate;
        private String privacy;
        private String status;

        public void setName(String name) {
            this.name = name;
        }
        public void setId(String id) {
            this.id = id;
        }
        public void setTopic(String topic) {
            this.topic = topic;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public void setCreator(String creator) {
            this.creator = creator;
        }
        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
        public void setPrivacy(String privacy) {
            this.privacy = privacy;
        }
        public void setStatus(String status) {
            this.status = status;
        }
    }

    private static class UserData {
        private String name;
        private String id;
        private String email;
        private String displayName;
        private String fullName;
        private String status;
        private String role;
        private String userCreateDate;
        private String statusChangeDate;

        public void setName(String name) {
            this.name = name;
        }
        public void setId(String id) {
            this.id = id;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public void setRole(String role) {
            this.role = role;
        }
        public void setUserCreateDate(String userCreateDate) {
            this.userCreateDate = userCreateDate;
        }
        public void setStatusChangeDate(String statusChangeDate) {
            this.statusChangeDate = statusChangeDate;
        }
    }

}
