package com.project.secrets;

import java.util.Arrays;
import java.util.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Secrets {
	// Credentials
	private static final String CONFIG_FILE_PATH = "Credentials/config.properties";
	
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
        	System.out.println("Authentication failed. Please check configuration file!");
        }
    }
	
	//Getters
  
	public static String getSlackBotToken() {
		return properties.getProperty("slack.bottoken");
	}

	public static String getSlackSigning() {
		return properties.getProperty("slack.signingsecret");
	}

	public static String getAPIKey() {
		return properties.getProperty("api.key");
	}

	public static String getBaseID() {
		return properties.getProperty("base.id");
	}

	public static String getTableUsersID() {
		return properties.getProperty("table.usersid");
	}

	public static String getTableChannelsID() {
		return properties.getProperty("table.channelsid");
	}
	public static List<String> getBotUserIDs() {
	    String botIDsString = properties.getProperty("slack.botids");
	    return Arrays.asList(botIDsString.split(","));
	    }
	public static String getTableLogsId() {
		return properties.getProperty("table.logsid");
	}
  
}
