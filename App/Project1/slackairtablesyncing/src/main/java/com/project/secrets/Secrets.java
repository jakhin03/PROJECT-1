package com.project.secrets;

import java.util.Arrays;
import java.util.List;

public class Secrets {
	// Credentials
	private static final String SLACK_BOT_TOKEN = "xoxb-5299649559379-5372256915506-LfjJ3tkaWbvOojjw9LPTEEsF";
	private static final String SLACK_SIGNING_SECRET = "2186633bb97c9c62aeaaadcd3a7b610a";
	private static final String API_KEY = "patAI3AeWkid1KoUX.76729d81c87bb327102a6667ce1fb87b0db5e26fa1436553ec56d8dfc0831c25";
	private static final String BASE_ID = "appNRtbTlWVuJ1lRQ";
	private static final String TABLE_USERS_ID = "tblgH8hhOc3S6o3T5";
	private static final String TABLE_CHANNELS_ID = "tbl5zr74HLsR1phRV";
	private static final String TABLE_LOGS_ID = "tblDtEEIA0xg7K7W3";
	private static final String SLACK_BOT_ID_1 = "USLACKBOT";
	private static final String SLACK_BOT_ID_2 = "U058RB6J40M";
	private static final String SLACK_BOT_ID_3 = "U05AY7JSXEW";

	// Getters
	public static String getSlackBotToken() {
		return SLACK_BOT_TOKEN;
	}

	public static String getSlackSigning() {
		return SLACK_SIGNING_SECRET;
	}

	public static String getAPIKey() {
		return API_KEY;
	}

	public static String getBaseID() {
		return BASE_ID;
	}

	public static String getTableUsersID() {
		return TABLE_USERS_ID;
	}

	public static String getTableChannelsID() {
		return TABLE_CHANNELS_ID;
	}

	public static List<String> getBotUserIDs() {
		return Arrays.asList(SLACK_BOT_ID_1, SLACK_BOT_ID_2, SLACK_BOT_ID_3);
	}

	public static String getTableLogsId() {
		return TABLE_LOGS_ID;
	}
}
