package com.example.imius.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imius.R;

import com.example.imius.adapter.SongAdapter;
import com.example.imius.data.DataLocalManager;
import com.example.imius.adapter.SongLibraryPlaylistAdapter;
import com.example.imius.api.API;
import com.example.imius.constants.Constants;
import com.example.imius.databinding.ActivityPlaylistBinding;
import com.example.imius.fragment.NoInternetDialog;
import com.example.imius.fragment.SearchFragment;
import com.example.imius.model.BaseResponse;

import com.example.imius.model.Song;
import com.example.imius.model.SongLibraryPlaylist;
import com.example.imius.network.AppUtil;
import com.example.imius.service.DataService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistActivity extends AppCompatActivity {

    private ActivityPlaylistBinding binding;
    private SongLibraryPlaylistAdapter songLibraryPlaylistAdapter;
    private SongAdapter songAdapter;

    private DataService dataService = API.getAccount().create(DataService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaylistBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        loadData();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setAdapter();

        initView();
        eventClickFabBtn();

        setContentView(view);
    }

    public void setAdapter() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        binding.activityPlaylistRvPlaylist.setLayoutManager(new LinearLayoutManager(PlaylistActivity.this));
        songLibraryPlaylistAdapter = new SongLibraryPlaylistAdapter(PlaylistActivity.this);
        binding.activityPlaylistRvPlaylist.setAdapter(songLibraryPlaylistAdapter);

        if (bundle.getString("nameLibraryPlaylist") != null) {

            Picasso.get().load(bundle.getString("imgPlaylistLibrary")).into(binding.activityPlaylistIvViewSong);
            binding.activityPlaylistTvSongName.setText(bundle.getString("nameLibraryPlaylist"));

            //   getAllSongLibraryPlaylist();
            dataService.getSongLibraryPlaylistList(bundle.getInt("idLibraryPlaylist")).enqueue(new Callback<List<SongLibraryPlaylist>>() {
                @Override
                public void onResponse(Call<List<SongLibraryPlaylist>> call, Response<List<SongLibraryPlaylist>> response) {
                    if (response.body() != null) {
                        songLibraryPlaylistAdapter.setSongLibraryPlaylistList(response.body());
                    } else {
                        StyleableToast.makeText(PlaylistActivity.this, "null",
                                Toast.LENGTH_LONG, R.style.myToast).show();
                    }
                }

                @Override
                public void onFailure(Call<List<SongLibraryPlaylist>> call, Throwable t) {

                }
            });
            deleteSongLibraryPlaylist(bundle.getInt("idLibraryPlaylist"));


        } else {
            binding.activityPlaylistImAddSong.setVisibility(View.GONE);
            if (bundle.getString("nameSinger") != null) {
                songAdapter = new SongAdapter(PlaylistActivity.this);
                binding.activityPlaylistRvPlaylist.setAdapter(songAdapter);
                // if (bundle.getBoolean("checkTrending", false)) {

                Picasso.get().load(bundle.getString("imageSinger")).into(binding.activityPlaylistIvViewSong);
                binding.activityPlaylistTvSongName.setText(bundle.getString("nameSinger"));
                //   getAllSongLibraryPlaylist();
                dataService.getSongSingerList(DataLocalManager.getIdSinger()).enqueue(new Callback<List<Song>>() {
                    @Override
                    public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                        if (response.body() != null) {
                            songAdapter.setListSongs((ArrayList<Song>) response.body());
                        } else {
                            StyleableToast.makeText(PlaylistActivity.this, "null",
                                    Toast.LENGTH_LONG, R.style.myToast).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Song>> call, Throwable t) {

                    }
                });
            } else if (bundle.getString("nameTrending") != null) {
                songAdapter = new SongAdapter(PlaylistActivity.this);
                binding.activityPlaylistRvPlaylist.setAdapter(songAdapter);
                // if (bundle.getBoolean("checkTrending", false)) {

                Picasso.get().load(bundle.getString("imageTrending")).into(binding.activityPlaylistIvViewSong);
                binding.activityPlaylistTvSongName.setText(bundle.getString("nameTrending"));
                //   getAllSongLibraryPlaylist();
                dataService.getSongTrendingList(DataLocalManager.getIdTrending())
                        .enqueue(new Callback<List<Song>>() {
                            @Override
                            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                                if (response.body() != null) {
                                    songAdapter.setListSongs((ArrayList<Song>) response.body());
                                } else {
                                    StyleableToast.makeText(PlaylistActivity.this, "null",
                                            Toast.LENGTH_LONG, R.style.myToast).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Song>> call, Throwable t) {

                            }
                        });
            } else if (bundle.getString("nameTopic") != null) {
                songAdapter = new SongAdapter(PlaylistActivity.this);
                binding.activityPlaylistRvPlaylist.setAdapter(songAdapter);
                // if (bundle.getBoolean("checkTrending", false)) {

                Picasso.get().load(bundle.getString("imageTopic")).into(binding.activityPlaylistIvViewSong);
                binding.activityPlaylistTvSongName.setText(bundle.getString("nameTopic"));
                //   getAllSongLibraryPlaylist();
                dataService.getSongTopicList(DataLocalManager.getIdTopic())
                        .enqueue(new Callback<List<Song>>() {
                            @Override
                            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                                if (response.body() != null) {
                                    songAdapter.setListSongs((ArrayList<Song>) response.body());
                                } else {
                                    StyleableToast.makeText(PlaylistActivity.this, "null",
                                            Toast.LENGTH_LONG, R.style.myToast).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Song>> call, Throwable t) {

                            }
                        });
            }
        }
    }


    public void callSearchFragment() {
        Bundle bundle = new Bundle();

        String message = "checkAddLibrary";
        bundle.putBoolean(message, true);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);

        binding.activityPlaylistAppbarLayout.setVisibility(View.GONE);
        binding.activityPlaylistCollapsingToolbar.setVisibility(View.GONE);
        binding.activityPlaylistLinearlayout.setVisibility(View.GONE);
        binding.activityPlaylistNested.setVisibility(View.GONE);
        binding.activityPlaylistFabAction.setVisibility(View.GONE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_playlist_coordinator_layout, searchFragment);
        transaction.commit();
    }

    private void initView() {
        binding.activityPlaylistFabAction.setEnabled(false);

        binding.activityPlaylistImAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PlaylistActivity.this, SearchFragment.class);
//
//                intent.putExtra("checkAddLibrary", true);
//
//                startActivity(intent);

                callSearchFragment();

            }
        });

        binding.activityPlaylistImBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getAllSongLibraryPlaylist(int idLibraryPlaylist) {
        dataService.getSongLibraryPlaylistList(idLibraryPlaylist).enqueue(new Callback<List<SongLibraryPlaylist>>() {
            @Override
            public void onResponse(Call<List<SongLibraryPlaylist>> call, Response<List<SongLibraryPlaylist>> response) {
                if (response.body() != null) {
                    songLibraryPlaylistAdapter.setSongLibraryPlaylistList(response.body());
                    eventClickFabBtn();
                } else {
                    StyleableToast.makeText(PlaylistActivity.this, "null",
                            Toast.LENGTH_LONG, R.style.myToast).show();
                }
            }

            @Override
            public void onFailure(Call<List<SongLibraryPlaylist>> call, Throwable t) {

            }
        });
    }

    private void deleteSongLibraryPlaylist(int idLibraryPlaylist) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                List<SongLibraryPlaylist> songLibraryPlaylistList = songLibraryPlaylistAdapter.getSongLibraryPlaylistList();
                SongLibraryPlaylist songLibraryPlaylist = songLibraryPlaylistList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(PlaylistActivity.this);

                builder.setTitle(getString(R.string.title_dialog));
                builder.setMessage(getString(R.string.dialog_delete_message));
                builder.setCancelable(false);

                builder.setPositiveButton(getString(R.string.yes_dialog),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dataService.deleteSongLibraryPlaylist(songLibraryPlaylist.getIdSongLibraryPlaylist())
                                        .enqueue(new Callback<BaseResponse>() {
                                            @Override
                                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                                if (response.body().getIsSuccess().equals(Constants.successfully)) {
                                                    StyleableToast.makeText(PlaylistActivity.this,
                                                            getString(R.string.song_library_playlist_delete_success),
                                                            Toast.LENGTH_LONG, R.style.myToast).show();
                                                    getAllSongLibraryPlaylist(idLibraryPlaylist);
                                                } else {
                                                    StyleableToast.makeText(PlaylistActivity.this,
                                                            getString(R.string.song_library_playlist_delete_failed),
                                                            Toast.LENGTH_LONG, R.style.myToast).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<BaseResponse> call, Throwable t) {

                                            }
                                        });
                            }
                        });

                builder.setNegativeButton(getString(R.string.no_dialog),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getAllSongLibraryPlaylist(idLibraryPlaylist);
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }).attachToRecyclerView(binding.activityPlaylistRvPlaylist);
    }

    private void eventClickFabBtn() {
        binding.activityPlaylistFabAction.setEnabled(true);
        binding.activityPlaylistFabAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaylistActivity.this, PlayMusicActivity.class);
                if (songLibraryPlaylistAdapter.getSongLibraryPlaylistList() != null) {
                    if (songLibraryPlaylistAdapter.getSongLibraryPlaylistList().size() > 0) {
                        intent.putExtra("list_song_library",
                                (ArrayList<SongLibraryPlaylist>) songLibraryPlaylistAdapter.getSongLibraryPlaylistList());
                        startActivity(intent);
                    } else {
                        StyleableToast.makeText(PlaylistActivity.this, "The list has no songs at all.",
                                Toast.LENGTH_LONG, R.style.myToast).show();
                    }

                } else if (songAdapter.getListSongs() != null) {
                    if (songAdapter.getListSongs().size() > 0) {
                        intent.putExtra("list_song",
                                (ArrayList<Song>) songAdapter.getListSongs());
                        startActivity(intent);
                    } else {
                        StyleableToast.makeText(PlaylistActivity.this, "The list has no songs at all.",
                                Toast.LENGTH_LONG, R.style.myToast).show();
                    }
                }

            }
        });
    }

    private void loadData() {
        if (!AppUtil.isNetworkAvailable(this)) {
            DialogFragment dialogFragment = NoInternetDialog.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "NoInternetDialog");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
     //   loadData();
        setAdapter();
    }
}
