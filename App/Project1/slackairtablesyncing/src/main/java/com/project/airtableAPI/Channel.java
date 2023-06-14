package com.project.airtableAPI;
import java.util.*;

public class Channel {
	private String channelId;
	private String name;
	private String topic;
	private String description;
	private String creatorId;
	private String channelCreateDate;
	private Boolean isPublic;
	private Integer numberOfMember;
	private Boolean isActive;
	private ArrayList<User> users;
	
	public Channel(String channelId, String name, String topic, String description, String creatorId,
			String channelCreateDate, Boolean isPublic, Integer numberOfMember, Boolean isActive,
			ArrayList<User> users) {
		super();
		this.channelId = channelId;
		this.name = name;
		this.topic = topic;
		this.description = description;
		this.creatorId = creatorId;
		this.channelCreateDate = channelCreateDate;
		this.isPublic = isPublic;
		this.numberOfMember = numberOfMember;
		this.isActive = isActive;
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Integer getNumberOfMember() {
		return numberOfMember;
	}

	public void setNumberOfMember(Integer numberOfMember) {
		this.numberOfMember = numberOfMember;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public ArrayList<User> getUsers() {
		return this.users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public String getChannelCreateDate() {
		return channelCreateDate;
	}
	
    public void addUser(User user) {
        this.users.add(user);
    }
    
    public void removeUser(User user) {
    	this.users.remove(user);
    }
    
	@Override
	public String toString() {
		return "Channel [name=" + name + ", topic=" + topic + "]";
	}


}
