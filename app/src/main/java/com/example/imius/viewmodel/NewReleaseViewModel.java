package com.example.imius.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.NewReleaseModel;
import com.example.imius.repository.NewReleaseRepository;

import java.util.List;

public class NewReleaseViewModel extends AndroidViewModel {
    private NewReleaseRepository repository;
    private RefreshLiveData<List<NewReleaseModel>> liveData;

    public NewReleaseViewModel(@NonNull Application application) {
        super(application);
        repository = new NewReleaseRepository();
        liveData = repository.getNewRelease();
    }
    public LiveData<List<NewReleaseModel>> getNewRelease(){
        return liveData;
    }
    public void refreshLivaData(){
        liveData.refresh();
    }
}
