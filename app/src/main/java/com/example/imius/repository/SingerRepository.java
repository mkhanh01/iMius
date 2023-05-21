package com.example.imius.repository;

import com.example.imius.api.API;
import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.Singer;
import com.example.imius.service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SingerRepository {
    private DataService dataService;
    public SingerRepository(){

        this.dataService = API.getAccount().create(DataService.class);
    }

    public RefreshLiveData<List<Singer>> getSinger(){
        RefreshLiveData<List<Singer>> data = new RefreshLiveData<>(callback -> {
            dataService.getSinger().enqueue(new Callback<List<Singer>>() {
                @Override
                public void onResponse(Call<List<Singer>> call, Response<List<Singer>> response) {
                    callback.onDataLoaded((ArrayList<Singer>) response.body());
                }

                @Override
                public void onFailure(Call<List<Singer>> call, Throwable t) {

                }
            });
        });
        return data;
    }
}
