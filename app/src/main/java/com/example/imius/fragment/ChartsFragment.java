package com.example.imius.fragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.imius.adapter.SongAdapter;
import com.example.imius.databinding.FragmentChartsBinding;
import com.example.imius.model.Song;
import com.example.imius.network.AppUtil;
import com.example.imius.viewmodel.SongViewModel;

import java.util.ArrayList;

public class ChartsFragment extends Fragment {
    private FragmentChartsBinding binding;
    private SongAdapter songAdapter;
    private SongViewModel songViewModel;

    public static ChartsFragment newInstance(String param1, String param2) {
        ChartsFragment fragment = new ChartsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChartsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.fragmentChartsRvCharts.setLayoutManager(linearLayoutManager);

        songAdapter = new SongAdapter(this.getContext());
        binding.fragmentChartsRvCharts.setAdapter(songAdapter);

        songViewModel = new ViewModelProvider(getActivity()).get(SongViewModel.class);
        songViewModel.getSongs().observe(getViewLifecycleOwner(), songs -> {
            songAdapter.setListSongs((ArrayList<Song>) songs);
        });

        songAdapter.setCheckChart(true);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
    //    loadData();
        songViewModel.refreshLiveData();
    }

    private void loadData() {
        if (!AppUtil.isNetworkAvailable(getContext())) {
            DialogFragment dialogFragment = NoInternetDialog.newInstance();
            dialogFragment.show(getActivity().getSupportFragmentManager(), "NoInternetDialog");
        }
    }

}