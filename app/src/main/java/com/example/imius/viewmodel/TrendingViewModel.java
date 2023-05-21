package com.example.imius.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.Trending;
import com.example.imius.repository.TrendingRepository;

import java.util.List;

public class TrendingViewModel extends AndroidViewModel {
    private TrendingRepository repository;
    private RefreshLiveData<List<Trending>> liveData;

    public RefreshLiveData getLiveData() {
        return liveData;
    }

    public TrendingViewModel(@NonNull Application application) {
        super(application);

        repository = new TrendingRepository();
        liveData = repository.getTrending();
    }
    public LiveData<List<Trending>> getTrending() {
        return liveData;
    }
    public void refreshLiveData (){
        liveData.refresh();
    }
}