package com.example.imius.model;

public class Singer {
    private int idSinger;
    private String nameSinger;
    private String imageSinger;

    public Singer(int idSinger, String nameSinger, String imageSinger) {
        this.idSinger = idSinger;
        this.nameSinger = nameSinger;
        this.imageSinger = imageSinger;
    }

    public Singer(String nameSinger, String imageSinger) {
        this.nameSinger = nameSinger;
        this.imageSinger = imageSinger;
    }

    public int getIdSinger() {
        return idSinger;
    }

    public void setIdSinger(int idSinger) {
        this.idSinger = idSinger;
    }

    public String getNameSinger() {
        return nameSinger;
    }

    public void setNameSinger(String nameSinger) {
        this.nameSinger = nameSinger;
    }

    public String getImageSinger() {
        return imageSinger;
    }

    public void setImageSinger(String imageSinger) {
        this.imageSinger = imageSinger;
    }
}
