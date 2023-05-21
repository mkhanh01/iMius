package com.example.imius.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.imius.R;

import com.example.imius.adapter.ViewPagerAdapter;
import com.example.imius.data.DataLocalManager;
import com.example.imius.databinding.ActivityHomeBinding;
import com.example.imius.fragment.NoInternetDialog;
import com.example.imius.network.AppUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);
        DataLocalManager.setCheckFromLogout(false);
        setUpViewPager();

        binding.activityHomeViewPager.setEnabled(false);

        binding.activityHomeBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        binding.activityHomeViewPager.setCurrentItem(0);
                        break;

                    case R.id.action_search:
                        binding.activityHomeViewPager.setCurrentItem(1);
                        break;

                    case R.id.action_library:
                        if (!DataLocalManager.getCheckLogin()){
                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else {
                            binding.activityHomeViewPager.setCurrentItem(2);
                        }
                        break;
                    case R.id.action_account:
                        binding.activityHomeViewPager.setCurrentItem(3);
                        break;
                }

                return true;
            }
        });
    }

    private void setUpViewPager(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        binding.activityHomeViewPager.setAdapter(viewPagerAdapter);

    }

}