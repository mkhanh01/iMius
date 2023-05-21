package com.example.imius.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.imius.R;
import com.example.imius.fragment.DialogAddToLibraryPlaylist;
import com.example.imius.fragment.LibraryPlaylistFragment;

public class AddMusicToLibraryActivity extends AppCompatActivity {

    private int idLibraryPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music_to_library);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LibraryPlaylistFragment add_music_library =new LibraryPlaylistFragment();
        fragmentTransaction.add(R.id.activity_add_music_to_library_fl_content, add_music_library);
        fragmentTransaction.commit();

        Intent intent =getIntent();
        idLibraryPlaylist = intent.getIntExtra("id", 1);
    }

    public int getIdLibraryPlaylist() {
        return idLibraryPlaylist;
    }
}