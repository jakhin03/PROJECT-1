 package com.project.slackmanagement;

import com.project.secrets.Secrets;
import com.project.main.*;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsInviteRequest;
import com.slack.api.methods.request.conversations.ConversationsListRequest;
import com.slack.api.methods.request.users.UsersLookupByEmailRequest;
import com.slack.api.methods.response.conversations.ConversationsInviteResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.ConversationType;
import com.slack.api.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InviteUsers {

    private static final String BOT_TOKEN = Secrets.getSlackBotToken();
    private static final String USER_TOKEN = Secrets.getSlackUserToken();

    private InviteUsers() {
        throw new IllegalStateException("Utility class");
    }
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void inviteUser() throws IOException, SlackApiException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String channelName = null;
        List<String> userEmails = new ArrayList<>();

        try {
            // Read the channel name
            System.out.print("Enter the channel name: ");
            channelName = reader.readLine().trim();

	        if (channelName.isEmpty() || channelName.length() > 21 || !Main.isValidChannelName(channelName)) {
	            System.out.println("Invalid channel name! Please enter a valid name.");
	            inviteUser();
	            return;
	        }

            // Read the list of user e-mails
            System.out.print("Enter the list of user emails (comma-separated): ");
            String userEmailsInput = reader.readLine();
            String[] userEmailsArray = userEmailsInput.split("\\s*,\\s*");
            for (String userEmail : userEmailsArray) {
            	if (!isValidEmail(userEmail)) {
            		System.out.println(userEmail+ " is an invalid email! Please enter valid emails.");
            		continue;
            	} else {
            		userEmails.add(userEmail.trim());
            	}
            } 
            // Check if list of e-mails is empty
            if (userEmails.isEmpty()) {
            	return;
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please provide a valid channel name and user emails.");
            return;
        }

        Slack slack = Slack.getInstance();

        // Find channel by name
        String channelId = getChannelId(channelName, slack, BOT_TOKEN, USER_TOKEN);

        if (channelId == null) {
            System.out.println("Channel with name '" + channelName + "' not found.");
            return;
        }

        // Get user IDs by email
        List<String> userIds = getUserIdsByEmails(userEmails, slack);

        if (userIds.isEmpty()) {
            System.out.println("No valid user IDs found for the provided email addresses.");
            return;
        }


        boolean userInviteSuccess = inviteUsersWithUserToken(slack, channelId, userIds);
        if (!userInviteSuccess) {
            boolean botInviteSuccess = inviteUsersWithBotToken(slack, channelId, userIds);
            if (!botInviteSuccess) {
                System.out.println("Failed to invite users to the channel using both user and bot tokens.");
            }
        }
    }

    private static boolean inviteUsersWithUserToken(Slack slack, String channelId, List<String> userIds) throws SlackApiException {
        try {
            ConversationsInviteRequest userTokenInviteRequest = ConversationsInviteRequest.builder()
                    .token(USER_TOKEN)
                    .channel(channelId)
                    .users(userIds)
                    .build();

            ConversationsInviteResponse inviteResponse = slack.methods(USER_TOKEN).conversationsInvite(userTokenInviteRequest);

            if (inviteResponse.isOk()) {
                System.out.println("Users invited successfully to the channel using the user token.");
                return true;
            } else {
                System.out.println("Failed to invite users to the channel using the user token. Error: " + inviteResponse.getError());
            }
        } catch (IOException e) {
            System.out.println("Error occurred while inviting users to the channel using the user token: " + e.getMessage());
        }
        return false;
    }

    private static boolean inviteUsersWithBotToken(Slack slack, String channelId, List<String> userIds) throws SlackApiException {
        try {
            ConversationsInviteRequest botTokenInviteRequest = ConversationsInviteRequest.builder()
                    .token(BOT_TOKEN)
                    .channel(channelId)
                    .users(userIds)
                    .build();

            ConversationsInviteResponse inviteResponse = slack.methods(BOT_TOKEN).conversationsInvite(botTokenInviteRequest);

            if (inviteResponse.isOk()) {
                System.out.println("Users invited successfully to the channel using the bot token.");
                return true;
            } else {
                System.out.println("Failed to invite users to the channel using the bot token. Error: " + inviteResponse.getError());
            }
        } catch (IOException e) {
            System.out.println("Error occurred while inviting users to the channel using the bot token: " + e.getMessage());
        }
        return false;
    }

    private static String getChannelId(String channelName, Slack slack, String botToken, String userToken) {
        try {
            // First, try to fetch channels using bot token
            ConversationsListRequest botTokenRequest = ConversationsListRequest.builder()
                    .excludeArchived(true)
                    .types(Arrays.asList(ConversationType.PUBLIC_CHANNEL, ConversationType.PRIVATE_CHANNEL))
                    .token(botToken)
                    .build();

            ConversationsListResponse botTokenResponse = slack.methods(botToken).conversationsList(botTokenRequest);

            if (botTokenResponse.isOk()) {
                List<Conversation> channels = botTokenResponse.getChannels();
                for (Conversation channel : channels) {
                    if (channel.getName().equals(channelName)) {
                        return channel.getId();
                    }
                }
            } else {
                System.out.println("Error occurred while fetching channels using bot token: " + botTokenResponse.getError());
            }

            // If not found using the bot token, try to fetch using the user token
            ConversationsListRequest userTokenRequest = ConversationsListRequest.builder()
                    .excludeArchived(true)
                    .types(Arrays.asList(ConversationType.PUBLIC_CHANNEL, ConversationType.PRIVATE_CHANNEL))
                    .token(userToken)
                    .build();

            ConversationsListResponse userTokenResponse = slack.methods(userToken).conversationsList(userTokenRequest);

            if (userTokenResponse.isOk()) {
                List<Conversation> channels = userTokenResponse.getChannels();
                for (Conversation channel : channels) {
                    if (channel.getName().equals(channelName)) {
                        return channel.getId();
                    }
                }
            } else {
                System.out.println("Error occurred while fetching channels using user token: " + userTokenResponse.getError());
            }
        } catch (IOException | SlackApiException e) {
            System.out.println("Error occurred while fetching conversations: " + e.getMessage());
        }

        return null;
    }




    private static List<String> getUserIdsByEmails(List<String> userEmails, Slack slack) {
        List<String> userIds = new ArrayList<>();
        for (String userEmail : userEmails) {
            UsersLookupByEmailRequest usersLookupByEmailRequest = UsersLookupByEmailRequest.builder()
                    .token(BOT_TOKEN)
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
        return userIds;

    }

}
