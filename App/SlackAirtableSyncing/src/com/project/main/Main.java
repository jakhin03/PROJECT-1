package com.project.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.project.airtableapi.AirTableAPI;
import com.project.slackdatafetching.SlackDataFetching;
import com.project.slackmanagement.CreateChannels;
import com.project.slackmanagement.InviteUsers;

public class Main {

	private static Scanner sc = new Scanner(System.in);
	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

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

	public static void showMenu() throws IOException {
	    String menu = "\nPlease select an option:\n\n" + "1. Show Slack's channels\n"
	            + "2. Show Slack user's information\n" + "3. Create a channels\n"
	            + "4. Invite user to channel\n" + "5. Fetching slack to airtable\n"
	            + "0. Exit\n\n" + "Enter your choice (0-5): ";

	    System.out.print(menu);

	    try {
	        int option = sc.nextInt();
	        switch (option) {
	            case 0:
	                System.out.println("Program ended!");
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
	                showMenuFetching();
	                break;
	            default:
	                System.out.println("Invalid input! Please enter a valid option (0-5).");
	                showMenu();
	                break;
	        }
	    } catch (InputMismatchException e) {
	        System.out.println("Invalid input! Please enter a valid option (0-5).");
	        sc.next();
	        showMenu();
	    }
	}


	public static void showMenuFetching() throws IOException{
		String menu = "\nPlease select an option:\n\n" + "1. Fetch data from Slack to AirTable\n" + "2. Schedule fetching task";
		System.out.println(menu);
		int option = sc.nextInt();
		
		switch (option) {
		case 0:
			showMenu();
			break;
		case 1:
			LocalDateTime submittedTime = LocalDateTime.now();
			autoFetching(submittedTime, "Manual sync");
			System.out.println("Syncing data...");
			System.out.println("Press Enter key to get back...");
			System.in.read();
			showMenu();
			
			break;
		case 2:
			taskScheduling("Schedule sync");
			System.out.println("Press Enter key to get back...");
			System.in.read();
			showMenu();
			break;
		default:
			System.out.println("Invalid input!");
			showMenuFetching();
			break;
		}
	}
	
	public static void taskScheduling(final String task) {
	    @SuppressWarnings("resource")
	    Scanner scanner = new Scanner(System.in);

	    System.out.print("Enter hour (0-23): ");
	    int hour = scanner.nextInt();
	    if (hour > 23 || hour < 0) {
	    	System.out.println("Invalid value for hour!");
	    	taskScheduling("Schedule sync");
	    }

	    System.out.print("Enter minute (0-59): ");
	    int minute = scanner.nextInt();
	    if (minute > 59 || minute < 0) {
	    	System.out.println("Invalid value for minute!");
	    	taskScheduling("Schedule sync");
	    }
	    
	    final LocalDateTime submittedScheduleTime = LocalDateTime.now();
	    
	    System.out.printf("Data will be syncing at %d:%02d\n", hour, minute);

	    

	    scheduler.scheduleAtFixedRate(new Runnable() {
	        @Override
	        public void run() {
	            autoFetching(submittedScheduleTime, task);
	        }
	    }, getDelay(hour, minute), 24L * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
	}

	    private static long getDelay(int hour, int minute) {
	        LocalDateTime now = LocalDateTime.now();
	        LocalDateTime nextRun = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), hour, minute);

	        if (now.compareTo(nextRun) > 0) {
	            nextRun = nextRun.plusDays(1);
	        }

	        return ChronoUnit.MILLIS.between(now, nextRun);
	}
		
	
	public static void autoFetching(final LocalDateTime submittedTime, final String task){
		Thread thread = new Thread(new Runnable() {
             @Override
             public void run() {
            	LocalDateTime startedTime = LocalDateTime.now();
            	String status = "SUCCESS";
            	 try {
            		SlackDataFetching.airtableFetching();
         		}catch (IOException e) {
         			status = "FAILED";
         			System.out.println("Network Error!");
         		}
       			LocalDateTime finishedTime = LocalDateTime.now();
       			AirTableAPI.createLogs(submittedTime, startedTime, finishedTime, status, task);
             }
         });
         thread.start();
	}

	public static void showUsers() throws IOException {
		SlackDataFetching.printUsers();
		System.out.println("Press Enter key to get back...");
		System.in.read();
		showMenu();
	}

	public static void showChannels() throws IOException  {
		SlackDataFetching.printChannels();
		System.out.println("Press Enter key to get back...");
		System.in.read();
		showMenu();
	}

	public static void createChannel() throws IOException {
	    // create channel bang slack API
	    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	    System.out.print("Note: Channel names have a 21-character limit and can include lowercase letters, non-Latin characters, numbers, and hyphens.\nEnter channel's name:\n");
	    String channelName;
	    try {
	        channelName = reader.readLine();
	        if (channelName.trim().isEmpty()) {
	            System.out.println("Channel name cannot be empty.");
	            System.out.println("Press Enter to get back...");
	            System.in.read();
	            showMenu();
	            return;
	        }
	        // Replace whitespace with hyphens
	        channelName = channelName.replaceAll("\\s", "-");

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
	    } catch (IOException e) {
	        System.out.println("Can't create channel!");
	    }

	    System.out.println("Press Enter key to get back...");
	    System.in.read();
	    showMenu();
	}


	public static void inviteUser() throws IOException{
		try {
			InviteUsers.inviteUser();
		}catch (IOException e) {
			System.out.println("Cant invtie user!");
		}
		
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
