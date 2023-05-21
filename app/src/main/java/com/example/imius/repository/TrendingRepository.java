package com.example.imius.repository;

import com.example.imius.api.API;
import com.example.imius.data.DataLocalManager;
import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.Trending;
import com.example.imius.service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingRepository {
    private DataService dataService;

    public TrendingRepository(){
        this.dataService = API.getAccount().create(DataService.class);
    }

    public RefreshLiveData<List<Trending>> getTrending() {
        RefreshLiveData<List<Trending>> data = new RefreshLiveData<>(callback -> {
            dataService.getTrending().enqueue(new Callback<List<Trending>>() {
                @Override
                public void onResponse(Call<List<Trending>> call, Response<List<Trending>> response) {
                    callback.onDataLoaded((ArrayList<Trending>) response.body()) ;
                }

                @Override
                public void onFailure(Call<List<Trending>> call, Throwable t) {

                }
            });
        });
        return data;
    }
}
