package com.example.imius.repository;

import com.example.imius.api.API;
import com.example.imius.data.DataLocalManager;
import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.FavoriteSong;
import com.example.imius.model.HistorySong;
import com.example.imius.service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorySongRepository {
    private DataService dataService;

    public HistorySongRepository() {
        this.dataService = API.getAccount().create(DataService.class);
    }

    public RefreshLiveData<List<HistorySong>> getAllHistorySong(){
        RefreshLiveData<List<HistorySong>> data = new RefreshLiveData<>(callback -> {
            dataService.getHistorySongList(DataLocalManager.getUsernameData()).enqueue(new Callback<List<HistorySong>>() {
                @Override
                public void onResponse(Call<List<HistorySong>> call, Response<List<HistorySong>> response) {
                    callback.onDataLoaded((ArrayList<HistorySong>) response.body());
                }

                @Override
                public void onFailure(Call<List<HistorySong>> call, Throwable t) {
                }
            });
        });
        return data;
    }

}
