package com.example.imius.api;

import com.example.imius.constants.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    private static Retrofit retrofit;

    public static Retrofit getAccount (){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(Constants.baseURL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
