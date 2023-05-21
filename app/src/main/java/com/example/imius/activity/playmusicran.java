//package com.example.imius.activity;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProvider;
//
//import android.content.Intent;
//import android.graphics.Color;
//
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.StrictMode;
//import android.view.View;
//
//import android.widget.SeekBar;
//import android.widget.Toast;
//
//import com.example.imius.constants.Constants;
//import com.example.imius.data.DataLocalManager;
//import com.example.imius.databinding.ActivityPlayMusicBinding;
//
//import com.example.imius.R;
//
//import com.example.imius.fragment.MusicDiscFragment;
//import com.example.imius.fragment.PlayMusicPlaylistFragment;
//import com.example.imius.model.BaseResponse;
//import com.example.imius.model.FavoriteSong;
//import com.example.imius.model.HistorySong;
//import com.example.imius.model.Song;
//import com.example.imius.model.SongLibraryPlaylist;
//
//
//import com.example.imius.repository.MusicRepository;
//import com.example.imius.viewmodel.SongViewModel;
//import com.example.imius.widget.DiscViewPager;
//import com.example.imius.widget.ForegroundServiceControl;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Objects;
//import java.util.Random;
//
//import io.github.muddz.styleabletoast.StyleableToast;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class PlayMusic extends AppCompatActivity {
//
//    private ActivityPlayMusicBinding binding;
//    private SongViewModel viewModel;
//
//    private int index = 0, position = 0;
//
//    private String username;
//    private boolean repeat = false;
//    private boolean checkRandom = false;
//    private boolean nextSong = false;
//
//    private MusicDiscFragment musicDiscFragment;
//    public static DiscViewPager discViewPager;
//    private PlayMusicPlaylistFragment playMusicPlaylistFragment;
//
//    MediaPlayer mediaPlayer;
//
//    public static ArrayList<Song> songArrayList = new ArrayList<>();
//    public static ArrayList<SongLibraryPlaylist> songLibraryPlaylistArrayList = new ArrayList<>();
//    public static ArrayList<FavoriteSong> favoriteSongArrayList = new ArrayList<>();
//    public static ArrayList<HistorySong> historySongArrayList = new ArrayList<>();
//
//    private Song song = new Song();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityPlayMusicBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        viewModel = new ViewModelProvider(this).get(SongViewModel.class);
//        setContentView(view);
//
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        getDataFromIntent();
//        init();
//        eventClick();
//        startService();
//
//        overridePendingTransition(R.anim.anim_intent_in, R.anim.anim_intent_out);
//
//    }
//
//    private void startService(){
//        Intent intent = new Intent(this, ForegroundServiceControl.class);
//
//        Bundle bundle = new Bundle();
//        if (songArrayList.size() > 0){
//            bundle.putSerializable("obj_song", songArrayList);
//            intent.putExtras(bundle);
//        }
//        if (songLibraryPlaylistArrayList.size() > 0){
//            bundle.putSerializable("obj_song_library", songArrayList);
//            intent.putExtras(bundle);
//        }
//        if (favoriteSongArrayList.size() > 0){
//            bundle.putSerializable("obj_song_favorite", songArrayList);
//            intent.putExtras(bundle);
//        }
//        if (historySongArrayList.size() > 0){
//            bundle.putSerializable("obj_song_history", songArrayList);
//            intent.putExtras(bundle);
//        }
//
//        startService(intent);
//    }
//
//    private void init(){
//
//        musicDiscFragment = new MusicDiscFragment();
//        playMusicPlaylistFragment = new PlayMusicPlaylistFragment();
//        discViewPager = new DiscViewPager(getSupportFragmentManager());
//
//        discViewPager.addFragment(musicDiscFragment);
//        discViewPager.addFragment(playMusicPlaylistFragment);
//
//        binding.activityPlayMusicVpDiscography.setAdapter(discViewPager);
//
//        setSupportActionBar(binding.activityPlayMusicToolbar);
//
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//
//        musicDiscFragment = (MusicDiscFragment) discViewPager.getItem(0);
//
//        if (songArrayList.size() > 0){
//            getSupportActionBar().setTitle(songArrayList.get(0).getNameSong());
//            new PlayMP3().execute(songArrayList.get(0).getLinkSong());
//            binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//        }
//
//        if (songLibraryPlaylistArrayList.size() > 0){
//            getSupportActionBar().setTitle(songLibraryPlaylistArrayList.get(0).getNameSong());
//            new PlayMP3().execute(songLibraryPlaylistArrayList.get(0).getLinkSong());
//            binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//        }
//
//        if (favoriteSongArrayList.size() > 0){
//            getSupportActionBar().setTitle(favoriteSongArrayList.get(0).getNameSong());
//            new PlayMP3().execute(favoriteSongArrayList.get(0).getLinkSong());
//            binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//        }
//
//        if (historySongArrayList.size() > 0){
//            getSupportActionBar().setTitle(historySongArrayList.get(0).getNameSong());
//            new PlayMP3().execute(historySongArrayList.get(0).getLinkSong());
//            binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//        }
//
//        binding.activityPlayMusicToolbar.setNavigationOnClickListener( view -> {
//            finish();
//            mediaPlayer.stop();
//            songArrayList.clear();
//            songLibraryPlaylistArrayList.clear();
//            favoriteSongArrayList.clear();
//            historySongArrayList.clear();
//        });
//
//        binding.activityPlayMusicToolbar.setTitleTextColor(Color.BLACK);
//
//    }
//
//
//    private void eventClick() {
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (discViewPager.getItem(1) != null){
//                    if (songArrayList.size() > 0){
//                        musicDiscFragment.playMusicDisc(songArrayList.get(0).getImgSong());
//                        handler.removeCallbacks(this);
//                    } else if (songLibraryPlaylistArrayList.size() > 0){
//                        musicDiscFragment.playMusicDisc(songLibraryPlaylistArrayList.get(0).getImageSong());
//                        handler.removeCallbacks(this);
//                    } else if (favoriteSongArrayList.size() > 0){
//                        musicDiscFragment.playMusicDisc(favoriteSongArrayList.get(0).getImageSong());
//                        handler.removeCallbacks(this);
//                    } else if (historySongArrayList.size() >0){
//                        musicDiscFragment.playMusicDisc(historySongArrayList.get(0).getImageSong());
//                        handler.removeCallbacks(this);
//                    }else {
//                        handler.postDelayed(this, 300);
//                    }
//
//                }
//            }
//        },500);
//
//        binding.activityPlayMusicIbBackSong.setOnClickListener(view -> {
//            if (songArrayList.size() > 0){
//                if (mediaPlayer.isPlaying() || mediaPlayer != null){
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    mediaPlayer = null;
//                }
//                if (position < (songArrayList.size())){
//                    binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//                    position--;
//
//                    if (position < 0){
//                        position = songArrayList.size() - 1;
//                    }
//
//                    if (repeat == true){
//                        position += 1;
//                    }
//
//                    if (checkRandom == true ){
//                        Random random = new Random();
//                        index = random.nextInt(songArrayList.size());
//                        if (index == position){
//                            position = index - 1;
//                        }
//
//                        position = index;
//                    }
//                    new PlayMP3().execute(songArrayList.get(position).getLinkSong());
//                    musicDiscFragment.playMusicDisc(songArrayList.get(position).getImgSong());
//                    getSupportActionBar().setTitle(songArrayList.get(position).getNameSong());
//                    updateTimeSong();
//                }
//            }
//
//            if (songLibraryPlaylistArrayList.size() > 0){
//                if (mediaPlayer.isPlaying() || mediaPlayer != null){
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    mediaPlayer = null;
//                }
//                if (position < (songLibraryPlaylistArrayList.size())){
//                    binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//                    position--;
//
//                    if (position < 0){
//                        position = songLibraryPlaylistArrayList.size() - 1;
//                    }
//
//                    if (repeat == true){
//                        position += 1;
//                    }
//
//                    if (checkRandom == true ){
//                        Random random = new Random();
//                        index = random.nextInt(songLibraryPlaylistArrayList.size());
//                        if (index == position){
//                            position = index - 1;
//                        }
//
//                        position = index;
//                    }
//                    new PlayMP3().execute(songLibraryPlaylistArrayList.get(position).getLinkSong());
//                    musicDiscFragment.playMusicDisc(songLibraryPlaylistArrayList.get(position).getImageSong());
//                    getSupportActionBar().setTitle(songLibraryPlaylistArrayList.get(position).getNameSong());
//                    updateTimeSong();
//                }
//            }
//
//            if (favoriteSongArrayList.size() > 0){
//                if (mediaPlayer.isPlaying() || mediaPlayer != null){
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    mediaPlayer = null;
//                }
//                if (position < (favoriteSongArrayList.size())){
//                    binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//                    position--;
//
//                    if (position < 0){
//                        position = favoriteSongArrayList.size() - 1;
//                    }
//
//                    if (repeat == true){
//                        position += 1;
//                    }
//
//                    if (checkRandom == true ){
//                        Random random = new Random();
//                        index = random.nextInt(favoriteSongArrayList.size());
//                        if (index == position){
//                            position = index - 1;
//                        }
//
//                        position = index;
//                    }
//                    new PlayMP3().execute(favoriteSongArrayList.get(position).getLinkSong());
//                    musicDiscFragment.playMusicDisc(favoriteSongArrayList.get(position).getImageSong());
//                    getSupportActionBar().setTitle(favoriteSongArrayList.get(position).getNameSong());
//                    updateTimeSong();
//                }
//            }
//
//            if (historySongArrayList.size() > 0){
//                if (mediaPlayer.isPlaying() || mediaPlayer != null){
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                    mediaPlayer = null;
//                }
//                if (position < (historySongArrayList.size())){
//                    binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//                    position--;
//
//                    if (position < 0){
//                        position = historySongArrayList.size() - 1;
//                    }
//
//                    if (repeat == true){
//                        position += 1;
//                    }
//
//                    if (checkRandom == true ){
//                        Random random = new Random();
//                        index = random.nextInt(historySongArrayList.size());
//                        if (index == position){
//                            position = index - 1;
//                        }
//
//                        position = index;
//                    }
//                    new PlayMP3().execute(historySongArrayList.get(position).getLinkSong());
//                    musicDiscFragment.playMusicDisc(historySongArrayList.get(position).getImageSong());
//                    getSupportActionBar().setTitle(historySongArrayList.get(position).getNameSong());
//                    updateTimeSong();
//                }
//            }
//            binding.activityPlayMusicIbBackSong.setClickable(false);
//            binding.activityPlayMusicIbNextSong.setClickable(false);
//
//            Handler handler1 = new Handler();
//            handler1.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    binding.activityPlayMusicIbBackSong.setClickable(true);
//                    binding.activityPlayMusicIbNextSong.setClickable(true);
//                }
//            }, 5000);
//        });
//
//        binding.activityPlayMusicIbNextSong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (songArrayList.size() > 0){
//                    if (mediaPlayer.isPlaying() || mediaPlayer != null){
//                        mediaPlayer.stop();
//                        mediaPlayer.release();
//                        mediaPlayer = null;
//                    }
//                    if (position < (songArrayList.size())){
//                        binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//                        position++;
//
//                        if (repeat == true){
//                            if (position == 0){
//                                position = songArrayList.size();
//                            }
//                            position -= 1;
//                        }
//
//                        if (checkRandom == true ){
//                            Random random = new Random();
//                            index = random.nextInt(songArrayList.size());
//                            if (index == position){
//                                position = index - 1;
//                            }
//
//                            position = index;
//                        }
//                        if (position > (songArrayList.size() - 1)){
//                            position = 0;
//                        }
//                        new PlayMP3().execute(songArrayList.get(position).getLinkSong());
//                        musicDiscFragment.playMusicDisc(songArrayList.get(position).getImgSong());
//                        getSupportActionBar().setTitle(songArrayList.get(position).getNameSong());
//                        updateTimeSong();
//                    }
//
//                }
//
//                if (songLibraryPlaylistArrayList.size() > 0){
//                    if (mediaPlayer.isPlaying() || mediaPlayer != null){
//                        mediaPlayer.stop();
//                        mediaPlayer.release();
//                        mediaPlayer = null;
//                    }
//                    if (position < (songLibraryPlaylistArrayList.size())){
//                        binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//                        position++;
//
//                        if (repeat == true){
//                            if (position == 0){
//                                position = songLibraryPlaylistArrayList.size();
//                            }
//                            position -= 1;
//                        }
//
//                        if (checkRandom == true ){
//                            Random random = new Random();
//                            index = random.nextInt(songLibraryPlaylistArrayList.size());
//                            if (index == position){
//                                position = index - 1;
//                            }
//
//                            position = index;
//                        }
//                        if (position > (songLibraryPlaylistArrayList.size() - 1)){
//                            position = 0;
//                        }
//                        new PlayMP3().execute(songLibraryPlaylistArrayList.get(position).getLinkSong());
//                        musicDiscFragment.playMusicDisc(songLibraryPlaylistArrayList.get(position).getImageSong());
//                        getSupportActionBar().setTitle(songLibraryPlaylistArrayList.get(position).getNameSong());
//                        updateTimeSong();
//                    }
//
//                }
//
//                if (favoriteSongArrayList.size() > 0){
//                    if (mediaPlayer.isPlaying() || mediaPlayer != null){
//                        mediaPlayer.stop();
//                        mediaPlayer.release();
//                        mediaPlayer = null;
//                    }
//                    if (position < (favoriteSongArrayList.size())){
//                        binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//                        position++;
//
//                        if (repeat == true){
//                            if (position == 0){
//                                position = favoriteSongArrayList.size();
//                            }
//                            position -= 1;
//                        }
//
//                        if (checkRandom == true ){
//                            Random random = new Random();
//                            index = random.nextInt(favoriteSongArrayList.size());
//                            if (index == position){
//                                position = index - 1;
//                            }
//
//                            position = index;
//                        }
//                        if (position > (favoriteSongArrayList.size() - 1)){
//                            position = 0;
//                        }
//                        new PlayMP3().execute(favoriteSongArrayList.get(position).getLinkSong());
//                        musicDiscFragment.playMusicDisc(favoriteSongArrayList.get(position).getImageSong());
//                        getSupportActionBar().setTitle(favoriteSongArrayList.get(position).getNameSong());
//                        updateTimeSong();
//                    }
//                }
//
//                if (historySongArrayList.size() > 0){
//                    if (mediaPlayer.isPlaying() || mediaPlayer != null){
//                        mediaPlayer.stop();
//                        mediaPlayer.release();
//                        mediaPlayer = null;
//                    }
//                    if (position < (historySongArrayList.size())){
//                        binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//                        position++;
//
//                        if (repeat == true){
//                            if (position == 0){
//                                position = historySongArrayList.size();
//                            }
//                            position -= 1;
//                        }
//
//                        if (checkRandom == true ){
//                            Random random = new Random();
//                            index = random.nextInt(historySongArrayList.size());
//                            if (index == position){
//                                position = index - 1;
//                            }
//
//                            position = index;
//                        }
//                        if (position > (historySongArrayList.size() - 1)){
//                            position = 0;
//                        }
//                        new PlayMP3().execute(historySongArrayList.get(position).getLinkSong());
//                        musicDiscFragment.playMusicDisc(historySongArrayList.get(position).getImageSong());
//                        getSupportActionBar().setTitle(historySongArrayList.get(position).getNameSong());
//                        updateTimeSong();
//                    }
//
//                }
//                binding.activityPlayMusicIbBackSong.setClickable(false);
//                binding.activityPlayMusicIbNextSong.setClickable(false);
//
//                Handler handler1 = new Handler();
//                handler1.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        binding.activityPlayMusicIbBackSong.setClickable(true);
//                        binding.activityPlayMusicIbNextSong.setClickable(true);
//                    }
//                }, 5000);
//            }
//        });
//
//        binding.activityPlayMusicIbPlayAndPauseSong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mediaPlayer.isPlaying()){
//                    mediaPlayer.pause();
//                    binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_pause_button);
//                    musicDiscFragment.stopImgOfSong();
//                } else {
//                    mediaPlayer.start();
//                    binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//                    musicDiscFragment.startImgOfSong();
//                }
//            }
//        });
//
//        binding.activityPlayMusicIbRepeatSong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (repeat == false){
//                    if (checkRandom == true){
//                        checkRandom = false;
//                        binding.activityPlayMusicIbRepeatSong.setImageResource(R.drawable.ic_repeated);
//                        binding.activityPlayMusicIbRandomSong.setImageResource(R.drawable.ic_random);
//                    }
//                    binding.activityPlayMusicIbRepeatSong.setImageResource(R.drawable.ic_repeated);
//                    repeat = true;
//                } else {
//                    binding.activityPlayMusicIbRepeatSong.setImageResource(R.drawable.ic_repeat_song);
//                    repeat = false;
//                }
//            }
//        });
//
//
//
//        binding.activityPlayMusicIbRandomSong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (checkRandom == false){
//                    if (repeat == true){
//                        repeat = false;
//                        binding.activityPlayMusicIbRandomSong.setImageResource(R.drawable.ic_shuffled);
//                        binding.activityPlayMusicIbRepeatSong.setImageResource(R.drawable.ic_repeat_song);
//                    }
//                    binding.activityPlayMusicIbRandomSong.setImageResource(R.drawable.ic_shuffled);
//                    checkRandom = true;
//                } else {
//                    binding.activityPlayMusicIbRandomSong.setImageResource(R.drawable.ic_random);
//                    checkRandom = false;
//                }
//            }
//        });
//
//        if (!DataLocalManager.getCheckLogin()){
//            binding.activityPlayMusicIvLoveButton.setImageResource(R.drawable.ic_love);
//            binding.activityPlayMusicIvLoveButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(PlayMusicActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                }
//            });
//        }else {
//            if (songArrayList.size() > 0){
//                song = new Song(songArrayList.get(position).getIdSong(), songArrayList.get(position).getNameSong(),
//                        songArrayList.get(position).getImgSong(), songArrayList.get(position).getNameSong(),
//                        songArrayList.get(position).getLinkSong());
//
//            } else if (songLibraryPlaylistArrayList.size() > 0){
//                song = new Song(songLibraryPlaylistArrayList.get(position).getIdSong(),
//                        songLibraryPlaylistArrayList.get(position).getNameSong(),
//                        songLibraryPlaylistArrayList.get(position).getImageSong(),
//                        songLibraryPlaylistArrayList.get(position).getNameSong(),
//                        songLibraryPlaylistArrayList.get(position).getLinkSong());
//
//            }
//            else if (favoriteSongArrayList.size() > 0){
//                song = new Song(favoriteSongArrayList.get(position).getIdSong(),
//                        favoriteSongArrayList.get(position).getNameSong(),
//                        favoriteSongArrayList.get(position).getImageSong(),
//                        favoriteSongArrayList.get(position).getNameSong(),
//                        favoriteSongArrayList.get(position).getLinkSong());
//
//            } else if (historySongArrayList.size() > 0){
//                song = new Song(historySongArrayList.get(position).getIdSong(),
//                        historySongArrayList.get(position).getNameSong(),
//                        historySongArrayList.get(position).getImageSong(),
//                        historySongArrayList.get(position).getNameSong(),
//                        historySongArrayList.get(position).getLinkSong());
//            }
//
//            checkLikeSong();
//            binding.activityPlayMusicIvLoveButton.setOnClickListener(view -> {
//                setBtnLike();
//            });
//        }
//
//        binding.activityPlayMusicSbSongTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                mediaPlayer.seekTo(seekBar.getProgress());
//
//            }
//        });
//
//    }
//
//    public void checkLikeSong (){
//        MusicRepository repository = new MusicRepository();
//
//        repository.checkLikeSong(DataLocalManager.getUsernameData(), song.getIdSong())
//                .enqueue(new Callback<BaseResponse>() {
//                    @Override
//                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
//                        if (response.body() != null){
//                            //    Toast.makeText(context, response.body().getIsSuccess(), Toast.LENGTH_LONG).show();
//                            if (response.body().getIsSuccess().equals(Constants.successfully)){
//                                binding.activityPlayMusicIvLoveButton.setImageResource(R.drawable.ic_loved);
//                            }else {
//                                binding.activityPlayMusicIvLoveButton.setImageResource(R.drawable.ic_love);
//                            }
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<BaseResponse> call, Throwable t) {
//                        StyleableToast.makeText(PlayMusicActivity.this, t.getMessage(),
//                                Toast.LENGTH_LONG, R.style.myToast).show();
//                    }
//                });
//    }
//
//    public void setBtnLike (){
//        MusicRepository repository = new MusicRepository();
//
//        repository.updateLikeOfNumber(song.getIdSong())
//                .enqueue(new Callback<BaseResponse>() {
//                    @Override
//                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
//                        if (response.body() != null){
//                            //    Toast.makeText(context, response.body().getIsSuccess(), Toast.LENGTH_LONG).show();
//                            if (response.body().getIsSuccess().equals(Constants.successfully)){
//                                if (response.body().getMessage().equals(Constants.DELETE)){
//                                    deleteLikeSong(song.getIdSong());
//                                    binding.activityPlayMusicIvLoveButton.setImageResource(R.drawable.ic_love);
//                                }else {
//                                    updateNumberOfLike();
//                                    binding.activityPlayMusicIvLoveButton.setImageResource(R.drawable.ic_loved);
//                                }
//                            }
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<BaseResponse> call, Throwable t) {
//                        StyleableToast.makeText(PlayMusicActivity.this, t.getMessage(),
//                                Toast.LENGTH_LONG, R.style.myToast).show();
//                    }
//                });
//    }
//
//    public void updateNumberOfLike() {
//        MusicRepository repository = new MusicRepository();
//
//        //     repository.insertLoveSong(DataLocalManager.getUsernameData(), song);
//        repository.insertLoveSong(DataLocalManager.getUsernameData(), song).enqueue(new Callback<BaseResponse>() {
//            @Override
//            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
////                if (response.body().getIsSuccess().equals(Constants.successfully)){
////                    Toast.makeText(context, "insert success", Toast.LENGTH_SHORT).show();
////                }else {
////                    Toast.makeText(context, "insert failed", Toast.LENGTH_SHORT).show();
////                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse> call, Throwable t) {
//                StyleableToast.makeText(PlayMusicActivity.this, t.getMessage(),
//                        Toast.LENGTH_LONG, R.style.myToast).show();
//            }
//        });
//    }
//
//    public void deleteLikeSong(int idSong) {
//        MusicRepository repository = new MusicRepository();
//        //       repository.deleteLikeSong(DataLocalManager.getUsernameData(), idSong);
//
//        repository.deleteLikeSong(DataLocalManager.getUsernameData(), idSong).enqueue(new Callback<BaseResponse>() {
//            @Override
//            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
////                if (response.body().getIsSuccess().equals(Constants.successfully)){
////                    Toast.makeText(context, "delete success", Toast.LENGTH_SHORT).show();
////                }else {
////                    Toast.makeText(context, "delete failed", Toast.LENGTH_SHORT).show();
////                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse> call, Throwable t) {
//                StyleableToast.makeText(PlayMusicActivity.this, t.getMessage(),
//                        Toast.LENGTH_LONG, R.style.myToast).show();
//            }
//        });
//    }
//
//    private void getDataFromIntent(){
//        Intent intent = getIntent();
//
//        songArrayList.clear();
//        songLibraryPlaylistArrayList.clear();
//        favoriteSongArrayList.clear();
//        historySongArrayList.clear();
//
//        if (intent != null){
//            if (intent.hasExtra("song")){
//                Song song = intent.getParcelableExtra("song");
//                songArrayList.add(song);
//            }
//
//            if (intent.hasExtra("list_song")){
//                ArrayList<Song> listSong = intent.getParcelableArrayListExtra("list_song");
//                songArrayList = listSong;
//            }
//
//            if (intent.hasExtra("library_song")){
//                SongLibraryPlaylist songLibrary = intent.getParcelableExtra("library_song");
//                songLibraryPlaylistArrayList.add(songLibrary);
//            }
//
//            if (intent.hasExtra("list_song_library")){
//                ArrayList<SongLibraryPlaylist> listSongLibrary = intent.getParcelableArrayListExtra("list_song_library");
//                songLibraryPlaylistArrayList = listSongLibrary;
//            }
//
//            if (intent.hasExtra("favorite_song")){
//                FavoriteSong favoriteSong = intent.getParcelableExtra("favorite_song");
//                favoriteSongArrayList.add(favoriteSong);
//            }
//
//            if (intent.hasExtra("history_song")){
//                HistorySong historySong = intent.getParcelableExtra("history_song");
//                historySongArrayList.add(historySong);
//            }
//        }
//    }
//
//
//
//    private void timeSong() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
//
//        binding.activityPlayMusicTvTotalTime.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
//        binding.activityPlayMusicSbSongTime.setMax(mediaPlayer.getDuration());
//    }
//
//    private void updateTimeSong(){
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mediaPlayer != null){
//                    binding.activityPlayMusicSbSongTime.setProgress(mediaPlayer.getCurrentPosition());
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
//                    binding.activityPlayMusicTvRuntime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
//                    handler.postDelayed(this, 300);
//                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mediaPlayer) {
//                            nextSong = true;
//                            try {
//                                Thread.sleep(1000);
//                            }catch (InterruptedException e){
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
//            }
//        },300);
//        Handler handler1 = new Handler();
//        handler1.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (nextSong == true){
//                    if (position < (songArrayList.size())){
//                        binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//                        position++;
//
//                        if (repeat == true){
//                            if (position == 0){
//                                position = songArrayList.size();
//                            }
//                            position -= 1;
//                        }
//
//                        if (checkRandom == true ){
//                            Random random = new Random();
//                            index = random.nextInt(songArrayList.size());
//                            if (index == position){
//                                position = index - 1;
//                            }
//
//                            position = index;
//                        }
//                        if (position > (songArrayList.size() - 1)){
//                            position = 0;
//                        }
//                        new PlayMP3().execute(songArrayList.get(position).getLinkSong());
//                        musicDiscFragment.playMusicDisc(songArrayList.get(position).getImgSong());
//                        getSupportActionBar().setTitle(songArrayList.get(position).getNameSong());
//                    }
//
//                    if (position < (songLibraryPlaylistArrayList.size())){
//                        binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//                        position++;
//
//                        if (repeat == true){
//                            if (position == 0){
//                                position = songLibraryPlaylistArrayList.size();
//                            }
//                            position -= 1;
//                        }
//
//                        if (checkRandom == true ){
//                            Random random = new Random();
//                            index = random.nextInt(songLibraryPlaylistArrayList.size());
//                            if (index == position){
//                                position = index - 1;
//                            }
//
//                            position = index;
//                        }
//                        if (position > (songLibraryPlaylistArrayList.size() - 1)){
//                            position = 0;
//                        }
//                        new PlayMP3().execute(songLibraryPlaylistArrayList.get(position).getLinkSong());
//                        musicDiscFragment.playMusicDisc(songLibraryPlaylistArrayList.get(position).getImageSong());
//                        getSupportActionBar().setTitle(songLibraryPlaylistArrayList.get(position).getNameSong());
//                    }
//
//                    if (position < (favoriteSongArrayList.size())){
//                        binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//                        position++;
//
//                        if (repeat == true){
//                            if (position == 0){
//                                position = favoriteSongArrayList.size();
//                            }
//                            position -= 1;
//                        }
//
//                        if (checkRandom == true ){
//                            Random random = new Random();
//                            index = random.nextInt(favoriteSongArrayList.size());
//                            if (index == position){
//                                position = index - 1;
//                            }
//
//                            position = index;
//                        }
//                        if (position > (favoriteSongArrayList.size() - 1)){
//                            position = 0;
//                        }
//                        new PlayMP3().execute(favoriteSongArrayList.get(position).getLinkSong());
//                        musicDiscFragment.playMusicDisc(favoriteSongArrayList.get(position).getImageSong());
//                        getSupportActionBar().setTitle(favoriteSongArrayList.get(position).getNameSong());
//                    }
//
//                    if (position < (historySongArrayList.size())){
//                        binding.activityPlayMusicIbPlayAndPauseSong.setImageResource(R.drawable.ic_play_button);
//                        position++;
//
//                        if (repeat == true){
//                            if (position == 0){
//                                position = historySongArrayList.size();
//                            }
//                            position -= 1;
//                        }
//
//                        if (checkRandom == true ){
//                            Random random = new Random();
//                            index = random.nextInt(historySongArrayList.size());
//                            if (index == position){
//                                position = index - 1;
//                            }
//
//                            position = index;
//                        }
//                        if (position > (historySongArrayList.size() - 1)){
//                            position = 0;
//                        }
//                        new PlayMP3().execute(historySongArrayList.get(position).getLinkSong());
//                        musicDiscFragment.playMusicDisc(historySongArrayList.get(position).getImageSong());
//                        getSupportActionBar().setTitle(historySongArrayList.get(position).getNameSong());
//                    }
//
//                    binding.activityPlayMusicIbBackSong.setClickable(false);
//
//                    binding.activityPlayMusicIbNextSong.setClickable(false);
//
//                    Handler handler1 = new Handler();
//                    handler1.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            binding.activityPlayMusicIbBackSong.setClickable(true);
//                            binding.activityPlayMusicIbNextSong.setClickable(true);
//                        }
//                    }, 5000);
//                    nextSong = false;
//                    handler1.removeCallbacks(this);
//                } else {
//                    handler1.postDelayed(this, 1000);
//                }
//            }
//        },1000);
//    }
//
//    class PlayMP3 extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            return strings[0];
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            try {
//                mediaPlayer = new MediaPlayer();
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        mediaPlayer.stop();
//                        mediaPlayer.reset();
//                    }
//                });
//
//                mediaPlayer.setDataSource(s);
//                mediaPlayer.prepare();
//
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//
//            mediaPlayer.start();
//            timeSong();
//            updateTimeSong();
//        }
//    }
//}
