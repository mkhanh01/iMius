package com.example.imius.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.imius.R;
import com.example.imius.activity.LoginActivity;
import com.example.imius.adapter.ViewPagerLibrary;
import com.example.imius.data.DataLocalManager;
import com.example.imius.databinding.FragmentLibraryBinding;
import com.google.android.material.tabs.TabLayout;

public class LibraryFragment extends Fragment {

    private FragmentLibraryBinding binding;
    ViewPager viewPager;
    TabLayout tabLayout;

    public static LibraryFragment newInstance() {
        LibraryFragment fragment = new LibraryFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentLibraryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
   //     checkLogin();
        init();
        return view;
    }

    private void checkLogin (){
        if (!DataLocalManager.getCheckLogin()){
            DataLocalManager.setCheckFromLibrary(true);
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    private void init(){

        viewPager = binding.fragmentLibraryViewPager;
        tabLayout = binding.fragmentLibraryTabLayout;

       // checkLogin();

        ViewPagerLibrary viewPagerLibrary = new ViewPagerLibrary(getChildFragmentManager());
        viewPagerLibrary.addFragment(new LibraryPlaylistFragment(), getString(R.string.playlist));
        viewPagerLibrary.addFragment(new FavoriteFragment(), getString(R.string.favorite));
        viewPagerLibrary.addFragment(new HistoryFragment(), getString(R.string.history));

        viewPager.setAdapter(viewPagerLibrary);
        tabLayout.setupWithViewPager(viewPager);

        binding.fragmentLibraryIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          //      checkLogin();
                callDialogAddToLibraryPlaylist();
            }
        });
    }

    private void callDialogAddToLibraryPlaylist(){
        DialogFragment dialogFragment =DialogAddToLibraryPlaylist.newInstance();

        Bundle bundle =  new Bundle();
        bundle.putString(getString(R.string.check_update),getString(R.string.insert));

        dialogFragment.setArguments(bundle);

        dialogFragment.show(getActivity().getSupportFragmentManager(), "DialogAddToLibraryPlaylist");
    }

}