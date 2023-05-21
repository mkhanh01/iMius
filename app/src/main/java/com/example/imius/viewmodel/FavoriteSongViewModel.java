package com.example.imius.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.FavoriteSong;
import com.example.imius.repository.FavoriteSongRepository;

import java.util.List;

public class FavoriteSongViewModel extends AndroidViewModel {
    private FavoriteSongRepository repository;
    private RefreshLiveData<List<FavoriteSong>> favoriteSongs;

    public FavoriteSongViewModel(@NonNull Application application) {
        super(application);

        repository = new FavoriteSongRepository();
        favoriteSongs = repository.getAllFavoriteSong();
    }

    public LiveData<List<FavoriteSong>> getFavoriteSongs() {
        return favoriteSongs;
    }

    public void refreshLiveData (){
        favoriteSongs.refresh();
    }
}
