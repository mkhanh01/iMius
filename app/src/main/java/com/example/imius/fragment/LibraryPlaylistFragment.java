package com.example.imius.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.imius.activity.SignUpActivity;
import com.example.imius.adapter.LibraryPlaylistAdapter;
import com.example.imius.constants.Constants;
import com.example.imius.databinding.FragmentLibraryPlaylistBinding;
import com.example.imius.model.BaseResponse;
import com.example.imius.model.LibraryPlaylist;
import com.example.imius.network.AppUtil;
import com.example.imius.viewmodel.LibraryPlaylistViewModel;

import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryPlaylistFragment extends Fragment {

    private FragmentLibraryPlaylistBinding binding;
    private LibraryPlaylistViewModel viewModel;
    private LibraryPlaylistAdapter adapter;
    public static LibraryPlaylistFragment newInstance() {
        LibraryPlaylistFragment fragment = new LibraryPlaylistFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLibraryPlaylistBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadData();

        initView();

        return view;
    }

    private void initView (){
        binding.fragmentLibraryPlaylistRvPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LibraryPlaylistAdapter(this.getContext());
        binding.fragmentLibraryPlaylistRvPlaylist.setAdapter(adapter);

        viewModel = new ViewModelProvider(getActivity()).get(LibraryPlaylistViewModel.class);
        viewModel.getListLibraryPlaylist().observe(getViewLifecycleOwner(), libraryPlaylists -> {
            adapter.setPlaylistLibraryList(libraryPlaylists);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                List<LibraryPlaylist> libraryPlaylistList = adapter.getPlaylistLibraryList();
                LibraryPlaylist libraryPlaylist = libraryPlaylistList.get(position);

                if (direction == ItemTouchHelper.LEFT){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle(getString(R.string.title_dialog));
                    builder.setMessage(getString(R.string.dialog_delete_message));
                    builder.setCancelable(false);

                    builder.setPositiveButton(getString(R.string.yes_dialog),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteLibraryPlaylist(libraryPlaylist.getIdLibraryPlaylist());
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
                else {
                    callDialogAddToLibraryPlaylist(getString(R.string.update), libraryPlaylist.getNameLibraryPlaylist());
                    viewModel.refreshLiveData();
                }
            }
        }).attachToRecyclerView(binding.fragmentLibraryPlaylistRvPlaylist);
    }

    private void deleteLibraryPlaylist (int idLibraryPlaylist){
        viewModel.deleteLibraryPlaylist(idLibraryPlaylist).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.body() != null){
                    if (response.body().getIsSuccess().equals(Constants.successfully)){
                        StyleableToast.makeText(getContext(), getString(R.string.library_playlist_delete_success),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                        viewModel.refreshLiveData();
                    }else {
                        StyleableToast.makeText(getContext(), getString(R.string.library_playlist_delete_failed),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                StyleableToast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG,
                        R.style.myToast).show();
            }
        });
    }



    private void callDialogAddToLibraryPlaylist(String str, String nameLibraryPlaylist){
        DialogFragment dialogFragment = DialogAddToLibraryPlaylist.newInstance();

        Bundle bundle =  new Bundle();
        bundle.putString(getString(R.string.check_update),str);
        bundle.putString(getString(R.string.nameLibraryPlaylist), nameLibraryPlaylist);

        dialogFragment.setArguments(bundle);
        dialogFragment.show(getActivity().getSupportFragmentManager(), "DialogAddToLibraryPlaylist");
    }

    private void callPlaylistActivity (){
        Intent intent = new Intent(getContext(), SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
   //     loadData();
        viewModel.refreshLiveData();
    }

    private void loadData() {
        if (!AppUtil.isNetworkAvailable(getContext())) {
            DialogFragment dialogFragment = NoInternetDialog.newInstance();
            dialogFragment.show(getActivity().getSupportFragmentManager(), "NoInternetDialog");
        }
    }

}