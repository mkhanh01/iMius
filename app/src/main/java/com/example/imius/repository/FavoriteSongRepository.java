package com.example.imius.repository;

import com.example.imius.api.API;
import com.example.imius.data.DataLocalManager;
import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.FavoriteSong;
import com.example.imius.service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteSongRepository {
    private DataService dataService;

    public FavoriteSongRepository() {
        this.dataService = API.getAccount().create(DataService.class);
    }

    public RefreshLiveData<List<FavoriteSong>> getAllFavoriteSong(){
        RefreshLiveData<List<FavoriteSong>> data = new RefreshLiveData<>(callback -> {
            dataService.getFavoriteSong(DataLocalManager.getUsernameData()).enqueue(new Callback<List<FavoriteSong>>() {
                @Override
                public void onResponse(Call<List<FavoriteSong>> call, Response<List<FavoriteSong>> response) {
                    callback.onDataLoaded((ArrayList<FavoriteSong>) response.body());
                }

                @Override
                public void onFailure(Call<List<FavoriteSong>> call, Throwable t) {
                }
            });
        });
        return data;
    }


}
