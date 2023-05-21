package com.example.imius.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Playlist implements Serializable {
    @SerializedName("idPlaylist")
    @Expose
    private String idPlaylist;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("imgPlaylist")
    @Expose
    private String imgPlaylist;

    public String getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(String idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgPlaylist(String imgPlaylist) {
        this.imgPlaylist = imgPlaylist;
    }

    public String getImgPlaylist() {
        return imgPlaylist;
    }
}
