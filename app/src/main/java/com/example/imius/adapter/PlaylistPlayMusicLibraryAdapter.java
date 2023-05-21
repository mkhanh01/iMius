package com.example.imius.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imius.R;
import com.example.imius.activity.PlayMusicActivity;
import com.example.imius.model.SongLibraryPlaylist;

import java.util.ArrayList;

public class PlaylistPlayMusicLibraryAdapter extends RecyclerView.Adapter<PlaylistPlayMusicLibraryAdapter.ViewHolder>{
    Context context;
    ArrayList<SongLibraryPlaylist> songLibraryPlaylistArrayList;

    public PlaylistPlayMusicLibraryAdapter(Context context, ArrayList<SongLibraryPlaylist> songLibraryPlaylistArrayList) {
        this.context = context;
        this.songLibraryPlaylistArrayList = songLibraryPlaylistArrayList;
    }

    @NonNull
    @Override
    public PlaylistPlayMusicLibraryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_play_music, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistPlayMusicLibraryAdapter.ViewHolder holder, int position) {
        SongLibraryPlaylist song = songLibraryPlaylistArrayList.get(position);

        holder.tvNumber.setText(position + 1 + "");
        holder.tvNameSong.setText(song.getNameSong());
        holder.tvNameSinger.setText(song.getNameSinger());

//        holder.relativeLayout.setOnClickListener(view -> {
//            PlayMusicActivity playMusicActivity = new PlayMusicActivity();
//            playMusicActivity.setPosition(position);
//        });

    }

    @Override
    public int getItemCount() {
        return songLibraryPlaylistArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNumber;
        private TextView tvNameSong;
        private TextView tvNameSinger;
        private RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumber = itemView.findViewById(R.id.item_play_music_number);
            tvNameSong = itemView.findViewById(R.id.item_play_music_name_of_song);
            tvNameSinger = itemView.findViewById(R.id.item_play_music_name_of_singer);
            relativeLayout = itemView.findViewById(R.id.item_play_music_relative_layout);
        }
    }
}
