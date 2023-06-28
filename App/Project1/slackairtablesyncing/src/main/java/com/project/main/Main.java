package com.project.main;

import com.project.createchannels.CreateChannels;
import com.project.slackdatafetching.*;
import com.project.inviteusers.*;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
	public static void main(String[] args) throws Exception {
		autoFetching();
		String logo = "\n  ___  ___  ___  ___  ___.---------------.\n"
                + ".'\\__\\'\\__\\'\\__\\'\\__\\'\\__,`   .  ____ ___ \\\n"
                + "|\\/ __\\/ __\\/ __\\/ __\\/ _:\\   |`.  \\  \\___ \\\n"
                + " \\\\'\\__\\'\\__\\'\\__\\'\\__\\'\\_`.__|\"\"`. \\  \\___ \\\n"
                + "  \\\\/ __\\/ __\\/ __\\/ __\\/ __:                \\\n"
                + "   \\\\'\\__\\'\\__\\'\\__\\ \\__\\'\\_;-----------------`\n"
	    		+ "    \\\\/   \\/   \\/   \\/   \\/ :               tk|\n"
	    		+ "     \\|______________________;________________|\n";
	    String menu = "\nPlease select an option:\n\n"
                + "1. Show Slack's channels\n"
                + "2. Show Slack user's information\n"
                + "3. Create a channels\n"
                + "4. Invite user to channel\n"
                + "5. User management\n"
                + "0. Exit\n\n"
                + "Enter your choice (1-5):\n";

	    System.out.println(logo);
	    String title = "WELCOME TO SLACK MANAGEMENT PROGRAM!";
	    int width = 60;
	    String line = "-".repeat(width);
	    System.out.println(line);
	    System.out.println(centerString(title, width));
	    System.out.println(line);
	    System.out.print(menu);
	    
		Scanner sc = new Scanner(System.in);
		int buff = 0;
		int option = sc.nextInt();
		while (option != 0) {
			buff++;
			switch (option) {
			case 1:
				showChannels();
				break;
			case 2:
				showUsers();
				break;
			case 3:
				createChannel();
				break;
			case 4:
				inviteUser();
				break;
			case 5:
				manageUsers();
				break;
			default:
				System.out.println("Invalid input!");
				break;
			}
			if (buff >= 20) {
				System.out.println("Maximum requests!");
				break;
			}
			
			System.out.print(menu);
			
			if (sc.hasNextInt()) {
				option = sc.nextInt();
			} else {
				System.out.println("No input available.");
			}

		}
		System.out.println("Program ended");
		sc.close();
	}
	
	public static void autoFetching() {
		try {
			SlackDataFetching.airtableFetching();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void showUsers() throws IOException {
		SlackDataFetching.printUsers();
	}

	public static void showChannels() throws IOException {
		SlackDataFetching.printChannels();
	}

	public static void createChannel() throws Exception {
		// create channel bang slack API
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Note: Channel names have a 21-character limit and can include lowercase letters, non-Latin characters, numbers, and hyphens.\nEnter channel's name:\n");
		String channelName = reader.readLine();
		if (channelName.trim().isEmpty()) {
			System.out.println("Channel name cannot be empty.");
			return;
		}
		
		System.out.print("Enter '0' for private channel or '1' for public channel:\n");
		String channelType = reader.readLine();
		boolean isPrivate = false;

		while (!channelType.equals("0") && !channelType.equals("1")) {
		    System.out.println("Invalid input! Please enter '0' for private channel or '1' for public channel:");
		    channelType = reader.readLine();
		}

		isPrivate = channelType.equals("0");
		
		System.out.print("Enter channel' description (optional):\n");
		String description = reader.readLine();
		CreateChannels.createChannel(channelName, description, isPrivate);
		
	}

	public static void inviteUser() throws Exception {
		//for debug
		System.out.println("inviteUser");
		InviteUsers.inviteUser();
	}

	public static void manageUsers() {
		//for debug
		System.out.println("manageUsers");
	}
	
	public static String centerString(String text, int width) {
	    if (text.length() > width) {
	        return text.substring(0, width);
	    } else {
	        int padding = width - text.length();
	        int leftPadding = padding / 2;
	        int rightPadding = padding - leftPadding;
	        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
	    }
	    }


}
