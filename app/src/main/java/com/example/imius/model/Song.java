package com.example.imius.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Song implements Parcelable {
    // BAI HAT
    @SerializedName("idSong")
    @Expose
    private int idSong;
    @SerializedName("nameSong")
    @Expose
    private String nameSong;
    @SerializedName("imageSong")
    @Expose
    private String imageSong;
    @SerializedName("nameSinger")
    @Expose
    private String nameSinger;
    @SerializedName("linkSong")
    @Expose
    private String linkSong;


    public Song() {
    }

    public Song(Song song) {
        this.idSong = song.getIdSong();
        this.imageSong = song.getImgSong();
        this.nameSong = song.getNameSong();
        this.nameSinger = song.getNameSinger();
        this.linkSong = song.getLinkSong();
    }

    public Song(int idSong, String nameSong, String imageSong, String nameSinger, String linkSong) {
        this.idSong = idSong;
        this.imageSong = imageSong;
        this.nameSong = nameSong;
        this.nameSinger = nameSinger;
        this.linkSong = linkSong;
    }



    public Song(Parcel in) {
        idSong = in.readInt();
        nameSong = in.readString();
        imageSong = in.readString();
        nameSinger = in.readString();
        linkSong = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int getIdSong() {
        return idSong;
    }

    public void setIdSong(int idSong) {
        this.idSong = idSong;
    }

    public String getNameSinger() {
        return nameSinger;
    }

    public void setNameSinger(String nameSinger) {
        this.nameSinger = nameSinger;
    }

    public String getImgSong() {
        return imageSong;
    }

    public void setImgSong(String imgSong) {
        this.imageSong = imgSong;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getLinkSong() {
        return linkSong;
    }

    public void setLinkSong(String linkSong) {
        this.linkSong = linkSong;
    }

    //Mo ta noi dung
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeInt(idSong);
        parcel.writeString(nameSong);
        parcel.writeString(imageSong);
        parcel.writeString(nameSinger);
        parcel.writeString(linkSong);
    }
}
