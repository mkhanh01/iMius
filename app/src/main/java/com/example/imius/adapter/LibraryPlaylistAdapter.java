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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imius.R;
import com.example.imius.activity.PlaylistActivity;
import com.example.imius.api.API;
import com.example.imius.data.DataLocalManager;
import com.example.imius.model.LibraryPlaylist;
import com.example.imius.model.SongLibraryPlaylist;
import com.example.imius.service.DataService;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryPlaylistAdapter extends RecyclerView.Adapter<LibraryPlaylistAdapter.ViewHolder> {

    private Context context;
    private List<LibraryPlaylist> playlistLibraryList;

    public LibraryPlaylistAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.item_library_playlist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LibraryPlaylist playlistLibrary = playlistLibraryList.get(position);

        if (playlistLibraryList == null){
            return;
        }
        holder.tvNamePlaylistLibrary.setText(playlistLibrary.getNameLibraryPlaylist());
    //    Picasso.get().load(playlistLibrary.getImageLibraryPlaylist()).into(holder.imgPlaylistLibrary);
               DataService dataService = API.getAccount().create(DataService.class);

        dataService.getSongLibraryPlaylistList(playlistLibrary.getIdLibraryPlaylist()).enqueue(new Callback<List<SongLibraryPlaylist>>() {
            @Override
            public void onResponse(Call<List<SongLibraryPlaylist>> call, Response<List<SongLibraryPlaylist>> response) {
                if (response.body() != null){
                    if (response.body().size() > 0) {
                        Picasso.get().load(response.body().get(response.body().size() -1).getImageSong()).into(holder.imgPlaylistLibrary);
                        playlistLibrary.setImageLibraryPlaylist(response.body().get(response.body().size() -1).getImageSong());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<SongLibraryPlaylist>> call, Throwable t) {

            }
        });

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPlaylistActivity(playlistLibrary);

            }
        });
    }

    private void callPlaylistActivity (LibraryPlaylist libraryPlaylist){
        DataLocalManager.setIdLibraryPlaylist(libraryPlaylist.getIdLibraryPlaylist());

        Intent intent = new Intent(context, PlaylistActivity.class);

        Bundle bundle =  new Bundle();
        bundle.putInt("idLibraryPlaylist", libraryPlaylist.getIdLibraryPlaylist());
        bundle.putString("nameLibraryPlaylist", libraryPlaylist.getNameLibraryPlaylist());
        bundle.putString("imgPlaylistLibrary", libraryPlaylist.getImageLibraryPlaylist());

        intent.putExtras(bundle);

        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (playlistLibraryList != null){
            return playlistLibraryList.size();
        }
        return 0;
    }

    public List<LibraryPlaylist> getPlaylistLibraryList (){
        return playlistLibraryList;
    }

    public void setPlaylistLibraryList(List<LibraryPlaylist> playlistLibraryList) {
        this.playlistLibraryList = playlistLibraryList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView layoutItem;
        private TextView tvNamePlaylistLibrary;
        private ImageView imgPlaylistLibrary;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.item_library_playlist);
            tvNamePlaylistLibrary = itemView.findViewById(R.id.item_library_playlist_tv_name);
            imgPlaylistLibrary = itemView.findViewById(R.id.item_library_playlist_img);
        }
    }
}
