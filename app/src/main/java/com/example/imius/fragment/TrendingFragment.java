package com.example.imius.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.imius.adapter.TrendingAdapter;
import com.example.imius.databinding.FragmentTrendingBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.imius.viewmodel.TrendingViewModel;


public class TrendingFragment extends Fragment {
    private FragmentTrendingBinding binding;
    private TrendingAdapter trendingAdapter;
    private TrendingViewModel viewModel;

    public static TrendingFragment newInstance() {
        TrendingFragment fragment = new TrendingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTrendingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        binding.fragmentTrendingRvTrending.setLayoutManager(linearLayoutManager);

        trendingAdapter = new TrendingAdapter(this.getContext());
        binding.fragmentTrendingRvTrending.setAdapter(trendingAdapter);

        viewModel = new ViewModelProvider(getActivity()).get(TrendingViewModel.class);
        viewModel.getTrending().observe(getViewLifecycleOwner(), trendingList -> {
            trendingAdapter.setTrendingList(trendingList);
        });

        viewModel.refreshLiveData();
        return view;
    }

}