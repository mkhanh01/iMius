package com.example.imius.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imius.R;
import com.example.imius.model.HistorySong;

import java.util.ArrayList;

public class PlaylistPlayMusicHistoryAdapter extends RecyclerView.Adapter<PlaylistPlayMusicHistoryAdapter.ViewHolder>{
    Context context;
    ArrayList<HistorySong> historySongArrayList;

    public PlaylistPlayMusicHistoryAdapter(Context context, ArrayList<HistorySong> historySongArrayList) {
        this.context = context;
        this.historySongArrayList = historySongArrayList;
    }

    @NonNull
    @Override
    public PlaylistPlayMusicHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_play_music, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistPlayMusicHistoryAdapter.ViewHolder holder, int position) {
        HistorySong song = historySongArrayList.get(position);

        holder.tvNumber.setText(position + 1 + "");
        holder.tvNameSong.setText(song.getNameSong());
        holder.tvNameSinger.setText(song.getNameSinger());
    }

    @Override
    public int getItemCount() {
        return historySongArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNumber;
        private TextView tvNameSong;
        private TextView tvNameSinger;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumber = itemView.findViewById(R.id.item_play_music_number);
            tvNameSong = itemView.findViewById(R.id.item_play_music_name_of_song);
            tvNameSinger = itemView.findViewById(R.id.item_play_music_name_of_singer);
        }
    }
}
