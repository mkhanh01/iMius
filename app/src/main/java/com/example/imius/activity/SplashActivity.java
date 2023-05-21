package com.example.imius.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.imius.R;
import com.example.imius.data.DataLocalManager;
import com.example.imius.fragment.DialogAddToLibraryPlaylist;
import com.example.imius.fragment.NoInternetDialog;
import com.example.imius.network.AppUtil;

import io.github.muddz.styleabletoast.StyleableToast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadData();
    }

    private void loadData() {
        if (AppUtil.isNetworkAvailable(this)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!DataLocalManager.getFirstInstall()){
                        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
            }, 3000);
        }else {
            DialogFragment dialogFragment = NoInternetDialog.newInstance();
            dialogFragment.show(SplashActivity.this.getSupportFragmentManager(), "NoInternetDialog");
//            StyleableToast.makeText(this, getString(R.string.network_disconnected),
//                    Toast.LENGTH_LONG, R.style.myToast).show();
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        loadData();
//    }
}