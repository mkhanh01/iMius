package com.example.imius.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.imius.data.DataLocalManager;
import com.example.imius.databinding.ActivityWelcomeBinding;
import com.example.imius.fragment.NoInternetDialog;
import com.example.imius.network.AppUtil;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        loadData();
        setListener();
        setContentView(view);
    }


    private void setListener() {

        binding.activityWelcomeBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLoginActivity();
                DataLocalManager.setFirstInstall(true);
            }
        });

        binding.activityWelcomeTvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataLocalManager.setFirstInstall(true);
                callSplashActivity();
            }
        });

        binding.activityWelcomeBtnSignupWithGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataLocalManager.setFirstInstall(true);
                callSignUpActivity();
            }
        });
    }

    private void callLoginActivity() {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void callSignUpActivity() {
        Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void callHomeActivity() {
        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void callSplashActivity() {
        Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
        startActivity(intent);
    }

    private void loadData() {
        if (!AppUtil.isNetworkAvailable(this)) {
            DialogFragment dialogFragment = NoInternetDialog.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "NoInternetDialog");
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        loadData();
//    }
}