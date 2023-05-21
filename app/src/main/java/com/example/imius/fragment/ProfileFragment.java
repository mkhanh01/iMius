package com.example.imius.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.imius.activity.LoginActivity;
import com.example.imius.data.DataLocalManager;
import com.example.imius.databinding.FragmentProfileBinding;

import com.example.imius.R;
import com.example.imius.network.AppUtil;

public class ProfileFragment extends Fragment{
    public FragmentProfileBinding binding;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        loadData();

        init();
        binding.fragmentProfileTvUsername.setText(DataLocalManager.getNameData());
        return view;
    }


    public void init(){
        binding.fragmentProfileTvUsername.setText(DataLocalManager.getNameData());
        if (DataLocalManager.getNameData().equals("")){
            binding.fragmentProfileTvUsername.setText(DataLocalManager.getUsernameData());
        } else {
            binding.fragmentProfileTvUsername.setText(DataLocalManager.getNameData());
        }

        if (!DataLocalManager.getCheckLogin()){
            binding.fragmentProfileBtnLogout.setText(getString(R.string.login));
            binding.fragmentProfileTvChangePassword.setVisibility(View.GONE);
            binding.fragmentProfileCivAvatar.setVisibility(View.GONE);
            binding.fragmentProfileTvUsername.setVisibility(View.GONE);
        }

        binding.fragmentProfileTvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callUpdateProfileDialog();
                binding.fragmentProfileTvUsername.setText(DataLocalManager.getNameData());

            }
        });

        binding.fragmentProfileCivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.fragmentProfileTvUsername.setText(DataLocalManager.getNameData());
                callUpdateProfileDialog();

                reloadFragment();

                binding.fragmentProfileTvUsername.setText(DataLocalManager.getNameData());

            }
        });

        binding.fragmentProfileIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callUpdateProfileDialog();

                reloadFragment();

                binding.fragmentProfileTvUsername.setText(DataLocalManager.getNameData());

            }
        });

        binding.fragmentProfileBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DataLocalManager.getCheckLogin()){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    showDialogLogout();
                }
            }
        });

        binding.fragmentProfileTvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callChangePasswordFragment();
            }
        });

    }

    public void callUpdateProfileDialog() {
        DialogFragment updateProfileDialog = UpdateProfileDialog.newInstance();
        updateProfileDialog.show(getActivity().getSupportFragmentManager(),"Update Profile Dialog");
    }

    public void callChangePasswordFragment(){

        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
        binding.fragmentProfileConstraintLayout.setVisibility(View.GONE);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_profile_frame_content, changePasswordFragment);
        transaction.commit();
    }

    public void reloadFragment(){
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(this);
        ft.attach(this);
        ft.commit();
    }

    private void showDialogLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(getString(R.string.title_dialog));
        builder.setMessage(getString(R.string.dialog_logout));
        builder.setCancelable(false);

        builder.setPositiveButton(getString(R.string.yes_dialog),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        DataLocalManager.clearDataLocal();
                        DataLocalManager.setFirstInstall(true);
                        DataLocalManager.setCheckFromLogout(true);
                        getActivity().finish();
                        startActivity(intent);
                    }
                });

        builder.setNegativeButton(getString(R.string.no_dialog),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadData() {
        if (!AppUtil.isNetworkAvailable(getContext())) {
            DialogFragment dialogFragment = NoInternetDialog.newInstance();
            dialogFragment.show(getActivity().getSupportFragmentManager(), "NoInternetDialog");
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        loadData();
//    }
}