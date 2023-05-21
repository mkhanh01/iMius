package com.example.imius.repository;

import com.example.imius.api.API;
import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.ChartsModel;
import com.example.imius.service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartsRepository {
    private DataService dataService;
    public ChartsRepository(){
        this.dataService = API.getAccount().create(DataService.class);
    }
    public RefreshLiveData<List<ChartsModel>> getCharts(){
        RefreshLiveData<List<ChartsModel>> data = new RefreshLiveData<>(callback -> {
            dataService.getCharts().enqueue(new Callback<List<ChartsModel>>() {
                @Override
                public void onResponse(Call<List<ChartsModel>> call, Response<List<ChartsModel>> response) {
                    callback.onDataLoaded((ArrayList<ChartsModel>) response.body());
                }

                @Override
                public void onFailure(Call<List<ChartsModel>> call, Throwable t) {

                }
            });
        });
        return data;
    }
}
