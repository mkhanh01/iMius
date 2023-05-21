package com.example.imius.repository;

import com.example.imius.api.API;
import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.TopicModel;
import com.example.imius.model.Trending;
import com.example.imius.service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicRepository {
    private DataService dataService;
    public TopicRepository(){
        this.dataService = API.getAccount().create(DataService.class);
    }

    public RefreshLiveData<List<TopicModel>> getTopic() {
        RefreshLiveData<List<TopicModel>> data = new RefreshLiveData<>(callback -> {
            dataService.getTopic().enqueue(new Callback<List<TopicModel>>() {
                @Override
                public void onResponse(Call<List<TopicModel>> call, Response<List<TopicModel>> response) {
                    callback.onDataLoaded((ArrayList<TopicModel>) response.body());
                }

                @Override
                public void onFailure(Call<List<TopicModel>> call, Throwable t) {

                }
            });
        });
        return data;
    }
}
