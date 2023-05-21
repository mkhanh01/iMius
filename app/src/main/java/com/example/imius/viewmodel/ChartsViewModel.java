package com.example.imius.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.ChartsModel;
import com.example.imius.repository.ChartsRepository;

import java.util.List;

public class ChartsViewModel extends AndroidViewModel {
    private ChartsRepository repository;
    private RefreshLiveData<List<ChartsModel>> liveData;
    public RefreshLiveData getLiveData(){
        return liveData;
    }
    public ChartsViewModel(@NonNull Application application) {
        super(application);
        repository = new ChartsRepository();
        liveData = repository.getCharts();
    }
    public LiveData<List<ChartsModel>> getCharts(){
        return liveData;
    }
    public  void refreshLiveData(){
        liveData.refresh();
    }
}
