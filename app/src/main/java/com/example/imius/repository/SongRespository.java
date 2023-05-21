package com.example.imius.repository;

import com.example.imius.api.API;
import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.Song;
import com.example.imius.service.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRespository {
    private DataService dataService;
    public SongRespository(){
        this.dataService = API.getAccount().create(DataService.class);

    }
    public RefreshLiveData<List<Song>> getSongChart(){
        RefreshLiveData<List<Song>> data = new RefreshLiveData<>(callback -> {
           dataService.getSongCharts().enqueue(new Callback<List<Song>>() {
               @Override
               public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                   callback.onDataLoaded(response.body());
               }

               @Override
               public void onFailure(Call<List<Song>> call, Throwable t) {

               }
           });
        });
        return data;
    }
}
