package com.example.imius.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imius.R;
import com.example.imius.adapter.FavoriteSongAdapter;
import com.example.imius.constants.Constants;
import com.example.imius.data.DataLocalManager;
import com.example.imius.databinding.FragmentFavoriteBinding;

import com.example.imius.model.BaseResponse;
import com.example.imius.model.FavoriteSong;

import com.example.imius.network.AppUtil;
import com.example.imius.repository.MusicRepository;
import com.example.imius.viewmodel.FavoriteSongViewModel;
;

import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment {


    private FragmentFavoriteBinding binding;
    private FavoriteSongViewModel viewModel;
    private FavoriteSongAdapter adapter;

    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadData();

        initView();

        return view;
    }

    private void initView() {
        binding.fragmentLibraryFavoriteRvFavorite.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FavoriteSongAdapter(this.getContext());
        binding.fragmentLibraryFavoriteRvFavorite.setAdapter(adapter);

        viewModel = new ViewModelProvider(getActivity()).get(FavoriteSongViewModel.class);
        viewModel.getFavoriteSongs().observe(getViewLifecycleOwner(), songs -> {
            adapter.setFavoriteSongs(songs);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                List<FavoriteSong> favoriteSongs = adapter.getFavoriteSongs();
                FavoriteSong song = favoriteSongs.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle(getString(R.string.title_dialog));
                builder.setMessage(getString(R.string.dialog_delete_message));
                builder.setCancelable(false);

                builder.setPositiveButton(getString(R.string.yes_dialog),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MusicRepository repository = new MusicRepository();

                                repository.updateLikeOfNumber(song.getIdSong())
                                        .enqueue(new Callback<BaseResponse>() {
                                            @Override
                                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                                if (response.body() != null) {
                                                    if (response.body().getIsSuccess().equals(Constants.successfully)) {
                                                        if (response.body().getMessage().equals(Constants.DELETE)) {
                                                            deleteLikeSong(song.getIdSong());
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                                StyleableToast.makeText(getContext(), t.getMessage(),
                                                        Toast.LENGTH_LONG, R.style.myToast).show();
                                            }
                                        });
                            }
                        });

                builder.setNegativeButton(getString(R.string.no_dialog),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                viewModel.refreshLiveData();
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }).attachToRecyclerView(binding.fragmentLibraryFavoriteRvFavorite);
    }

    public void deleteLikeSong(int idSong) {
        MusicRepository repository = new MusicRepository();
        //       repository.deleteLikeSong(DataLocalManager.getUsernameData(), idSong);

        repository.deleteLikeSong(DataLocalManager.getUsernameData(), idSong).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getIsSuccess().equals(Constants.successfully)) {
                    StyleableToast.makeText(getContext(), getString(R.string.delete_success),
                            Toast.LENGTH_LONG, R.style.myToast).show();
                    viewModel.refreshLiveData();
                } else {
                    StyleableToast.makeText(getContext(), getString(R.string.delete_failed),
                            Toast.LENGTH_LONG, R.style.myToast).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                StyleableToast.makeText(getContext(), t.getMessage(),
                        Toast.LENGTH_LONG, R.style.myToast).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
  //      loadData();
        viewModel.refreshLiveData();
    }

    private void loadData() {
        if (!AppUtil.isNetworkAvailable(getContext())) {
            DialogFragment dialogFragment = NoInternetDialog.newInstance();
            dialogFragment.show(getActivity().getSupportFragmentManager(), "NoInternetDialog");
        }
    }

}