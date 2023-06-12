package com.project.slackdatafetching;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.channels.ChannelsListRequest;
import com.slack.api.methods.request.users.UsersInfoRequest;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.channels.ChannelsListResponse;
import com.slack.api.methods.response.users.UsersInfoResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Channel;
import com.slack.api.model.User;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SlackDataFetching {
    // Slack API credentials
    private static final String SLACK_TOKEN = "YOUR_SLACK_TOKEN";

    public static void main(String[] args) {
        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods();

        // Fetch channels
        List<Channel> channels = fetchChannels(methods);
        if (channels != null) {
            System.out.println("Channels:");
            for (Channel channel : channels) {
                System.out.println("Channel Name: " + channel.getName());
                System.out.println("Channel ID: " + channel.getId());
                System.out.println("Topic: " + channel.getTopic().getValue());
                System.out.println("Description: " + channel.getPurpose().getValue());
                System.out.println("Creator ID: " + channel.getCreator());
                System.out.println("Channel Create Date: " + formatDate(channel.getCreated()));
                System.out.println("Privacy: " + (channel.isPrivateChannel() ? "Private" : "Public"));
                System.out.println("Members: " + channel.getMembers().size());
                System.out.println("Status: " + (channel.isArchived() ? "Archived" : "Active"));
                System.out.println("---------------------");
            }
        }

        // Fetch users
        List<User> users = fetchUsers(methods);
        if (users != null) {
            System.out.println("Users:");
            for (User user : users) {
                System.out.println("User Name: " + user.getName());
                System.out.println("User ID: " + user.getId());
                System.out.println("Email: " + user.getProfile().getEmail());
                System.out.println("Display Name: " + user.getProfile().getDisplayName());
                System.out.println("Full Name: " + user.getProfile().getRealName());
                System.out.println("Status: " + user.getProfile().getStatusText());
                System.out.println("Role: " + (user.isOwner() ? "Owner" : "Member"));
                System.out.println("User Created Date: " + fetchUserCreatedDate(methods, user.getId()));
                System.out.println("Status Change Date: " + formatDate(user.getProfile().getStatusExpiration()));
                System.out.println("---------------------");
            }
        }
    }

    private static List<Channel> fetchChannels(MethodsClient methods) {
        try {
            ChannelsListRequest request = ChannelsListRequest.builder()
                    .token(SLACK_TOKEN)
                    .build();
            ChannelsListResponse response = methods.channelsList(request);
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

    private static String fetchUserCreatedDate(MethodsClient methods, String userId) {
        try {
            UsersInfoRequest request = UsersInfoRequest.builder()
                    .token(SLACK_TOKEN)
                    .user(userId)
                    .build();
            UsersInfoResponse response = methods.usersInfo(request);
            if (response.isOk()) {
                User user = response.getUser();
                return formatDate(user.getUpdated());
            } else {
                System.out.println("Failed to fetch user info: " + response.getError());
            }
        } catch (IOException | SlackApiException e) {
            System.out.println("Error occurred while fetching user info: " + e.getMessage());
        }
        return "N/A";
    }

    private static String formatDate(long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        return sdf.format(date);
    }
}