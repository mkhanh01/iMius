package com.example.imius.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Theme {
    
    @SerializedName("idTheme")
    @Expose
    private String idTheme;
    @SerializedName("nameTheme")
    @Expose
    private String nameTheme;
    @SerializedName("imgTheme")
    @Expose
    private String imgTheme;

    public String getIdTheme() {
        return idTheme;
    }

    public void setIdTheme(String idTheme) {
        this.idTheme = idTheme;
    }

    public String getNameTheme() {
        return nameTheme;
    }

    public void setNameTheme(String nameTheme) {
        this.nameTheme = nameTheme;
    }

    public String getImgTheme() {
        return imgTheme;
    }

    public void setImgTheme(String imgTheme) {
        this.imgTheme = imgTheme;
    }
}
