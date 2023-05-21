package com.example.imius.repository;

import com.example.imius.api.API;
import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.NewReleaseModel;
import com.example.imius.service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewReleaseRepository {
    private DataService dataService;
    public NewReleaseRepository(){
        this.dataService = API.getAccount().create(DataService.class);
    }

    public RefreshLiveData<List<NewReleaseModel>> getNewRelease(){
        RefreshLiveData<List<NewReleaseModel>> data = new RefreshLiveData<>(callback -> {
           dataService.getNewRelease().enqueue(new Callback<List<NewReleaseModel>>() {
               @Override
               public void onResponse(Call<List<NewReleaseModel>> call, Response<List<NewReleaseModel>> response) {
                   callback.onDataLoaded((ArrayList<NewReleaseModel>) response.body());
               }

               @Override
               public void onFailure(Call<List<NewReleaseModel>> call, Throwable t) {

               }
           });
        });
        return data;
    }
}
