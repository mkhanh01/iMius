package com.example.imius.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.palette.graphics.Palette;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.imius.constants.Constants;
import com.example.imius.data.DataLocalManager;
import com.example.imius.databinding.ActivityPlayMusicBinding;

import com.example.imius.R;
import com.example.imius.fragment.LibraryPlaylistFragment;
import com.example.imius.fragment.MusicDiscFragment;
import com.example.imius.fragment.NoInternetDialog;
import com.example.imius.fragment.PlayMusicPlaylistFragment;
import com.example.imius.model.BaseResponse;
import com.example.imius.model.FavoriteSong;
import com.example.imius.model.HistorySong;
import com.example.imius.model.Song;
import com.example.imius.model.SongLibraryPlaylist;
import com.example.imius.model.User;
import com.example.imius.network.AppUtil;
import com.example.imius.repository.MusicRepository;
import com.example.imius.viewmodel.SongViewModel;
import com.example.imius.widget.DiscViewPager;
import com.example.imius.widget.ForegroundServiceControl;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class PlayMusicActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityPlayMusicBinding binding;
    private SongViewModel viewModel;

    private int index = 0,  duration = 0, timeValue = 0, durationToService = 0;
    public int position = 0;
    private boolean repeat = false;
    private boolean random = false;
    private boolean checkRandom = false;
    private boolean isPlaying;

    private MusicDiscFragment musicDiscFragment;
    private static DiscViewPager discViewPager;

    private PlayMusicPlaylistFragment playMusicPlaylistFragment;

    public void setPosition(int position) {
        this.position = position;
    }

    public static ArrayList<Song> songArrayList = new ArrayList<>();
    public static ArrayList<SongLibraryPlaylist> songLibraryPlaylistArrayList = new ArrayList<>();
    public static ArrayList<FavoriteSong> favoriteSongArrayList = new ArrayList<>();
    public static ArrayList<HistorySong> historySongArrayList = new ArrayList<>();

    private Song song = new Song();

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                isPlaying = intent.getBooleanExtra(getResources().getString(R.string.status_player), false );
                int action = intent.getIntExtra(getResources().getString(R.string.action_music), 0);
                duration = intent.getIntExtra(getResources().getString(R.string.duration_music), 0);
                timeValue = intent.getIntExtra(getResources().getString(R.string.seek_to_music), 0);
                position = intent.getIntExtra(getResources().getString(R.string.position_music), 0);
                binding.activityPlayMusicSbSongTime.setProgress(timeValue);

                @SuppressLint("SimpleDateFormat")SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

                binding.activityPlayMusicTvRuntime.setText(simpleDateFormat.format(timeValue));

                handleMusic(action);
                timeSong();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlayMusicBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        viewModel = new ViewModelProvider(this).get(SongViewModel.class);

        loadData();

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(getResources().getString(R.string.send_data_to_activity)));

        getDataFromIntent();
        setViewStart();
        startService();
        init();

        overridePendingTransition(R.anim.anim_intent_in, R.anim.anim_intent_out);

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();

        songArrayList.clear();
        songLibraryPlaylistArrayList.clear();
        favoriteSongArrayList.clear();
        historySongArrayList.clear();

        if (intent != null){
            if (intent.hasExtra("song")){
                Song song = intent.getParcelableExtra("song");
                songArrayList.add(song);
            }

            if (intent.hasExtra("list_song")){
                ArrayList<Song> listSong = intent.getParcelableArrayListExtra("list_song");
                songArrayList = listSong;
            }

            if (intent.hasExtra("library_song")){
                SongLibraryPlaylist songLibrary = intent.getParcelableExtra("library_song");
                songLibraryPlaylistArrayList.add(songLibrary);
            }

            if (intent.hasExtra("list_song_library")){
                ArrayList<SongLibraryPlaylist> listSongLibrary = intent.getParcelableArrayListExtra("list_song_library");
                songLibraryPlaylistArrayList = listSongLibrary;
            }

            if (intent.hasExtra("favorite_song")){
                FavoriteSong favoriteSong = intent.getParcelableExtra("favorite_song");
                favoriteSongArrayList.add(favoriteSong);
            }

            if (intent.hasExtra("history_song")){
                HistorySong historySong = intent.getParcelableExtra("history_song");
                historySongArrayList.add(historySong);
            }
        }
    }

    private void sendActionToService(int action){
        Intent intent = new Intent(this, ForegroundServiceControl.class);

        intent.putExtra(getResources().getString(R.string.action_music_service), action);
        intent.putExtra(getResources().getString(R.string.duration), durationToService);
        intent.putExtra(getResources().getString(R.string.repeat_music), repeat);
        intent.putExtra(getResources().getString(R.string.random_music), checkRandom);

        startService(intent);
    }

    private void startService(){
        Intent intent = new Intent(this, ForegroundServiceControl.class);

        if (songArrayList.size() > 0 ){
            intent.putExtra(getResources().getString(R.string.obj_song), songArrayList);

        } else if (songLibraryPlaylistArrayList.size() > 0){
            intent.putExtra(getResources().getString(R.string.obj_song_library), songLibraryPlaylistArrayList);

        } else if (favoriteSongArrayList.size() > 0){
            intent.putExtra(getResources().getString(R.string.obj_song_favorite),favoriteSongArrayList);

        } else if (historySongArrayList.size() > 0){
            intent.putExtra("obj_song_history", historySongArrayList);
        }

        startService(intent);
    }

    private void timeSong() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

        binding.activityPlayMusicTvTotalTime.setText(simpleDateFormat.format(duration));
        binding.activityPlayMusicSbSongTime.setMax(duration);
    }

    private void handleMusic(int action) {
        switch (action){
            case ForegroundServiceControl.ACTION_PAUSE:
                binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_pause_button);
                break;
            case ForegroundServiceControl.ACTION_RESUME:
                binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
                break;
            case ForegroundServiceControl.ACTION_NEXT:
                completeNextMusic();
                break;
            case ForegroundServiceControl.ACTION_PREVIOUS:
                completePreviousMusic();
                break;

        }

    }

    private void nextMusic(){
        binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
        timeValue = 0;
        setLikeSong();
    }

    private void previousMusic(){
        binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
        timeValue = 0;
        setLikeSong();
    }

    private void completeNextMusic() {
        if (songArrayList.size() > 0){
            nextMusic();
            setView(songArrayList.get(position).getImgSong(),
                    songArrayList.get(position).getNameSong());

        } else if (songLibraryPlaylistArrayList.size() > 0){
            nextMusic();
            setView(songLibraryPlaylistArrayList.get(position).getImageSong(),
                    songLibraryPlaylistArrayList.get(position).getNameSong());

        } else if (favoriteSongArrayList.size() > 0){
            nextMusic();
            setView(favoriteSongArrayList.get(position).getImageSong(),
                    favoriteSongArrayList.get(position).getNameSong());

        } else if (historySongArrayList.size() > 0){
            nextMusic();
            setView(historySongArrayList.get(position).getImageSong(),
                    historySongArrayList.get(position).getNameSong());
        }
    }

    private void completePreviousMusic() {
        if (songArrayList.size() > 0){
            previousMusic();
            setView(songArrayList.get(position).getImgSong(),
                    songArrayList.get(position).getNameSong());

        } else if (songLibraryPlaylistArrayList.size() > 0){
            previousMusic();
            setView(songLibraryPlaylistArrayList.get(position).getImageSong(),
                    songLibraryPlaylistArrayList.get(position).getNameSong());

        } else if (favoriteSongArrayList.size() > 0){
            previousMusic();
            setView(favoriteSongArrayList.get(position).getImageSong(),
                    favoriteSongArrayList.get(position).getNameSong());

        } else if (historySongArrayList.size() > 0){
            previousMusic();
            setView(historySongArrayList.get(position).getImageSong(),
                    historySongArrayList.get(position).getNameSong());
        }
    }

    private void setView(String songImg, String songName){
        setGradient(songImg);
        musicDiscFragment.playMusicDisc(songImg);
        Objects.requireNonNull(getSupportActionBar()).setTitle(songName);
//        binding.activityPlayMusicTvSingerName.setText(singerName);
//        binding.activityPlayMusicTvSongName.setText(songName);


    }

    @SuppressWarnings("deprecation")
    private void setViewStart(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (songArrayList.size() > 0){
                    setView(songArrayList.get(position).getImgSong(),
                            songArrayList.get(position).getNameSong());

                } else if (songLibraryPlaylistArrayList.size() > 0){
                    setView(songLibraryPlaylistArrayList.get(position).getImageSong(),
                            songLibraryPlaylistArrayList.get(position).getNameSong());

                } else if (favoriteSongArrayList.size() > 0){
                    setView(favoriteSongArrayList.get(position).getImageSong(),
                            favoriteSongArrayList.get(position).getNameSong());

                } else if (historySongArrayList.size() > 0){
                    setView(historySongArrayList.get(position).getImageSong(),
                            historySongArrayList.get(position).getNameSong());
                } else {
                    handler.postDelayed(this, 300);
                }
            }
        }, 500);
    }


    private void setGradient(String songImg){
//        Picasso.get().load(songImg).into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                Palette.from(bitmap).generate(palette -> {
//                    assert  palette != null;
//                    Palette.Swatch swatch = palette.getDominantSwatch();
//                    binding.activityPlayMusicRlContainer.setBackgroundResource(R.drawable.bgr_play_music);
//                    assert  swatch != null;
//                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
//                            new int[]{swatch.getRgb(), swatch.getRgb()});
//
//                    binding.activityPlayMusicRlContainer.setBackground(gradientDrawable);
//                });
//            }
//
//            @Override
//            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//        });
    }

    private void init(){

        musicDiscFragment = new MusicDiscFragment();
        playMusicPlaylistFragment = new PlayMusicPlaylistFragment();
        discViewPager = new DiscViewPager(getSupportFragmentManager());

        discViewPager.addFragment(musicDiscFragment);
        discViewPager.addFragment(playMusicPlaylistFragment);
        binding.activityPlayMusicVpDiscography.setAdapter(discViewPager);

        setSupportActionBar(binding.activityPlayMusicToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.activityPlayMusicToolbar.setTitleTextColor(Color.BLACK);

        musicDiscFragment = (MusicDiscFragment) discViewPager.getItem(0);

        binding.activityPlayMusicIbBackSong.setOnClickListener(view -> {
            sendActionToService(ForegroundServiceControl.ACTION_PREVIOUS);

        });

        binding.activityPlayMusicIbNextSong.setOnClickListener(view -> {
            sendActionToService(ForegroundServiceControl.ACTION_NEXT);
        });

        binding.activityPlayMusicIbPlayAndPauseSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying){
                    sendActionToService(ForegroundServiceControl.ACTION_PAUSE);
                    binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_pause_button);
                    musicDiscFragment.stopImgOfSong();
                } else {
                    sendActionToService(ForegroundServiceControl.ACTION_RESUME);
                    binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
                    musicDiscFragment.startImgOfSong();
                }
            }
        });


        binding.activityPlayMusicIbRepeatSong.setOnClickListener(this::onClick);

        setLikeSong();

        binding.activityPlayMusicIvLoveButton.setOnClickListener(view -> {
                if (!DataLocalManager.getCheckLogin()){
                    Intent intent = new Intent(PlayMusicActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    Animation animation = AnimationUtils.loadAnimation(PlayMusicActivity.this, R.anim.anim_love_click);
                    setBtnLike();
                    view.startAnimation(animation);
                }
            });

        binding.activityPlayMusicIbRandomSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkRandom){
                    if (repeat){
                        repeat = false;
                        binding.activityPlayMusicIbRandomSong.setImageResource(R.drawable.ic_shuffled);
                        binding.activityPlayMusicIbRepeatSong.setImageResource(R.drawable.ic_repeat_song);
                    } else {
                        binding.activityPlayMusicIbRandomSong.setImageResource(R.drawable.ic_shuffled);
                    }
                    checkRandom = true;
                } else {
                    binding.activityPlayMusicIbRandomSong.setImageResource(R.drawable.ic_random);
                    checkRandom = false;
                }
                sendActionToService(ForegroundServiceControl.ACTION_RANDOM);
            }
        });

        binding.activityPlayMusicSbSongTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                durationToService = seekBar.getProgress();
                sendActionToService(ForegroundServiceControl.ACTION_DURATION);
            }
        });

        binding.activityPlayMusicToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                songArrayList.clear();
                songLibraryPlaylistArrayList.clear();
                favoriteSongArrayList.clear();
                historySongArrayList.clear();
                finish();
            }
        });



        binding.activityPlayMusicSbSongTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                durationToService = seekBar.getProgress();
                sendActionToService(ForegroundServiceControl.ACTION_DURATION);
            }
        });

    }

    private void setLikeSong (){
        if (!DataLocalManager.getCheckLogin()){
            binding.activityPlayMusicIvLoveButton.setImageResource(R.drawable.ic_love);
            binding.activityPlayMusicIvLoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PlayMusicActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }else {
            if (songArrayList.size() > 0){
                song = new Song(songArrayList.get(position).getIdSong(), songArrayList.get(position).getNameSong(),
                        songArrayList.get(position).getImgSong(), songArrayList.get(position).getNameSong(),
                        songArrayList.get(position).getLinkSong());

            } else if (songLibraryPlaylistArrayList.size() > 0){
                song = new Song(songLibraryPlaylistArrayList.get(position).getIdSong(),
                        songLibraryPlaylistArrayList.get(position).getNameSong(),
                        songLibraryPlaylistArrayList.get(position).getImageSong(),
                        songLibraryPlaylistArrayList.get(position).getNameSong(),
                        songLibraryPlaylistArrayList.get(position).getLinkSong());

            }
            else if (favoriteSongArrayList.size() > 0){
                song = new Song(favoriteSongArrayList.get(position).getIdSong(),
                        favoriteSongArrayList.get(position).getNameSong(),
                        favoriteSongArrayList.get(position).getImageSong(),
                        favoriteSongArrayList.get(position).getNameSong(),
                        favoriteSongArrayList.get(position).getLinkSong());

            } else if (historySongArrayList.size() > 0){
                song = new Song(historySongArrayList.get(position).getIdSong(),
                        historySongArrayList.get(position).getNameSong(),
                        historySongArrayList.get(position).getImageSong(),
                        historySongArrayList.get(position).getNameSong(),
                        historySongArrayList.get(position).getLinkSong());
            }

            checkLikeSong();

        }
    }

    public void setBtnLike (){
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
                                    binding.activityPlayMusicIvLoveButton.setImageResource(R.drawable.ic_love);
                                }else {
                                    updateNumberOfLike();
                                    binding.activityPlayMusicIvLoveButton.setImageResource(R.drawable.ic_loved);
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        StyleableToast.makeText(PlayMusicActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                    }
                });
    }

    public void updateNumberOfLike() {
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
                StyleableToast.makeText(PlayMusicActivity.this, t.getMessage(),
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
                StyleableToast.makeText(PlayMusicActivity.this, t.getMessage(),
                        Toast.LENGTH_LONG, R.style.myToast).show();
            }
        });
    }
    public void checkLikeSong (){
        MusicRepository repository = new MusicRepository();

        repository.checkLikeSong(DataLocalManager.getUsernameData(), song.getIdSong())
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.body() != null){
                            //    Toast.makeText(context, response.body().getIsSuccess(), Toast.LENGTH_LONG).show();
                            if (response.body().getIsSuccess().equals(Constants.successfully)){
                                binding.activityPlayMusicIvLoveButton.setImageResource(R.drawable.ic_loved);
                            }else {
                                binding.activityPlayMusicIvLoveButton.setImageResource(R.drawable.ic_love);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        StyleableToast.makeText(PlayMusicActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onClick(View view) {
        if (!repeat){
            if (checkRandom){
                checkRandom = false;
                binding.activityPlayMusicIbRepeatSong.setImageResource(R.drawable.ic_repeated);
                binding.activityPlayMusicIbRandomSong.setImageResource(R.drawable.ic_random);
            } else {
                binding.activityPlayMusicIbRepeatSong.setImageResource(R.drawable.ic_repeated);
            }
            repeat = true;
        } else {
            binding.activityPlayMusicIbRepeatSong.setImageResource(R.drawable.ic_repeat_song);
            repeat = false;
        }
        sendActionToService(ForegroundServiceControl.ACTION_REPEAT);
    }

    private void loadData() {
        if (!AppUtil.isNetworkAvailable(this)) {
            DialogFragment dialogFragment = NoInternetDialog.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "NoInternetDialog");
        }
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        loadData();
//    }

}