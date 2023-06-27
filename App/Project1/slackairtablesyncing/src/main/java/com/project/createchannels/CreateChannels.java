package com.project.createchannels;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CreateChannels {
	public static void createChannel(String channelName, String description, boolean isPrivate) throws IOException {
		String url = "https://slack.com/api/conversations.create";
		String token = "xoxb-5299649559379-5372256915506-LfjJ3tkaWbvOojjw9LPTEEsF";
		
		StringBuilder requestBodyBuilder = new StringBuilder();
        requestBodyBuilder.append("name=").append(channelName).append("&is_private=" + isPrivate);
        if (description != null) {
            requestBodyBuilder.append("&description=").append(description);
        }
        String requestBody = requestBodyBuilder.toString();
		
		byte[] postData = requestBody.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;
		
		URL apiUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Authorization", "Bearer" + token);
		connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
		
		try (OutputStream outputStream = connection.getOutputStream()) {
			outputStream.write(postData);
		}
		
		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			System.out.println("Channel '" + channelName + "' created successfully.");
		} else {
			System.out.println("Failed to create channel '" + channelName + "'. Response Code: " + responseCode);
			
		}
	}

}
