package com.project.main;

import com.project.createchannels.CreateChannels;
import com.project.slackdatafetching.*;
import com.project.inviteusers.*;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		//autoFetching();
		showlogo();
		showMenu();
		sc.close();
	}

	public static void showlogo() {
		String logo = "\n  ___  ___  ___  ___  ___.---------------.\n"
				+ ".'\\__\\'\\__\\'\\__\\'\\__\\'\\__,`   .  ____ ___ \\\n"
				+ "|\\/ __\\/ __\\/ __\\/ __\\/ _:\\   |`.  \\  \\___ \\\n"
				+ " \\\\'\\__\\'\\__\\'\\__\\'\\__\\'\\_`.__|\"\"`. \\  \\___ \\\n"
				+ "  \\\\/ __\\/ __\\/ __\\/ __\\/ __:                \\\n"
				+ "   \\\\'\\__\\'\\__\\'\\__\\ \\__\\'\\_;-----------------`\n"
				+ "    \\\\/   \\/   \\/   \\/   \\/ :               tk|\n"
				+ "     \\|______________________;________________|\n";

		String title = "WELCOME TO SLACK MANAGEMENT PROGRAM!";
		int width = 60;
		String line = "-".repeat(width);
		
		System.out.println(logo);
		System.out.println(line);
		System.out.println(centerString(title, width));
		System.out.println(line);
	}

	public static void showMenu() throws Exception {
		String menu = "\nPlease select an option:\n\n" + "1. Show Slack's channels\n"
				+ "2. Show Slack user's information\n" + "3. Create a channels\n" + "4. Invite user to channel\n" + "5. User management\n"
				+ "0. Exit\n\n" + "Enter your choice (1-5): ";
		System.out.print(menu);
		int option = sc.nextInt();
		
		switch (option) {
		case 0:
			System.out.println("Program ended");
			break;
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
			showMenu();
			break;
		}
	}

	public static void autoFetching() {
		try {
			SlackDataFetching.airtableFetching();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void showUsers() throws Exception {
		SlackDataFetching.printUsers();
		System.out.println("Press Enter key to get back...");
		System.in.read();
		showMenu();
	}

	public static void showChannels() throws Exception {
		SlackDataFetching.printChannels();
		System.out.println("Press Enter key to get back...");
		System.in.read();
		showMenu();
	}

	public static void createChannel() throws Exception {
		// create channel bang slack API
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Note: Channel names have a 21-character limit and can include lowercase letters, non-Latin characters, numbers, and hyphens.\nEnter channel's name:\n");
		String channelName = reader.readLine();
		if (channelName.trim().isEmpty()) {
			System.out.println("Channel name cannot be empty.");
			System.out.println("Press Enter to get back...");
			System.in.read();
			showMenu();
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
    System.out.println("Press Enter key to get back...");
		System.in.read();
		showMenu();
	}

	public static void inviteUser() throws Exception {
		InviteUsers.inviteUser();
		System.out.println("Press Enter key to get back...");
		System.in.read();
		showMenu();
	}

	public static void manageUsers() throws Exception {
		//for debug
		System.out.println("manageUsers");
		System.out.println("Press Enter key to get back...");
		System.in.read();
		showMenu();
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
