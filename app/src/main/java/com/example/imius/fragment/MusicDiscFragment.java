package com.example.imius.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.fragment.app.Fragment;

import com.example.imius.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicDiscFragment extends Fragment {

    private CircleImageView circleImageView;
    private ObjectAnimator objectAnimator;

    public static MusicDiscFragment newInstance() {
        MusicDiscFragment fragment = new MusicDiscFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_disc, container, false);

        circleImageView = view.findViewById(R.id.fragment_music_disc_civ_disc_image);
        startImgOfSong();
        return view;
    }

    public void playMusicDisc(String image){
        Picasso.get().load(image).into(circleImageView);
    }

    public void stopImgOfSong(){
        objectAnimator.pause();
    }

    public void startImgOfSong(){
        objectAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0f, 360f);
        objectAnimator.setDuration(10000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
    }
}