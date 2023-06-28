package com.project.createchannels;

import com.project.secrets.Secrets;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.conversations.ConversationsCreateRequest;
import com.slack.api.methods.request.conversations.ConversationsSetPurposeRequest;
import com.slack.api.methods.response.conversations.ConversationsCreateResponse;

import java.io.IOException;

public class CreateChannels {

    public static void createChannel(String channelName, String description, boolean isPrivate) {
         
    	String token = Secrets.getSlackBotToken();
        Slack slack = Slack.getInstance();

        ConversationsCreateRequest createRequest = ConversationsCreateRequest.builder()
                .token(token)
                .name(channelName)
                .isPrivate(isPrivate)
                .build();

        try {
            ConversationsCreateResponse createResponse = slack.methods().conversationsCreate(createRequest);
            if (createResponse.isOk()) {
                String channelId = createResponse.getChannel().getId();
                System.out.println("Channel created successfully. Channel ID: " + channelId);

                // Set the channel purpose (description)
                slack.methods().conversationsSetPurpose(
                        ConversationsSetPurposeRequest.builder()
                                .token(token)
                                .channel(channelId)
                                .purpose(description)
                                .build()
                );

                System.out.println("Channel purpose set successfully.");
            } else {
                System.out.println("Failed to create channel. Error: " + createResponse.getError());
            }
        } catch (IOException | SlackApiException e) {
            e.printStackTrace();
        }
    }
}
