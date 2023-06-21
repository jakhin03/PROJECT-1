package com.project.slackdatafetching;

import com.google.gson.GsonBuilder;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SlackDataFetching {
    // Slack API credentials
    private static final String SLACK_TOKEN = "xoxb-5299649559379-5372256915506-LfjJ3tkaWbvOojjw9LPTEEsF";

    public static void PrintChannels() {
        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods();

        List<Conversation> channels = fetchChannels(methods);
        if (channels != null) {
            String channelsJson = convertToJson(extractChannelData(channels));
            System.out.println("Channels data:\n" + channelsJson);
        }
    }
    
    public static void printUSers() {
    	Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods();
        
        List<User> users = fetchUsers(methods);
        if (users != null) {
            String usersJson = convertToJson(extractUserData(users));
            System.out.println("Users data:\n" + usersJson);
        }
    }

    private static List<Conversation> fetchChannels(MethodsClient methods) {
        try {
            ConversationsListRequest request = ConversationsListRequest.builder()
                    .token(SLACK_TOKEN)
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

    private static List<User> fetchUsers(MethodsClient methods) {
        try {
            UsersListRequest request = UsersListRequest.builder()
                    .token(SLACK_TOKEN)
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

    private static List<ChannelData> extractChannelData(List<Conversation> channels) {
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

    private static List<UserData> extractUserData(List<User> users) {
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

    private static String convertToJson(Object object) {
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
