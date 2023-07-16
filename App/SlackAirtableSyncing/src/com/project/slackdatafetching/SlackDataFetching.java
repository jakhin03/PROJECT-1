package com.project.slackdatafetching;

import com.google.gson.GsonBuilder;
import com.project.airtableapi.AirTableAPI;
import com.project.json.JsonUtils;
import com.project.secrets.Secrets;
import com.google.gson.Gson;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsListRequest;
import com.slack.api.methods.request.conversations.ConversationsMembersRequest;
import com.slack.api.methods.request.users.UsersInfoRequest;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.conversations.ConversationsMembersResponse;
import com.slack.api.methods.response.users.UsersInfoResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.ConversationType;
import com.slack.api.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlackDataFetching {

	private SlackDataFetching() {
		throw new IllegalStateException(" Utility class");
	}
	// Slack API credentials
	static String slackToken = Secrets.getSlackBotToken();

    public static void airtableFetching() throws IOException {
        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods();
        
        // get list channels from Slack
        List<Conversation> slackChannels = fetchChannels(methods);
        JSONArray slackChannelArray = convertToJSONArray(extractChannelData(slackChannels));

        // get list users from Slack
        List<User> slackUsers = fetchUsers(methods);
        JSONArray slackUserArray = convertToJSONArray(extractUserData(slackUsers));

        JSONArray airtableChannelArray = AirTableAPI.listRecords(Secrets.getTableChannelsID());
        JSONArray airtableUserArray = AirTableAPI.listRecords(Secrets.getTableUsersID());

        if (slackChannelArray != null) {
            JSONObject usersInChannel = fetchChannelsWithUsers(slack, slackChannels);

            for (int j = 0; j < slackChannelArray.length(); j++) {
                JSONObject slackChannelObject = slackChannelArray.getJSONObject(j);
                String channelName = slackChannelObject.getString("name");
                JSONArray usersField = usersInChannel.getJSONArray(channelName);
                ArrayList<String> usersIdArray = new ArrayList<>();

                for (int i = 0; i < usersField.length(); i++) {
                    String userId = usersField.getString(i);
                    String existingRecordId = JsonUtils.findIdInAirtableJsonArray(userId, airtableUserArray);

                    if (existingRecordId != null) {
                        usersIdArray.add(existingRecordId);
                    }
                }

                slackChannelObject.put("Users", usersIdArray);
                AirTableAPI.createOrUpdateRecord(Secrets.getTableChannelsID(), slackChannelObject, airtableChannelArray);
            }

            for (int j = 0; j < airtableChannelArray.length(); j++) {
                JSONObject airtableChannelObject = airtableChannelArray.getJSONObject(j);
                AirTableAPI.checkDeletedRecord(Secrets.getTableChannelsID(), airtableChannelObject, slackChannelArray);
            }

            for (int i = 0; i < slackUserArray.length(); i++) {
                JSONObject slackUserObject = slackUserArray.getJSONObject(i);
                AirTableAPI.createOrUpdateRecord(Secrets.getTableUsersID(), slackUserObject, airtableUserArray);
            }

            for (int i = 0; i < airtableUserArray.length(); i++) {
                JSONObject airtableUserObject = airtableUserArray.getJSONObject(i);
                AirTableAPI.checkDeletedRecord(Secrets.getTableUsersID(), airtableUserObject, slackUserArray);
            }
        } else {
            throw new IOException("Network Error!");
        }
    }

    public static void printChannels() {
        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods();

        List<Conversation> channels = fetchChannels(methods);
        if (channels != null) {
            String channelsString = convertToString(extractChannelData(channels));
            JSONArray channelsJSON = new JSONArray(channelsString);
            System.out.format("%-30s %-20s %-20s %-20s %-10s %-10s %-30s %-50s%n", "Name", "ID", "Creator",
                    "Create Date", "Privacy", "Status", "Topic", "Description");
            System.out.println(
                    "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            // Print the data
            for (int i = 0; i < channelsJSON.length(); i++) {
                JSONObject channel = channelsJSON.getJSONObject(i);
                String name = channel.optString("name", "");
                String id = channel.optString("id", "");
                String topic = channel.optString("topic", "");
                if (topic.length() > 25) { // limit maximum length of characters
                    topic = topic.substring(0, 25) + "...";
                }
                String description = channel.optString("description", "");
                if (description.length() > 50) { // limit maximum length of characters
                    description = description.substring(0, 50) + "...";
                }
                String creator = channel.optString("creator", "");
                String createDate = channel.optString("createDate", "");
                String privacy = channel.optString("privacy", "");
                String status = channel.optString("status", "");
                System.out.format("%-30s %-20s %-20s %-20s %-10s %-10s %-30s %-50s%n", name, id, creator, createDate,
                        privacy, status, topic, description);
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
            System.out.format("%-20s %-20s %-30s %-20s %-20s %-10s %-10s %-20s %-20s%n", "Name", "ID", "Email",
                    "Display Name", "Full Name", "Status", "Role", "User Create Date", "Status Change Date");
            System.out.println(
                    "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
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
                System.out.format("%-20s %-20s %-30s %-20s %-20s %-10s %-10s %-20s %-20s%n", name, id, email,
                        displayName, fullName, status, role, userCreateDate, statusChangeDate);
            }
        }
    }

    public static List<Conversation> fetchChannels(MethodsClient methods) {
        try {
            ConversationsListRequest request = ConversationsListRequest.builder().token(slackToken)
                    .types(Arrays.asList(ConversationType.PUBLIC_CHANNEL, ConversationType.PRIVATE_CHANNEL)).build();
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
                List<User> users = response.getMembers();
                // Filter out bot users
                List<User> filteredUsers = new ArrayList<>();
                for (User user : users) {
                    if (!user.isBot()) {
                        filteredUsers.add(user);
                    }
                }
                return filteredUsers;
            } else {
                System.out.println("Failed to fetch users: " + response.getError());
            }
        } catch (IOException | SlackApiException e) {
            System.out.println("Error occurred while fetching users: " + e.getMessage());
        }
        return null;
    }

    public static JSONObject fetchChannelsWithUsers(Slack slack, List<Conversation> channels) {
        JSONObject channelUsersObject = new JSONObject();
        try {
            for (Conversation channel : channels) {
                String channelId = channel.getId();
                String channelName = channel.getName();
                ConversationsMembersResponse membersResponse = slack.methods(slackToken)
                        .conversationsMembers(ConversationsMembersRequest.builder().channel(channelId).build());

                if (membersResponse.isOk()) {
                    List<String> memberIds = membersResponse.getMembers();
                    channelUsersObject.put(channelName, memberIds);
                } else {
                    System.out.println("Failed to fetch members for channel " + channelName + ". Error: "
                            + membersResponse.getError());
                }
            }
        } catch (IOException | SlackApiException e) {
            System.out.println("Error occurred while fetching channels with users: " + e.getMessage());
        }
        return channelUsersObject;
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
            if (user.isBot() || "USLACKBOT".equals(user.getId())) {
                continue;
            }
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


    public static void printChannelsWithUsers() {
        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods();
        List<Conversation> channels = fetchChannels(methods);

        if (channels != null) {
            JSONObject channelUsersObject = fetchChannelsWithUsers(slack, channels);
            System.out.println("Channels:");

            for (Conversation channel : channels) {
                String channelId = channel.getId();
                String channelName = channel.getName();

                System.out.println("Channel: " + channelName + " (ID: " + channelId + ")");

                if (channelUsersObject.has(channelName)) {
                    JSONArray memberIds = channelUsersObject.getJSONArray(channelName);
                    List<User> members = new ArrayList<>();

                    for (int i = 0; i < memberIds.length(); i++) {
                        String memberId = memberIds.getString(i);
                        UsersInfoResponse userInfoResponse;
                        try {
                            userInfoResponse = slack.methods(slackToken)
                                    .usersInfo(UsersInfoRequest.builder().user(memberId).build());
                            if (userInfoResponse.isOk()) {
                                User user = userInfoResponse.getUser();
                                members.add(user);
                            }
                        } catch (IOException | SlackApiException e) {
                            System.out.println("Error occurred while fetching user info: " + e.getMessage());
                        }
                    }

                    System.out.println("Users in Channel:");

                    for (User user : members) {
                        System.out.println("  - " + user.getName() + " (ID: " + user.getId() + ")");
                    }
                }

                System.out.println();
            }
        }
    }

    private static String formatDate(Number timestamp) {
        if (timestamp != null && timestamp.longValue() > 0) {
            long timestampValue = timestamp.longValue();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return sdf.format(timestampValue * 1000);
        }
        return "N/A";
    }

    private static JSONArray convertToJSONArray(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(object);
        return new JSONArray(json);
    }

    private static String convertToString(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

}
