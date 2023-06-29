package com.project.inviteusers;

import com.project.secrets.Secrets;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsInviteRequest;
import com.slack.api.methods.response.conversations.ConversationsInviteResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InviteUsers {

    public static void inviteUser() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String channelID = null;
        List<String> userIDs = new ArrayList<>();

        try {
            // Read the channel ID
            System.out.print("Enter the channel's ID: ");
            channelID = reader.readLine();

            // Read the list of user IDs
            System.out.print("Enter the list of user IDs (comma-separated): ");
            String userIDsInput = reader.readLine();
            String[] userIDsArray = userIDsInput.split(",");
            for (String userID : userIDsArray) {
                userIDs.add(userID.trim());
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please provide valid channel ID and user IDs.");
            return;
        }

        String token = Secrets.getSlackBotToken();
        Slack slack = Slack.getInstance();

        ConversationsInviteRequest inviteRequest = ConversationsInviteRequest.builder()
                .token(token)
                .channel(channelID)
                .users(userIDs)
                .build();

        try {
            ConversationsInviteResponse inviteResponse = slack.methods().conversationsInvite(inviteRequest);
            if (inviteResponse.isOk()) {
                System.out.println("Users invited successfully to the channel.");
            } else {
                System.out.println("Failed to invite users to the channel. Error: " + inviteResponse.getError());
            }
        } catch (IOException | SlackApiException e) {
            System.out.println("Failed to invite users to the channel. Error: " + e.getMessage());
        }
    }
}