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
import com.example.imius.livedata.RefreshLiveData;
import com.example.imius.model.BaseResponse;
import com.example.imius.model.NewReleaseModel;
import com.example.imius.model.Song;
import com.example.imius.repository.MusicRepository;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewReleaseAdapter extends RecyclerView.Adapter<NewReleaseAdapter.ViewHolder> {
    private Context context;
    private List<NewReleaseModel> newReleaseModelList;
    private ArrayList<Song> listSongs = new ArrayList<Song>();

    public NewReleaseAdapter(Context context){
        this.context = context;
    }

    public NewReleaseAdapter(Context context, ArrayList<NewReleaseModel> newReleaseModelList) {
        this.context = context;
        this.newReleaseModelList = newReleaseModelList;
    }

    public List<NewReleaseModel> getNewReleaseModelList() {
        return newReleaseModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_new_release, parent, false);

        return new ViewHolder(view);
    }

    public void setNewReleaseModelList(List<NewReleaseModel> newReleaseModelList) {
        this.newReleaseModelList = newReleaseModelList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull NewReleaseAdapter.ViewHolder holder, int position) {
        NewReleaseModel newReleaseModel = newReleaseModelList.get(position);

        MusicRepository repository = new MusicRepository();

        repository.getSongNewReleaseList(String.valueOf(newReleaseModel.getIdNewRelease()))
                .enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                holder.setSong(response.body());
                listSongs.add(response.body());
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {

            }
        });
     //   holder.setSong(new Song(newReleaseModel.));

        if (newReleaseModelList == null){
            return;
        }
        holder.tvNameNewRelease.setText(newReleaseModel.getNameNewRelease());
        Picasso.get().load(newReleaseModel.getImageNewRelease()).into(holder.imgNewRelease);
    }

    @Override
    public int getItemCount() {
        return newReleaseModelList != null ? newReleaseModelList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgNewRelease;
        private TextView tvNameNewRelease;

        private Song song;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgNewRelease = itemView.findViewById(R.id.item_newrelease_iv_newrelease_image);
            tvNameNewRelease = itemView.findViewById(R.id.item_newrelease_tv_newrelease_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayMusicActivity.class);

                    checkHistorySong(song);
                    intent.putExtra("song", listSongs.get(getPosition()));
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
