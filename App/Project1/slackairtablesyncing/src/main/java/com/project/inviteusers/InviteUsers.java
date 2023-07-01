package com.project.inviteusers;

import com.project.secrets.Secrets;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsInviteRequest;
import com.slack.api.methods.request.conversations.ConversationsListRequest;
import com.slack.api.methods.request.users.UsersLookupByEmailRequest;
import com.slack.api.methods.response.conversations.ConversationsInviteResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InviteUsers {

    public static void inviteUser() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String channelName = null;
        List<String> userEmails = new ArrayList<>();

        try {
            // Read the channel name
            System.out.print("Enter the channel name: ");
            channelName = reader.readLine();

            // Read the list of user emails
            System.out.print("Enter the list of user emails (comma-separated): ");
            String userEmailsInput = reader.readLine();
            String[] userEmailsArray = userEmailsInput.split(",");
            for (String userEmail : userEmailsArray) {
                userEmails.add(userEmail.trim());
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please provide valid channel name and user emails.");
            return;
        }

        String token = Secrets.getSlackBotToken();
        Slack slack = Slack.getInstance();

        // Find channel by name
        String channelId = null;
        try {
            ConversationsListResponse conversationsListResponse = slack.methods().conversationsList(
                    ConversationsListRequest.builder().token(token).build());
            if (conversationsListResponse.isOk()) {
                List<Conversation> channels = conversationsListResponse.getChannels();
                for (Conversation channel : channels) {
                    if (channel.getName().equals(channelName)) {
                        channelId = channel.getId();
                        break;
                    }
                }
            } else {
                System.out.println("Failed to fetch conversations: " + conversationsListResponse.getError());
                return;
            }
        } catch (IOException | SlackApiException e) {
            System.out.println("Error occurred while fetching conversations: " + e.getMessage());
            return;
        }

        if (channelId == null) {
            System.out.println("Channel with name '" + channelName + "' not found.");
            return;
        }

        // Get user IDs by email
        List<String> userIds = new ArrayList<>();
        for (String userEmail : userEmails) {
            UsersLookupByEmailRequest usersLookupByEmailRequest = UsersLookupByEmailRequest.builder()
                    .token(token)
                    .email(userEmail)
                    .build();
            try {
                UsersLookupByEmailResponse usersLookupByEmailResponse = slack.methods().usersLookupByEmail(usersLookupByEmailRequest);
                if (usersLookupByEmailResponse.isOk()) {
                    User user = usersLookupByEmailResponse.getUser();
                    userIds.add(user.getId());
                } else {
                    System.out.println("Failed to lookup user with email '" + userEmail + "'. Error: " + usersLookupByEmailResponse.getError());
                }
            } catch (IOException | SlackApiException e) {
                System.out.println("Failed to lookup user with email '" + userEmail + "'. Error: " + e.getMessage());
            }
        }

        // Invite users to the channel
        ConversationsInviteRequest inviteRequest = ConversationsInviteRequest.builder()
                .token(token)
                .channel(channelId)
                .users(userIds)
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
