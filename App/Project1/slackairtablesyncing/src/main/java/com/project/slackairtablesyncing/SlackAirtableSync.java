package com.project.slackairtablesyncing;

import com.slack.api.Slack;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.methods.response.channels.ChannelsListResponse;
import com.slack.api.model.Channel;
import com.slack.api.model.User;
import dev.fuxing.airtable.*;

import java.util.List;

public class SlackAirtableSync {
    // Slack API credentials
    private static final String SLACK_TOKEN = "xoxb-5299649559379-5372256915506-LfjJ3tkaWbvOojjw9LPTEEsF";

    // Airtable API credentials
    private static final String AIRTABLE_API_KEY = "key5WsiRYRz72hvRP";
    private static final String AIRTABLE_BASE_ID = "appvm6jKRtcKivy9e";
    private static final String AIRTABLE_TABLE_NAME = "Slack";

    public static void main(String[] args) {
        Slack slack = Slack.getInstance();

        // Sync Slack users to Airtable
        syncSlackUsersToAirtable(slack);

        // Sync Slack channels to Airtable
        syncSlackChannelsToAirtable(slack);
    }

    private static void syncSlackUsersToAirtable(Slack slack) {
        UsersListResponse response = slack.methods().usersList(req -> req.token(SLACK_TOKEN));

        if (response.isOk()) {
            List<User> users = response.getMembers();

            Airtable airtable = new Airtable(AIRTABLE_API_KEY);

            for (User user : users) {
                String userId = user.getId();
                String userName = user.getName();

                try {
                    Table table = airtable.base(AIRTABLE_BASE_ID).table(AIRTABLE_TABLE_NAME);
                    table.create(new TextField("User ID", userId), new TextField("Username", userName));
                    System.out.println("Synced user to Airtable: " + userName);
                } catch (AirtableApiException e) {
                    System.out.println("Failed to sync user to Airtable: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Failed to fetch Slack users: " + response.getError());
        }
    }

    private static void syncSlackChannelsToAirtable(Slack slack) {
        ChannelsListResponse response = slack.methods().channelsList(req -> req.token(SLACK_TOKEN));

        if (response.isOk()) {
            List<Channel> channels = response.getChannels();

            Airtable airtable = new Airtable(AIRTABLE_API_KEY);

            for (Channel channel : channels) {
                String channelId = channel.getId();
                String channelName = channel.getName();

                try {
                    Table table = airtable.base(AIRTABLE_BASE_ID).table(AIRTABLE_TABLE_NAME);
                    table.create(new TextField("Channel ID", channelId), new TextField("Channel Name", channelName));
                    System.out.println("Synced channel to Airtable: " + channelName);
                } catch (AirtableApiException e) {
                    System.out.println("Failed to sync channel to Airtable: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Failed to fetch Slack channels: " + response.getError());
        }
    }
}
