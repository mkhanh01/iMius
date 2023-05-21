package com.example.imius.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Trending {

    private int idTrending;
    private String nameTrending;
    private String imageTrending;

    public Trending(int idTrending, String nameTrending, String imageTrending) {
        this.idTrending = idTrending;
        this.nameTrending = nameTrending;
        this.imageTrending = imageTrending;
    }

    public Trending(String nameTrending, String imageTrending) {
        this.nameTrending = nameTrending;
        this.imageTrending = imageTrending;
    }

    public int getIdTrending() {
        return idTrending;
    }

    public void setIdTrending(int idTrending) {
        this.idTrending = idTrending;
    }

    public String getNameTrending() {
        return nameTrending;
    }

    public void setNameTrending(String nameTrending) {
        this.nameTrending = nameTrending;
    }

    public String getImageTrending() {
        return imageTrending;
    }

    public void setImageTrending(String imageTrending) {
        this.imageTrending = imageTrending;
    }
}