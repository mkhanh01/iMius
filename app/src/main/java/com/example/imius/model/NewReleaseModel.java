package com.example.imius.model;

public class NewReleaseModel {
    private int idNewRelease;
    private String nameNewRelease;
    private String imageNewRelease;

    public NewReleaseModel(int idNewRelease, String nameNewRelease, String imageNewRelease) {
        this.idNewRelease = idNewRelease;
        this.nameNewRelease = nameNewRelease;
        this.imageNewRelease = imageNewRelease;
    }

    public NewReleaseModel(String nameNewRelease, String imageNewRelease) {
        this.nameNewRelease = nameNewRelease;
        this.imageNewRelease = imageNewRelease;
    }

    public int getIdNewRelease() {
        return idNewRelease;
    }

    public void setIdNewRelease(int idNewRelease) {
        this.idNewRelease = idNewRelease;
    }

    public String getNameNewRelease() {
        return nameNewRelease;
    }

    public void setNameNewRelease(String nameNewRelease) {
        this.nameNewRelease = nameNewRelease;
    }

    public String getImageNewRelease() {
        return imageNewRelease;
    }

    public void setImageNewRelease(String imageNewRelease) {
        this.imageNewRelease = imageNewRelease;
    }
}
