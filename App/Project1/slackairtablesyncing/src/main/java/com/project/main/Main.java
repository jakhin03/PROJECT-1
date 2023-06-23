package com.project.main;

import com.project.slackdatafetching.*;

import java.io.IOException;
import java.util.*;

public class Main {
	public static void main(String[] args) throws IOException {
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
                + "4. User management\n"
                + "0. Exit\n\n"
                + "Enter your choice (1-5): ";

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
			option = sc.nextInt();

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

	public static void createChannel() {
		//for debug
		System.out.println("createChanenl");
		// create chanenl bang slack API

		// Them option them user sau khi da tao channel
		addUser();
	}

	public static void addUser() {
		//for debug
		System.out.println("addUser");
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
