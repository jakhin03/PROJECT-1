package com.project.slackdatafetching;

public class ChannelData {
    private String name;
    private String id;
    private String topic;
    private String description;
    private String creator;
    private String createDate;
    private String privacy;
    private String status;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
