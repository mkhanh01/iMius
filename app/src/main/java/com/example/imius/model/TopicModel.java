package com.example.imius.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopicModel {
    private int idTopic;
    private String nameTopic;
    private String imageTopic;

    public TopicModel(int idTopic, String nameTopic, String imageTopic) {
        this.idTopic = idTopic;
        this.nameTopic = nameTopic;
        this.imageTopic = imageTopic;
    }

    public TopicModel(String nameTopic, String imageTopic) {
        this.nameTopic = nameTopic;
        this.imageTopic = imageTopic;
    }

    public int getIdTopic() {
        return idTopic;
    }

    public void setIdTopic(int idTopic) {
        this.idTopic = idTopic;
    }

    public String getNameTopic() {
        return nameTopic;
    }

    public void setNameTopic(String nameTopic) {
        this.nameTopic = nameTopic;
    }

    public String getImageTopic() {
        return imageTopic;
    }

    public void setImageTopic(String imageTopic) {
        this.imageTopic = imageTopic;
    }
}
