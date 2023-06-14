package com.project.slackdatafetching;

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
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SlackDataFetching {
    // Slack API credentials
    private static final String SLACK_TOKEN = "xoxb-5299649559379-5372256915506-LfjJ3tkaWbvOojjw9LPTEEsF";

    public static void main(String[] args) {
        Slack slack = Slack.getInstance();
        MethodsClient methods = slack.methods();

        // Fetch channels
        List<Conversation> channels = fetchChannels(methods);
        if (channels != null) {
        	writeChannelsToCSV(channels, "I:/HUST/HUST-2022-2/Project 1/SlackAirtableSync/channels.csv");
        }
        
        // Fetch users
        List<User> users = fetchUsers(methods);
        if (users != null) {
        	writeUsersToCSV(users, "I:/HUST/HUST-2022-2/Project 1/SlackAirtableSync/users.csv");
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

    private static void writeChannelsToCSV(List<Conversation> channels, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
        	// Write CSVV header
            writer.write("Channel Name,Channel ID,Topic,Description,Creator ID,Channel Create Date,Privacy,Members,Status");
            writer.newLine();
            
            // Write channel dât
            for (Conversation channel : channels) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%d,%s",
                        channel.getName(),
                        channel.getId(),
                        getFieldValue(channel.getTopic().getValue()),
                        getFieldValue(channel.getPurpose().getValue()),
                        getFieldValue(channel.getCreator()),
                        formatDate(channel.getCreated()),
                        getFieldValue(channel.isPrivate() ? "Private" : "Public"),
                        channel.getNumOfMembers(),
                        channel.isArchived() ? "Archived" : "Active"));
                writer.newLine();
            }

            System.out.println("Channels data written to " + fileName);
        } catch (IOException e) {
            System.out.println("Error occurred while writing channels data to " + fileName + ": " + e.getMessage());
        }
    }
    
    private static void writeUsersToCSV(List<User> users, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
        	// Write CSV header
            writer.write("User Name,User ID,Email,Display Name,Full Name,Status,Role,User Created Date,Status Change Date");
            writer.newLine();
            
            // Write user data
            for (User user : users) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                		getFieldValue(user.getName()),
                        user.getId(),
                        user.getProfile().getEmail(),
                        user.getProfile().getDisplayName(),
                        user.getProfile().getRealName(),
                        user.getProfile().getStatusText(),
                        user.isOwner() ? "Owner" : "Member",
                        formatDate(user.getUpdated()),
                        formatDate(user.getProfile().getStatusExpiration())));
                writer.newLine();
            }

            System.out.println("Users data written to " + fileName);
        } catch (IOException e) {
            System.out.println("Error occurred while writing users data to " + fileName + ": " + e.getMessage());
        }
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
    
    private static <T> String getFieldValue(T value) {
        return value != null ? value.toString().replaceAll(",", ";") : "N/A";
    }
}
