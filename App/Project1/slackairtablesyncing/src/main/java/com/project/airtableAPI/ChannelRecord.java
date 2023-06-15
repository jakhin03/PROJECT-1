package com.project.airtableAPI;

public class ChannelRecord {
	private String createdTime;
	private Channel channel;
	private User user;
	private String id;

	public ChannelRecord(String createdTime, Channel channel, String id) {
		super();
		this.createdTime = createdTime;
		this.channel = channel;
		this.id = id;
	}

	public ChannelRecord(String createdTime, User user, String id) {
		super();
		this.createdTime = createdTime;
		this.user = user;
		this.id = id;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public Channel getField() {
		return channel;
	}

	public void setField(Channel channel) {
		this.channel = channel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Record [createdTime=" + createdTime + ", field=" + channel + ", id=" + id + ", getCreatedTime()="
				+ getCreatedTime() + ", getField()=" + getField() + ", getId()=" + getId() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
