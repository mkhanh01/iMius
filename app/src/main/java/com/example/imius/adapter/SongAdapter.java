package com.example.imius.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imius.R;
import com.example.imius.activity.LoginActivity;
import com.example.imius.activity.PlayMusicActivity;
import com.example.imius.activity.PlaylistActivity;
import com.example.imius.constants.Constants;
import com.example.imius.data.DataLocalManager;
import com.example.imius.fragment.SearchFragment;
import com.example.imius.model.BaseResponse;
import com.example.imius.model.ChartsModel;
import com.example.imius.model.Song;
import com.example.imius.repository.LibraryRepository;
import com.example.imius.repository.MusicRepository;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Song> listSongs;

    private SearchFragment searchFragment = new SearchFragment();

    private boolean checkChart = false;
    public SongAdapter(Context context) {
        this.context = context;

    }

    public SongAdapter(Context context, ArrayList<Song> listSongs) {
        this.context = context;
        this.listSongs = listSongs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    private void callPlaylistActivity(Song song){
        DataLocalManager.setIdChart(String.valueOf(song.getIdSong()));
        Intent intent = new Intent(context, PlaylistActivity.class);
        Bundle bundle= new Bundle();

        bundle.putString("nameSong", song.getNameSong());
        bundle.putString("imageSong", song.getImgSong());
        //bundle.putBoolean("checkChart", true);
        intent.putExtras(bundle);

        context.startActivity(intent);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = listSongs.get(position);

        holder.setSong(song);

        holder.tvIndex.setText(position + 1 + "");

        holder.tvNameOfSong.setText(song.getNameSong());
        holder.tvSinger.setText(song.getNameSinger());
        Picasso.get().load(song.getImgSong()).into(holder.imgImageOfSong);

        if (!DataLocalManager.getCheckLogin()){
            holder.imgLoveButton.setImageResource(R.drawable.ic_love);
            holder.imgLoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
            });
        }else {
            if (DataLocalManager.getCheckSearch()) {
                holder.imgLoveButton.setImageResource(R.drawable.ic_add_circle);
                holder.imgLoveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkSongLibraryPlaylist(song);
                    }
                });
            } else {
                checkLikeSong(holder, song);
                holder.imgLoveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setBtnLike(holder, song);
                    }
                });
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPlaylistActivity(song);
            }
        });
    }

    public void checkSongLibraryPlaylist (Song song){
        LibraryRepository repository = new LibraryRepository();

        repository.checkSongLibraryPlaylist(DataLocalManager.getIdLibraryPlaylist(), song.getIdSong())
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null){
                            if (!response.body().getIsSuccess().equals(Constants.successfully)){
                                //      Toast.makeText(context, String.valueOf(response.body().equals(Constants.successfully)))
                                addSongLibraryPlaylist(song);
                            }
                            else {
                                StyleableToast.makeText(context, context.getString(R.string.song_exist),
                                        Toast.LENGTH_LONG, R.style.myToast).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        StyleableToast.makeText(context, t.getMessage(),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                    }
                });
    }

    public void addSongLibraryPlaylist(Song song) {
        LibraryRepository repository = new LibraryRepository();

        repository.insertSongLibraryPlaylist(DataLocalManager.getIdLibraryPlaylist(), song.getIdSong(),
                song.getNameSong(), song.getNameSinger(), song.getImgSong(),
                song.getLinkSong()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body() != null) {
                    StyleableToast.makeText(context, context.getString(R.string.song_insert_success),
                            Toast.LENGTH_LONG, R.style.myToast).show();
                }else {
                    StyleableToast.makeText(context, context.getString(R.string.song_insert_failed),
                            Toast.LENGTH_LONG, R.style.myToast).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                StyleableToast.makeText(context, t.getMessage(),
                        Toast.LENGTH_LONG, R.style.myToast).show();
            }
        });
    }
    public void checkLikeSong (ViewHolder holder,Song song){
        MusicRepository repository = new MusicRepository();

        repository.checkLikeSong(DataLocalManager.getUsernameData(), song.getIdSong())
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null){
                            //    Toast.makeText(context, response.body().getIsSuccess(), Toast.LENGTH_LONG).show();
                            if (response.body().getIsSuccess().equals(Constants.successfully)){
                                holder.imgLoveButton.setImageResource(R.drawable.ic_loved);
                            }else {
                                holder.imgLoveButton.setImageResource(R.drawable.ic_love);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        StyleableToast.makeText(context, t.getMessage(),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                    }
                });
    }

    public void setBtnLike (ViewHolder holder, Song song){
        MusicRepository repository = new MusicRepository();

        repository.updateLikeOfNumber(song.getIdSong())
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null){
                            //    Toast.makeText(context, response.body().getIsSuccess(), Toast.LENGTH_LONG).show();
                            if (response.body().getIsSuccess().equals(Constants.successfully)){
                                if (response.body().getMessage().equals(Constants.DELETE)){
                                    deleteLikeSong(song.getIdSong());
                                    holder.imgLoveButton.setImageResource(R.drawable.ic_love);
                                }else {
                                    updateNumberOfLike(song);
                                    holder.imgLoveButton.setImageResource(R.drawable.ic_loved);
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        StyleableToast.makeText(context, t.getMessage(),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                    }
                });
    }

    public void updateNumberOfLike(Song song) {
        MusicRepository repository = new MusicRepository();

        //     repository.insertLoveSong(DataLocalManager.getUsernameData(), song);
        repository.insertLoveSong(DataLocalManager.getUsernameData(), song).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
//                if (response.body().getIsSuccess().equals(Constants.successfully)){
//                    Toast.makeText(context, "insert success", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(context, "insert failed", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                StyleableToast.makeText(context, t.getMessage(),
                        Toast.LENGTH_LONG, R.style.myToast).show();
            }
        });
    }

    public void deleteLikeSong(int idSong) {
        MusicRepository repository = new MusicRepository();
        //       repository.deleteLikeSong(DataLocalManager.getUsernameData(), idSong);

        repository.deleteLikeSong(DataLocalManager.getUsernameData(), idSong).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
//                if (response.body().getIsSuccess().equals(Constants.successfully)){
//                    Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(context, "delete failed", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                StyleableToast.makeText(context, t.getMessage(),
                        Toast.LENGTH_LONG, R.style.myToast).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        if (listSongs != null) {
            return listSongs.size();
        }
        return 0;
    }

    public void setCheckChart(boolean checkChart) {
        this.checkChart = checkChart;
    }

    public ArrayList<Song> getListSongs() {
        return listSongs;
    }

    public void setListSongs(ArrayList<Song> listSongs) {
        this.listSongs = listSongs;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgImageOfSong;
        private TextView tvNameOfSong;
        private TextView tvIndex, tvSinger;
        public ImageView imgLoveButton;
        public ConstraintLayout constraintLayout;
        private Song song;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIndex = itemView.findViewById(R.id.item_search_tv_index_song);
            imgImageOfSong = itemView.findViewById(R.id.item_search_iv_image_of_song);
            imgLoveButton = itemView.findViewById(R.id.item_search_iv_love);
            tvNameOfSong = itemView.findViewById(R.id.item_search_tv_name_of_song);
            tvSinger = itemView.findViewById(R.id.item_search_tv_name_of_singer);
            constraintLayout = itemView.findViewById(R.id.item_search_constraint_layout);

//            imgLoveButton.setVisibility(View.GONE);

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PlayMusicActivity.class);
                    intent.putExtra("song", listSongs.get(getPosition()));
                    checkHistorySong(song);
                    context.startActivity(intent);
                }
            });
        }

        private void checkHistorySong (Song song){
            MusicRepository repository = new MusicRepository();

            repository.checkHistorySong(DataLocalManager.getUsernameData(), song.getIdSong())
                    .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.body() != null){
                                if (!response.body().getIsSuccess().equals(Constants.successfully)){
                                    //      Toast.makeText(context, String.valueOf(response.body().equals(Constants.successfully)))
                                    addSongLibraryPlaylist(song);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            StyleableToast.makeText(context, t.getMessage(),
                                    Toast.LENGTH_LONG, R.style.myToast).show();
                        }
                    });
        }

        public void addSongLibraryPlaylist(Song song) {
            MusicRepository repository = new MusicRepository();
            repository.insertHistorySong(song).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.body() != null) {
                        //  Toast.makeText(context, R.string.song_insert_success, Toast.LENGTH_LONG).show();
                    }else {
                        //  Toast.makeText(context, R.string.song_insert_failed, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    StyleableToast.makeText(context, t.getMessage(),
                            Toast.LENGTH_LONG, R.style.myToast).show();
                }
            });
        }

        public void setSong (Song song){
            this.song = new Song(song);
        }

    }
}
