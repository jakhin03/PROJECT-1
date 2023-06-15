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

		// Sync Slack users to Airtable
//        syncSlackUsersToAirtable(slack);

		// Sync Slack channels to Airtable
//        syncSlackChannelsToAirtable(slack);
	}
}
