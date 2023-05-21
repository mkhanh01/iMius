package com.example.imius.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavoriteSong implements Parcelable {
    @SerializedName("idLike")
    @Expose
    private int idLike;

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

    public FavoriteSong(int idFavorite, String username, int idSong, String nameSong,
                        String imageSong, String nameSinger, String linkSong){
        this.idLike = idFavorite;
        this.username = username;
        this.idSong = idSong;
        this.nameSong = nameSong;
        this.imageSong = imageSong;
        this.nameSinger = nameSinger;
        this.linkSong = linkSong;
    }

    public int getIdLike() {
        return idLike;
    }

    public void setIdLike(int idLike) {
        this.idLike = idLike;
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

    protected FavoriteSong(Parcel in){
        idLike = in.readInt();
        username = in.readString();
        idSong = in.readInt();
        nameSong = in.readString();
        imageSong = in.readString();
        nameSinger = in.readString();
        linkSong = in.readString();
    }

    public static final Creator<FavoriteSong> CREATOR = new Creator<FavoriteSong>() {
        @Override
        public FavoriteSong createFromParcel(Parcel in) {
            return new FavoriteSong(in);
        }

        @Override
        public FavoriteSong[] newArray(int size) {
            return new FavoriteSong[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(idLike);
        parcel.writeString(username);
        parcel.writeInt(idSong);
        parcel.writeString(nameSong);
        parcel.writeString(imageSong);
        parcel.writeString(nameSinger);
        parcel.writeString(linkSong);
    }



}