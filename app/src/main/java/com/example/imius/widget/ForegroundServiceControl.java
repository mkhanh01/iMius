package com.example.imius.widget;

import static android.widget.RemoteViews.*;
import static com.example.imius.MyApplication.*;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.imius.MyApplication;
import com.example.imius.R;
import com.example.imius.activity.PlayMusicActivity;
import com.example.imius.constants.Constants;
import com.example.imius.model.FavoriteSong;
import com.example.imius.model.HistorySong;
import com.example.imius.model.Song;
import com.example.imius.model.SongLibraryPlaylist;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ForegroundServiceControl extends Service {
    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_NEXT = 3;
    public static final int ACTION_PREVIOUS = 4;
    public static final int ACTION_DURATION = 5;
    public static final int ACTION_REPEAT = 6;
    public static final int ACTION_RANDOM = 7;

    private MediaPlayer mediaPlayer;
    private boolean isRepeat, isRandom, isPlaying;

    private String songImage;

    private ArrayList<Song> songArrayList = new ArrayList<>();
    private ArrayList<SongLibraryPlaylist> songLibraryPlaylistArrayList = new ArrayList<>();
    private ArrayList<FavoriteSong> favoriteSongArrayList = new ArrayList<>();
    private ArrayList<HistorySong> historySongArrayList = new ArrayList<>();

    private int positionPlayer = 0, duration = 0, seekToTime = 0, currentTime = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null){
        //    sendNotificationMedia(songArrayList.get(positionPlayer).getNameSong(), songArrayList.get(positionPlayer).getNameSinger());

            if (intent.hasExtra("obj_song")){
                clearArray();
                songArrayList = intent.getParcelableArrayListExtra("obj_song");

            }
            if (intent.hasExtra("obj_song_library")){
                clearArray();
                songLibraryPlaylistArrayList = intent.getParcelableArrayListExtra("obj_song_library");

            }
            if (intent.hasExtra("obj_song_favorite")){
                clearArray();
                favoriteSongArrayList = intent.getParcelableArrayListExtra("obj_song_favorite");

            }
            if (intent.hasExtra("obj_song_history")){
                clearArray();
                historySongArrayList = intent.getParcelableArrayListExtra("obj_song_history");
            }
        }

        assert intent != null;
        if (!intent.hasExtra("action_music_service")){
            completeAndStart();
        }

        int actionMusic = intent.getIntExtra("action_music_service", 0);

        seekToTime = intent.getIntExtra("duration", 0);
        isRepeat = intent.getBooleanExtra("repeat_music", false);
        isRandom = intent.getBooleanExtra("random_music", false);

        handleActionMusic(actionMusic);

        return START_NOT_STICKY;
    }

    private void clearArray() {
        positionPlayer = 0;
        songArrayList.clear();
        songLibraryPlaylistArrayList.clear();
        favoriteSongArrayList.clear();
        historySongArrayList.clear();
    }

    private void handleActionMusic(int actionMusic) {
        switch (actionMusic){
            case ACTION_PAUSE:
                if (songArrayList != null && songArrayList.size() >0){
                    pauseMusic(songArrayList.get(positionPlayer).getNameSong(),
                            songArrayList.get(positionPlayer).getNameSinger());

                } else if (songLibraryPlaylistArrayList != null && songLibraryPlaylistArrayList.size() > 0){
                    pauseMusic(songLibraryPlaylistArrayList.get(positionPlayer).getNameSong(),
                            songLibraryPlaylistArrayList.get(positionPlayer).getNameSinger());

                } else if (favoriteSongArrayList != null && favoriteSongArrayList.size() > 0){
                    pauseMusic(favoriteSongArrayList.get(positionPlayer).getNameSong(),
                            favoriteSongArrayList.get(positionPlayer).getNameSinger());

                } else if (historySongArrayList != null && historySongArrayList.size() > 0){
                    pauseMusic(historySongArrayList.get(positionPlayer).getNameSong(),
                            historySongArrayList.get(positionPlayer).getNameSinger());
                }
                break;

            case ACTION_RESUME:
                if (songArrayList != null && songArrayList.size() >0){
                    resumeMusic(songArrayList.get(positionPlayer).getNameSong(),
                            songArrayList.get(positionPlayer).getNameSinger());

                } else if (songLibraryPlaylistArrayList != null && songLibraryPlaylistArrayList.size() > 0){
                    resumeMusic(songLibraryPlaylistArrayList.get(positionPlayer).getNameSong(),
                            songLibraryPlaylistArrayList.get(positionPlayer).getNameSinger());

                } else if (favoriteSongArrayList != null && favoriteSongArrayList.size() > 0){
                    resumeMusic(favoriteSongArrayList.get(positionPlayer).getNameSong(),
                            favoriteSongArrayList.get(positionPlayer).getNameSinger());

                }else if (historySongArrayList != null && historySongArrayList.size() > 0){
                    resumeMusic(historySongArrayList.get(positionPlayer).getNameSong(),
                            historySongArrayList.get(positionPlayer).getNameSinger());
                }
                break;
            case ACTION_NEXT:
                if (songArrayList != null && songArrayList.size() >0){
                    nextMusic(songArrayList.size());

                } else if (songLibraryPlaylistArrayList != null && songLibraryPlaylistArrayList.size() > 0){
                    nextMusic(songLibraryPlaylistArrayList.size());

                } else if (favoriteSongArrayList != null && favoriteSongArrayList.size() > 0){
                    nextMusic(favoriteSongArrayList.size());

                } else if (historySongArrayList != null && historySongArrayList.size() > 0){
                    nextMusic(favoriteSongArrayList.size());
                }
                completeAndStart();
                break;

            case ACTION_PREVIOUS:
                if (songArrayList != null && songArrayList.size() >0){
                    previousMusic(songArrayList.size());

                } else if (songLibraryPlaylistArrayList != null && songLibraryPlaylistArrayList.size() > 0){
                    previousMusic(songLibraryPlaylistArrayList.size());

                } else if (favoriteSongArrayList != null && favoriteSongArrayList.size() > 0){
                    previousMusic(favoriteSongArrayList.size());

                }else if (historySongArrayList != null && historySongArrayList.size() > 0){
                    previousMusic(historySongArrayList.size());
                }
                completeAndStart();
                break;

            case ACTION_DURATION:
                mediaPlayer.seekTo(seekToTime);
                break;
        }

    }

    private void startMusic(String songLink){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        new PlayMP3().onPostExecute(songLink);

        isPlaying = true;
        duration = mediaPlayer.getDuration();
        sendActionToPlayMusicActivity(ACTION_RESUME);
        sendTimeCurrent();
    }

    private void resumeMusic(String nameSong, String nameSinger) {
        if (mediaPlayer != null && !isPlaying){

            mediaPlayer.start();
            isPlaying = true;
            sendNotificationMedia(nameSong, nameSinger);
            sendActionToPlayMusicActivity(ACTION_RESUME);
        }
    }

    private void pauseMusic(String nameSong, String nameSinger) {
        if (mediaPlayer != null && isPlaying){

            mediaPlayer.pause();
            isPlaying = false;
            sendNotificationMedia(nameSong, nameSinger);
            sendActionToPlayMusicActivity(ACTION_PAUSE);
        }
    }

    private void nextMusic(int size) {
        positionPlayer++;
        if (isRepeat){
            positionPlayer -= 1;
        }else if (isRandom){
            Random random = new Random();
            positionPlayer = random.nextInt(size);
        }
        if (positionPlayer >= size){
            positionPlayer = 0;
        }
        sendActionToPlayMusicActivity(ACTION_NEXT);
    }

    private void previousMusic(int size){
        positionPlayer--;
        if (isRepeat){
            positionPlayer += 1;
        }else if (isRandom){
            Random random = new Random();
            positionPlayer = random.nextInt(size);
        }
        if (positionPlayer < 0){
            positionPlayer = size-1;
        }
        sendActionToPlayMusicActivity(ACTION_PREVIOUS);
    }

    private void completeAndStart() {
        if (songArrayList != null && songArrayList.size() > 0){
            startMusic(songArrayList.get(positionPlayer).getLinkSong());
            songImage = songArrayList.get(positionPlayer).getImgSong();
            sendNotificationMedia(songArrayList.get(positionPlayer).getNameSong(),
                    songArrayList.get(positionPlayer).getNameSinger());

        } else if (songLibraryPlaylistArrayList != null && songLibraryPlaylistArrayList.size() > 0){
            startMusic(songLibraryPlaylistArrayList.get(positionPlayer).getLinkSong());
            songImage = songLibraryPlaylistArrayList.get(positionPlayer).getImageSong();
            sendNotificationMedia(songLibraryPlaylistArrayList.get(positionPlayer).getNameSong(),
                    songLibraryPlaylistArrayList.get(positionPlayer).getNameSinger());

        } else if (favoriteSongArrayList != null && favoriteSongArrayList.size() > 0){
            startMusic(favoriteSongArrayList.get(positionPlayer).getLinkSong());
            songImage = favoriteSongArrayList.get(positionPlayer).getImageSong();
            sendNotificationMedia(favoriteSongArrayList.get(positionPlayer).getNameSong(),
                    favoriteSongArrayList.get(positionPlayer).getNameSinger());

        } else if (historySongArrayList != null && favoriteSongArrayList.size() > 0){
            startMusic(historySongArrayList.get(positionPlayer).getLinkSong());
            songImage = historySongArrayList.get(positionPlayer).getImageSong();
            sendNotificationMedia(historySongArrayList.get(positionPlayer).getNameSong(),
                    historySongArrayList.get(positionPlayer).getNameSinger());
        }
    }

    private void sendNotificationMedia(String nameSong, String nameSinger) {
        MediaSessionCompat mediaSession = new MediaSessionCompat(this, "tag");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_note)
                .setContentText("iMius")
                .setContentTitle(nameSong)
                .setContentText(nameSinger)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0,1,2)
                        .setMediaSession(mediaSession.getSessionToken()))
                .addAction(R.drawable.ic_back_song_for_noti, "Previous",getPendingIntent(this, ACTION_PREVIOUS));

        if (isPlaying){
            builder
                    .addAction(R.drawable.ic_play_button,"Pause", getPendingIntent(this, ACTION_PAUSE));
        } else {
            builder
                    .addAction(R.drawable.ic_pause_button,"Play", getPendingIntent(this, ACTION_RESUME));
        }

        builder.addAction(R.drawable.ic_next_song, "Next" , getPendingIntent(this,Constants.ACTION_NEXT));

        Picasso.get().load(songImage).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                builder.setLargeIcon(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        Notification notification = builder.build();
        startForeground(1, notification);
    }

    private PendingIntent getPendingIntent(Context context, int action){
        Intent intent = new Intent(this, BroadcastReceiverAction.class);
        intent.putExtra("action_music", action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    @SuppressWarnings("deprecation")
    private void sendTimeCurrent(){
        if (mediaPlayer != null){
            currentTime = mediaPlayer.getCurrentPosition();
            sendActionToPlayMusicActivity(ACTION_DURATION);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null){
                        sendTimeCurrent();
                    }
                }
            }, 1000);
        }
    }

    private void sendActionToPlayMusicActivity(int action) {

        if (songArrayList != null || songLibraryPlaylistArrayList != null || favoriteSongArrayList != null
            || historySongArrayList != null){

            Intent intent = new Intent("send_data_to_activity");

            intent.putExtra(getResources().getString(R.string.status_player), isPlaying);
            intent.putExtra(getResources().getString(R.string.action_music), action);
            intent.putExtra(getResources().getString(R.string.position_music), positionPlayer);
            intent.putExtra(getResources().getString(R.string.duration_music), duration);
            intent.putExtra(getResources().getString(R.string.seek_to_music), currentTime);

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();

    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    class PlayMP3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {

                        if (songArrayList != null && songArrayList.size() > 0){
                            nextMusic(songArrayList.size());
                        } else if (songLibraryPlaylistArrayList != null && songLibraryPlaylistArrayList.size() > 0){
                            nextMusic(songLibraryPlaylistArrayList.size());
                        } else if (favoriteSongArrayList != null && favoriteSongArrayList.size() > 0){
                            nextMusic(favoriteSongArrayList.size());
                        } else if (historySongArrayList != null && historySongArrayList.size() > 0){
                            nextMusic(historySongArrayList.size());
                        }
                        completeAndStart();
                    }
                });

                mediaPlayer.setDataSource(s);
                mediaPlayer.prepare();

            } catch (IOException e){
                e.printStackTrace();
            }

            mediaPlayer.start();
            duration = mediaPlayer.getDuration();
        }
    }
}
