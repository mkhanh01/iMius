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
import com.example.imius.model.HistorySong;
import com.example.imius.model.Song;
import com.example.imius.repository.MusicRepository;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorySongAdapter extends RecyclerView.Adapter<HistorySongAdapter.ViewHolder>{

    private Context context;
    private List<HistorySong> historySongs;

    public HistorySongAdapter(Context context) {
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
        HistorySong historySong = historySongs.get(position);

        if (historySongs == null){
            return;
        }
        holder.setSong(new Song(historySong.getIdSong(), historySong.getNameSong(), historySong.getImageSong(),
                historySong.getNameSinger(), historySong.getLinkSong()));

        holder.tvNameHistorySong.setText(historySong.getNameSong());
        holder.tvNameSinger.setText(historySong.getNameSinger());
        Picasso.get().load(historySong.getImageSong()).into(holder.imgHistorySong);
    }

    @Override
    public int getItemCount() {
        if (historySongs != null){
            return historySongs.size();
        }
        return 0;
    }

    public List<HistorySong> getHistorySongs() {
        return historySongs;
    }

    public void setHistorySongs(List<HistorySong> historySongs) {
        this.historySongs = historySongs;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNameHistorySong;
        private ImageView imgHistorySong;
        private TextView tvNameSinger;
        private Song song;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameHistorySong = itemView.findViewById(R.id.item_favorite_tv_name_of_song);
            tvNameSinger = itemView.findViewById(R.id.item_favorite_tv_name_of_singer);
            imgHistorySong = itemView.findViewById(R.id.item_favorite_iv_image_of_song);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PlayMusicActivity.class);
                    intent.putExtra("history_song", historySongs.get(getPosition()));
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
