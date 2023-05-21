package com.example.imius.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Popularity implements Serializable {
    @SerializedName("idPopularity")
    @Expose
    private String idPopularity;
    @SerializedName("namePopularity")
    @Expose
    private String namePopularity;
    @SerializedName("imgPopularity")
    @Expose
    private String imgPopularity;

    public String getIdPopularity() {
        return idPopularity;
    }

    public void setIdPopularity(String idPopularity) {
        this.idPopularity = idPopularity;
    }

    public String getNamePopularity() {
        return namePopularity;
    }

    public void setNamePopularity(String namePopularity) {
        this.namePopularity = namePopularity;
    }

    public String getImgPopularity() {
        return imgPopularity;
    }

    public void setImgPopularity(String imgPopularity) {
        this.imgPopularity = imgPopularity;
    }
}
