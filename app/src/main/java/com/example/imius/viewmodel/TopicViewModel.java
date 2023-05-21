package com.example.imius.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.TopicModel;
import com.example.imius.repository.TopicRepository;

import java.util.List;

public class TopicViewModel extends AndroidViewModel {
    private TopicRepository repository;
    private RefreshLiveData<List<TopicModel>> liveData;
    public RefreshLiveData getLiveData(){
        return liveData;
    }
    public TopicViewModel(@NonNull Application application){
        super(application);
         repository = new TopicRepository();
         liveData = repository.getTopic();
    }

    public LiveData<List<TopicModel>> getTopic(){
        return liveData;
    }
    public void refreshLiveData(){
        liveData.refresh();
    }
}
