package com.example.imius.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BaseResponse {
    // DAP TRA

    private String isSuccess;
    private String message;
    private User user;

    public String getIsSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public User getUser(){
        return user;
    }
}
