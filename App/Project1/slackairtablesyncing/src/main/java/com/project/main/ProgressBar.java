package com.project.main;

public class ProgressBar {
	public static void printProgressBar(String title, String comment, int current, int total) {
	    int width = 50;
	    float percent = (float) current / total;
	    int progress = (int) (percent * width);

	    StringBuilder sb = new StringBuilder();
	    sb.append('\r');
	    if (current == total) {
	        sb.append(String.format("%s [%-50s] %d%% done", title, "#".repeat(progress), (int) (percent * 100)));
	    } else {
	        sb.append(String.format("%s [%-50s] %d%% %s", title, "#".repeat(progress), (int) (percent * 100), comment));
	    }
	    System.out.print(sb.toString());
	}
}
