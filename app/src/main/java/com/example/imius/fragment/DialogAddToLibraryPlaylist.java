package com.example.imius.fragment;

import static android.graphics.Color.TRANSPARENT;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.imius.R;
import com.example.imius.constants.Constants;
import com.example.imius.databinding.DialogAddToLibraryPlaylistBinding;
import com.example.imius.model.BaseResponse;
import com.example.imius.model.LibraryPlaylist;
import com.example.imius.network.AppUtil;
import com.example.imius.viewmodel.LibraryPlaylistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogAddToLibraryPlaylist extends DialogFragment {

    private DialogAddToLibraryPlaylistBinding binding;
    private LibraryPlaylistViewModel viewModel;

    public static DialogAddToLibraryPlaylist newInstance() {
        return new DialogAddToLibraryPlaylist();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DialogAddToLibraryPlaylistBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadData();

        initView();

        return view;
    }

    private void initView (){

        viewModel = new ViewModelProvider(getActivity()).get(LibraryPlaylistViewModel.class);

        if (getArguments().getString(getString(R.string.check_update)).equals(getString(R.string.update))){
            binding.dialogAddToLibraryPlaylistTvContent.setText(getString(R.string.enter_new_name_for_your_playlist));
            binding.dialogAddToLibraryPlaylistEtPlaylistName.setText(getArguments()
                    .getString(getString(R.string.nameLibraryPlaylist)));
            binding.dialogAddToLibraryPlaylistBtnEnter.setText(getString(R.string.update_name_playlist));
        }

        binding.dialogAddToLibraryPlaylistIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.dialogAddToLibraryPlaylistBtnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()){
                    if (getArguments().getString(getString(R.string.check_update)).equals(getString(R.string.update))){
                        updateLibraryPlaylistName();
                    }else {
                        insertLibraryPlaylist();
                    }
                }else {
                    return;
                }
            }
        });
    }

    private boolean checkInput (){
        List<LibraryPlaylist> list = new ArrayList<>();

        binding.dialogAddToLibraryPlaylistTilPlaylistName.setError(null);
        if (binding.dialogAddToLibraryPlaylistEtPlaylistName.getText().toString().trim().isEmpty()){
            binding.dialogAddToLibraryPlaylistTilPlaylistName.setError(getString(R.string.require));
            return false;
        }

        viewModel.getListLibraryPlaylist().observe(getViewLifecycleOwner(), libraryPlaylists -> {
            list.addAll(libraryPlaylists);
        });

        for (LibraryPlaylist libraryPlaylist : list){
            if (binding.dialogAddToLibraryPlaylistEtPlaylistName.getText().toString()
                    .equals(libraryPlaylist.getNameLibraryPlaylist())){
                binding.dialogAddToLibraryPlaylistTilPlaylistName
                        .setError(getString(R.string.library_playlist_name_exits));
                return false;
            }
        }
        return true;
    }
    private void insertLibraryPlaylist (){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getResources().getString(R.string.progressbar_tittle));
        progressDialog.setCancelable(false);
        progressDialog.show();

        viewModel.insertLibraryPlaylist(binding.dialogAddToLibraryPlaylistEtPlaylistName
                .getText().toString().trim()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.body() != null){
                    if (response.body().getIsSuccess().equals(Constants.successfully)){
                        StyleableToast.makeText(getContext(), getString(R.string.library_playlist_insert_success),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                        progressDialog.dismiss();
                        getDialog().dismiss();
                        viewModel.refreshLiveData();


                    }else{
                        StyleableToast.makeText(getContext(), getString(R.string.library_playlist_insert_failed),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                        binding.dialogAddToLibraryPlaylistTilPlaylistName.setError(" ");
                        progressDialog.dismiss();
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

    private void updateLibraryPlaylistName (){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getResources().getString(R.string.progressbar_tittle));
        progressDialog.setCancelable(false);
        progressDialog.show();

        viewModel.updateLibraryPlaylistName(getArguments().getString(getString(R.string.nameLibraryPlaylist)),
                binding.dialogAddToLibraryPlaylistEtPlaylistName
                .getText().toString().trim()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.body() != null){
                    if (response.body().getIsSuccess().equals(Constants.successfully)){
                        StyleableToast.makeText(getContext(), getString(R.string.library_playlist_update_success),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                        progressDialog.dismiss();
                        getDialog().dismiss();
                        viewModel.refreshLiveData();


                    }else{
                        StyleableToast.makeText(getContext(), getString(R.string.library_playlist_update_failed),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                        binding.dialogAddToLibraryPlaylistTilPlaylistName.setError(" ");
                        progressDialog.dismiss();
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

    @Override
    public void onResume() {
        super.onResume();
     //   loadData();
        viewModel.refreshLiveData();

        ViewGroup.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) layoutParams);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

    }

    private void loadData() {
        if (!AppUtil.isNetworkAvailable(getContext())) {
            DialogFragment dialogFragment = NoInternetDialog.newInstance();
            dialogFragment.show(getActivity().getSupportFragmentManager(), "NoInternetDialog");
        }
    }
}
