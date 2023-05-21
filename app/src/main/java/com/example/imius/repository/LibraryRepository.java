package com.example.imius.repository;

import com.example.imius.api.API;
import com.example.imius.data.DataLocalManager;
import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.BaseResponse;
import com.example.imius.model.FavoriteSong;
import com.example.imius.model.LibraryPlaylist;
import com.example.imius.model.SongLibraryPlaylist;
import com.example.imius.service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class LibraryRepository {
    private DataService dataService;

    public LibraryRepository() {
        this.dataService = API.getAccount().create(DataService.class);
    }

    public RefreshLiveData<List<LibraryPlaylist>> getAllLibraryPlaylist(){
        RefreshLiveData<List<LibraryPlaylist>> data = new RefreshLiveData<>(callback -> {
            dataService.getLibraryPlaylistList(DataLocalManager.getUsernameData()).enqueue(new Callback<List<LibraryPlaylist>>() {
                @Override
                public void onResponse(Call<List<LibraryPlaylist>> call, Response<List<LibraryPlaylist>> response) {
                    callback.onDataLoaded((ArrayList<LibraryPlaylist>) response.body());
                }

                @Override
                public void onFailure(Call<List<LibraryPlaylist>> call, Throwable t) {
                }
            });
        });
        return data;
    }

    public RefreshLiveData<List<FavoriteSong>> getAllFavoriteSong(){
        RefreshLiveData<List<FavoriteSong>> data = new RefreshLiveData<>(callback -> {
            dataService.getFavoriteSong(DataLocalManager.getUsernameData()).enqueue(new Callback<List<FavoriteSong>>() {
                @Override
                public void onResponse(Call<List<FavoriteSong>> call, Response<List<FavoriteSong>> response) {
                    callback.onDataLoaded((ArrayList<FavoriteSong>) response.body());
                }

                @Override
                public void onFailure(Call<List<FavoriteSong>> call, Throwable t) {
                }
            });
        });
        return data;
    }

    public RefreshLiveData<List<SongLibraryPlaylist>> getAllSongLibraryPlaylist(int idLibraryPlaylist){
        RefreshLiveData<List<SongLibraryPlaylist>> data = new RefreshLiveData<>(callback -> {
            dataService.getSongLibraryPlaylistList(idLibraryPlaylist).enqueue(new Callback<List<SongLibraryPlaylist>>() {
                @Override
                public void onResponse(Call<List<SongLibraryPlaylist>> call, Response<List<SongLibraryPlaylist>> response) {
                    callback.onDataLoaded((ArrayList<SongLibraryPlaylist>) response.body());
                }

                @Override
                public void onFailure(Call<List<SongLibraryPlaylist>> call, Throwable t) {
                }
            });
        });
        return data;
    }

    public Call<BaseResponse> insertLibraryPlaylist (String nameLibraryPlaylist){
        return dataService.insertLibraryPlaylist(nameLibraryPlaylist, DataLocalManager.getUsernameData());
    }

    public Call<BaseResponse> deleteLibraryPlaylist (int idLibraryPlaylist){
        return dataService.deleteLibraryPlaylist(idLibraryPlaylist);
    }

    public Call<BaseResponse> updateLibraryPlaylistName (String nameLibraryPlaylist, String newNameLibraryPlaylist){
        return dataService.updateLibraryPlaylistName(nameLibraryPlaylist, newNameLibraryPlaylist, DataLocalManager.getUsernameData());
    }

    public Call<BaseResponse> deleteFavoriteSong (String idSong){
        return dataService.deleteFavoriteSong(idSong, DataLocalManager.getUsernameData());
    }

    public Call<BaseResponse> insertSongLibraryPlaylist (int idLibraryPlaylist, int idSong, String nameSong,
                                                         String nameSinger, String imageSong,
                                                         String linkSong){
        return dataService.insertSongLibraryPlaylist(idLibraryPlaylist, idSong, nameSong, nameSinger,
                imageSong,linkSong);
    }

    public Call<BaseResponse> checkSongLibraryPlaylist (int idLibraryPlaylist, int idSong){
        return dataService.checkSongLibraryPlaylist(idLibraryPlaylist, idSong);
    }


}
