package com.example.imius.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.imius.activity.PlayMusicActivity;
import com.example.imius.constants.Constants;
import com.example.imius.data.DataLocalManager;
import com.example.imius.model.BaseResponse;
import com.example.imius.model.Song;
import com.example.imius.model.SongLibraryPlaylist;
import com.example.imius.repository.MusicRepository;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongLibraryPlaylistAdapter extends RecyclerView.Adapter<SongLibraryPlaylistAdapter.ViewHolder>{

    private Context context;
    private List<SongLibraryPlaylist> songLibraryPlaylistList;

    public SongLibraryPlaylistAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_search, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SongLibraryPlaylist songLibraryPlaylist = songLibraryPlaylistList.get(position);

        if (songLibraryPlaylistList == null){
            return;
        }

        holder.setSong(new Song(songLibraryPlaylist.getIdSong(), songLibraryPlaylist.getNameSong(),
                songLibraryPlaylist.getImageSong(), songLibraryPlaylist.getNameSinger(),
                songLibraryPlaylist.getLinkSong()));

        holder.tvIndex.setText(position + 1 + "");
        holder.tvNameOfSong.setText(songLibraryPlaylist.getNameSong());
        holder.tvSinger.setText(songLibraryPlaylist.getNameSinger());
        Picasso.get().load(songLibraryPlaylist.getImageSong()).into(holder.imgImageOfSong);
        checkLikeSong(holder, songLibraryPlaylist);

        holder.imgLoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnLike(holder, songLibraryPlaylist);
            }
        });
    }


    public void checkLikeSong (ViewHolder holder, SongLibraryPlaylist songLibraryPlaylist){
        MusicRepository repository = new MusicRepository();

        repository.checkLikeSong(DataLocalManager.getUsernameData(), songLibraryPlaylist.getIdSong())
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

    public void setBtnLike (ViewHolder holder, SongLibraryPlaylist songLibraryPlaylist){
        MusicRepository repository = new MusicRepository();

        repository.updateLikeOfNumber(songLibraryPlaylist.getIdSong())
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null){
                            //    Toast.makeText(context, response.body().getIsSuccess(), Toast.LENGTH_LONG).show();
                            if (response.body().getIsSuccess().equals(Constants.successfully)){
                                if (response.body().getMessage().equals(Constants.DELETE)){
                                    deleteLikeSong(songLibraryPlaylist.getIdSong());
                                    holder.imgLoveButton.setImageResource(R.drawable.ic_love);
                                }else {
                                    updateNumberOfLike(songLibraryPlaylist);
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

    public void updateNumberOfLike(SongLibraryPlaylist songLibraryPlaylist) {
        MusicRepository repository = new MusicRepository();
        Song song = new Song(songLibraryPlaylist.getIdSong(), songLibraryPlaylist.getNameSong(),
                songLibraryPlaylist.getImageSong(), songLibraryPlaylist.getNameSinger(),
                songLibraryPlaylist.getLinkSong());

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
        if (songLibraryPlaylistList != null){
            return  songLibraryPlaylistList.size();
        }
        return 0;
    }

    public List<SongLibraryPlaylist> getSongLibraryPlaylistList() {
        return songLibraryPlaylistList;
    }

    public void setSongLibraryPlaylistList(List<SongLibraryPlaylist> songLibraryPlaylistList) {
        this.songLibraryPlaylistList = songLibraryPlaylistList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgImageOfSong;
        private TextView tvNameOfSong;
        private TextView tvIndex, tvSinger;
        public ImageView imgLoveButton;
        public ConstraintLayout constraintLayout;

        private Song song;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
      //      layoutItem = itemView.findViewById(R.id.item_playlist);
            tvIndex = itemView.findViewById(R.id.item_search_tv_index_song);
            imgImageOfSong = itemView.findViewById(R.id.item_search_iv_image_of_song);
            imgLoveButton = itemView.findViewById(R.id.item_search_iv_love);
            tvNameOfSong = itemView.findViewById(R.id.item_search_tv_name_of_song);
            tvSinger = itemView.findViewById(R.id.item_search_tv_name_of_singer);
            constraintLayout = itemView.findViewById(R.id.item_search_constraint_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PlayMusicActivity.class);
                    intent.putExtra("library_song", songLibraryPlaylistList.get(getAdapterPosition()));
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
