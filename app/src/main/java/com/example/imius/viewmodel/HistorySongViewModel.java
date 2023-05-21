package com.example.imius.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.FavoriteSong;
import com.example.imius.model.HistorySong;
import com.example.imius.repository.FavoriteSongRepository;
import com.example.imius.repository.HistorySongRepository;

import java.util.List;

public class HistorySongViewModel extends AndroidViewModel {
    private HistorySongRepository repository;
    private RefreshLiveData<List<HistorySong>> historySongs;

    public HistorySongViewModel(@NonNull Application application) {
        super(application);

        repository = new HistorySongRepository();
        historySongs = repository.getAllHistorySong();
    }

    public LiveData<List<HistorySong>> getHistorySongs() {
        return historySongs;
    }

    public void refreshLiveData (){
        historySongs.refresh();
    }
}
