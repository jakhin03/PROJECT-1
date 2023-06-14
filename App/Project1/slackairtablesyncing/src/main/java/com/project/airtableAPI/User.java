package com.project.airtableAPI;
import java.util.*;

public class User {
	private String userId;
	private String email;
	private String userName;
	private Boolean isActive;
	private String role;
	private String userCreatedDate;
	private String statusChangeDate;
	private ArrayList<Channel> channels;
	
    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUserCreatedDate() {
		return userCreatedDate;
	}

	public void setUserCreatedDate(String userCreatedDate) {
		this.userCreatedDate = userCreatedDate;
	}

	public String getStatusChangeDate() {
		return statusChangeDate;
	}

	public void setStatusChangeDate(String statusChangeDate) {
		this.statusChangeDate = statusChangeDate;
	}

	public ArrayList<Channel> getChannels() {
		return channels;
	}

	public void setChannels(ArrayList<Channel> channels) {
		this.channels = channels;
	}
	
	public void addChannel(Channel channel) {
        this.channels.add(channel);
    }
    
	public void removeChannel(ArrayList<Channel> channel) {
		this.channels.remove(channel);
	}
	@Override
	public String toString() {
		return "User [name=" + userName + "]";
	}
	
	
}
