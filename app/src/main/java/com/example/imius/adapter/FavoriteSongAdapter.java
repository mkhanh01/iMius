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
import androidx.recyclerview.widget.RecyclerView;

import com.example.imius.R;
import com.example.imius.activity.PlayMusicActivity;
import com.example.imius.constants.Constants;
import com.example.imius.data.DataLocalManager;
import com.example.imius.model.BaseResponse;
import com.example.imius.model.FavoriteSong;
import com.example.imius.model.Song;
import com.example.imius.repository.MusicRepository;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteSongAdapter extends RecyclerView.Adapter<FavoriteSongAdapter.ViewHolder>{

    private Context context;
    private List<FavoriteSong> favoriteSongs;

    public FavoriteSongAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_favorite, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteSong favoriteSong = favoriteSongs.get(position);

        if (favoriteSongs == null){
            return;
        }

        holder.setSong(new Song(favoriteSong.getIdSong(), favoriteSong.getNameSong(),
                favoriteSong.getImageSong(), favoriteSong.getNameSinger(), favoriteSong.getLinkSong()));

        holder.tvNameFavoriteSong.setText(favoriteSong.getNameSong());
        holder.tvNameSinger.setText(favoriteSong.getNameSinger());
        Picasso.get().load(favoriteSong.getImageSong()).into(holder.imgFavoriteSong);
    }

    @Override
    public int getItemCount() {
        if (favoriteSongs != null){
            return favoriteSongs.size();
        }
        return 0;
    }

    public List<FavoriteSong> getFavoriteSongs() {
        return favoriteSongs;
    }

    public void setFavoriteSongs(List<FavoriteSong> favoriteSongs) {
        this.favoriteSongs = favoriteSongs;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameFavoriteSong;
        private ImageView imgFavoriteSong;
        private TextView tvNameSinger;

        private Song song;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameFavoriteSong = itemView.findViewById(R.id.item_favorite_tv_name_of_song);
            tvNameSinger = itemView.findViewById(R.id.item_favorite_tv_name_of_singer);
            imgFavoriteSong = itemView.findViewById(R.id.item_favorite_iv_image_of_song);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PlayMusicActivity.class);
                    intent.putExtra("favorite_song", favoriteSongs.get(getPosition()));
                    checkHistorySong(song);
                    context.startActivity(intent);
                }
            });
        }

        private void checkHistorySong(Song song) {
            MusicRepository repository = new MusicRepository();

            repository.checkHistorySong(DataLocalManager.getUsernameData(), song.getIdSong())
                    .enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.body() != null) {
                                if (!response.body().getIsSuccess().equals(Constants.successfully)) {
                                //    Toast.makeText(context, String.valueOf(response.body().equals(Constants.successfully))).show();
                                    addHistorySong(song);
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

        public void addHistorySong(Song song) {
            MusicRepository repository = new MusicRepository();
            repository.insertHistorySong(song).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.body() != null) {
                        //  Toast.makeText(context, R.string.song_insert_success, Toast.LENGTH_LONG).show();
                    } else {
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

        private void setSong(Song song) {
            this.song = new Song(song);

        }
    }

}
