package com.example.imius.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HistorySong implements Parcelable {
    @SerializedName("idHistory")
    @Expose
    private int idHistory;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("idSong")
    @Expose
    private int idSong;

    @SerializedName("nameSong")
    @Expose
    private String nameSong;

    @SerializedName("nameSinger")
    @Expose
    private String nameSinger;

    @SerializedName("imageSong")
    @Expose
    private String imageSong;

    @SerializedName("linkSong")
    @Expose
    private String linkSong;

    public HistorySong(HistorySong historySong){
        this.idHistory = historySong.getIdHistory();
        this.username = historySong.username;
        this.idSong = historySong.idSong;
        this.nameSong = historySong.getNameSong();
        this.imageSong = historySong.getImageSong();
        this.nameSinger = historySong.nameSinger;
        this.linkSong = historySong.getLinkSong();
    }

    public HistorySong(int idHistory, String username, int idSong, String nameSong,
                       String imageSong, String nameSinger, String linkSong){
        this.idHistory = idHistory;
        this.username = username;
        this.idSong = idSong;
        this.nameSong = nameSong;
        this.imageSong = imageSong;
        this.nameSinger = nameSinger;
        this.linkSong = linkSong;
    }



    public int getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(int idHistory) {
        this.idHistory = idHistory;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIdSong() {
        return idSong;
    }

    public void setIdSong(int idSong) {
        this.idSong = idSong;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getImageSong() {
        return imageSong;
    }

    public void setImgSong(String imgSong) {
        this.imageSong = imgSong;
    }

    public String getNameSinger() {
        return nameSinger;
    }

    public void setNameSinger(String nameSinger) {
        this.nameSinger = nameSinger;
    }

    public String getLinkSong() {
        return linkSong;
    }

    public void setLinkSong(String linkSong) {
        this.linkSong = linkSong;
    }

    protected HistorySong(Parcel in){
        idHistory = in.readInt();
        username = in.readString();
        idSong = in.readInt();
        nameSong = in.readString();
        imageSong = in.readString();
        nameSinger = in.readString();
        linkSong = in.readString();
    }

    public static final Creator<HistorySong> CREATOR = new Creator<HistorySong>() {
        @Override
        public HistorySong createFromParcel(Parcel in) {
            return new HistorySong(in);
        }

        @Override
        public HistorySong[] newArray(int size) {
            return new HistorySong[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(idHistory);
        parcel.writeString(username);
        parcel.writeInt(idSong);
        parcel.writeString(nameSong);
        parcel.writeString(imageSong);
        parcel.writeString(nameSinger);
        parcel.writeString(linkSong);
    }



}