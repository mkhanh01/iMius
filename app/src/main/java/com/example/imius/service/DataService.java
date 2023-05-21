package com.example.imius.service;

import com.example.imius.model.BaseResponse;
import com.example.imius.model.ChartsModel;
import com.example.imius.model.FavoriteSong;
import com.example.imius.model.HistorySong;
import com.example.imius.model.LibraryPlaylist;
import com.example.imius.model.NewReleaseModel;
import com.example.imius.model.Singer;
import com.example.imius.model.Song;
import com.example.imius.model.SongLibraryPlaylist;
import com.example.imius.model.TopicModel;
import com.example.imius.model.Trending;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DataService {

    @GET("getLibraryPlaylistList.php")
    Call<List<LibraryPlaylist>> getLibraryPlaylistList(@Query("username") String username);

    @GET("getFavoriteSong.php")
    Call<List<FavoriteSong>> getFavoriteSong(@Query("username") String username);

    @GET("getSongLibraryPlaylistList.php")
    Call<List<SongLibraryPlaylist>> getSongLibraryPlaylistList (@Query("idLibraryPlaylist") int idLibraryPlaylist);

    @POST("getTrendingList.php")
    Call<List<Trending>> getTrending();

    @POST("getTopicList.php")
    Call<List<TopicModel>> getTopic();

    @GET("getSingerList.php")
    Call<List<Singer>> getSinger();

    @POST("getNewReleaseList.php")
    Call<List<NewReleaseModel>> getNewRelease();

    @POST("getSongChart.php")
    Call<List<ChartsModel>> getCharts();
    @POST("getSongChart.php")
    Call<List<Song>> getSongCharts();

    @GET("insertLibraryPlaylist.php")
    Call<BaseResponse> insertLibraryPlaylist (@Query("nameLibraryPlaylist") String nameLibraryPlaylist,
                                              @Query("username") String username);

    @GET("insertSongLibraryPlaylist.php")
    Call<BaseResponse> insertSongLibraryPlaylist (@Query("idLibraryPlaylist") int idLibraryPlaylist,
                                                  @Query("idSong") int idSong, @Query("nameSong") String nameSong,
                                                  @Query("nameSinger") String nameSinger, @Query("imageSong") String imageSong,
                                                  @Query("linkSong") String linkSong);

    @GET("deleteLibraryPlaylist.php")
    Call<BaseResponse> deleteLibraryPlaylist (@Query("idLibraryPlaylist") int idLibraryPlaylist);

    @GET("deleteSongLibraryPlaylist.php")
    Call<BaseResponse> deleteAllSongLibraryPlaylist (@Query("idLibraryPlaylist") int idLibraryPlaylist);

    @GET("deleteSongLibraryPlaylist.php")
    Call<BaseResponse> deleteSongLibraryPlaylist (@Query("idSongLibraryPlaylist") int idSongLibraryPlaylist);

    @GET("updateLibraryPlaylistName.php")
    Call<BaseResponse> updateLibraryPlaylistName (@Query("nameLibraryPlaylist") String nameLibraryPlaylist,
                                                  @Query("newNameLibraryPlaylist") String newNameLibraryPlaylist,
                                                  @Query("username") String username);

    @GET("deleteLikeSong.php")
    Call<BaseResponse> deleteFavoriteSong (@Query("idSong") String idSong, @Query("username") String username);
    @GET("findSong.php")
    Call<List<Song>> findSong(@Query("key") String key);

    @POST("checkLikeSong.php")
    Call<BaseResponse> checkLikeSong(@Query("username") String username, @Query("idSong") int idSong);

    @GET("checkSongLibraryPlaylist.php")
    Call<BaseResponse> checkSongLibraryPlaylist(@Query("idLibraryPlaylist") int idLibraryPlaylist, @Query("idSong") int idSong);

    @GET("deleteLikeSong.php")
    Call<BaseResponse> deleteLikeSong(@Query("username") String username, @Query("idSong")int idSong);

    @GET("insertLikeSong.php")
    Call<BaseResponse> insertLoveSong(@Query("username") String username,
                                      @Query("idSong")int idSong, @Query("nameSong") String nameSong,
                                      @Query("nameSinger") String nameSinger, @Query("imageSong") String imageSong,
                                      @Query("linkSong") String linkSong);


    @GET("addNumberOfLike.php")
    Call<BaseResponse> addNumberOfLike(@Query("idSong") int idSong, @Query("numberOfLike") int numberOfLike);

    @GET("subNumberOfLike.php")
    Call<BaseResponse> subNumberOfLike(@Query("idSong") int idSong);

    @GET("updateLikeOfNumber.php")
    Call<BaseResponse> updateLikeOfNumber(@Query("username") String username, @Query("idSong") int idSong);

    @GET("checkHistorySong.php")
    Call<BaseResponse> checkHistorySong (@Query("username") String username, @Query("idSong") int idSong);

    @GET("deleteHistorySong.php")
    Call<BaseResponse> deleteHistorySong (@Query("username") String username, @Query("idSong") int idSong);

    @GET("getHistorySongList.php")
    Call<List<HistorySong>> getHistorySongList (@Query("username") String username);

    @GET("insertHistorySong.php")
    Call<BaseResponse> insertHistorySong (@Query("username") String username, @Query("idSong") int idSong,
                                          @Query("nameSong") String nameSong, @Query("nameSinger") String nameSinger,
                                          @Query("imageSong") String imageSong, @Query("linkSong") String  linkSong);

    @GET("getSongList.php")
    Call<List<Song>> getSongSingerList(@Query("idSinger") String idSinger);

    @GET("getSongList.php")
    Call<List<Song>> getSongTrendingList(@Query("idTrending") String idTrending);

    @GET("getSongList.php")
    Call<List<Song>> getSongTopicList(@Query("idTopic") String idTopic);

    @GET("getSongNewRelease.php")
    Call<Song> getSongNewReleaseList(@Query("idNewRelease") String idNewRelease);

    @GET("getSongList.php")
    Call<List<Song>> getSongChartList(@Query("idChart") String idChart);

}
