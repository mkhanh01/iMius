package com.example.imius.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.imius.activity.LoginActivity;
import com.example.imius.constants.Constants;
import com.example.imius.data.DataLocalManager;
import com.example.imius.databinding.FragmentChangePasswordBinding;

import com.example.imius.R;
import com.example.imius.model.BaseResponse;
import com.example.imius.model.User;
import com.example.imius.network.AppUtil;
import com.example.imius.viewmodel.UserViewModel;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment{

    private FragmentChangePasswordBinding binding;
    private UserViewModel viewModel;

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();


        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadData();

        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        binding.fragmentChangePasswordEtUsername.setText(DataLocalManager.getUsernameData());

        binding.fragmentChangePasswordBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.fragmentChangePasswordEtPassword.getText().toString().equals(DataLocalManager.getPassword())){
                    if (binding.fragmentChangePasswordEtConfirmNewPassword.getText().toString()
                            .equals(binding.fragmentChangePasswordEtNewPassword.getText().toString())){
                        changePassword();
                    } else {
                        resetError();
                        binding.fragmentChangePasswordTilConfirmNewPassword.setError(getString(R.string.compare_password_require));
                    }

                } else {
                    resetError();
                    binding.fragmentChangePasswordTilPassword.setError(getString(R.string.password_error));
                }
            }
        });

        binding.fragmentChangePasswordTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callProfileFragment();

            }
        });

        return view;
    }

    public boolean checkInput(){

        resetError();

        if (TextUtils.isEmpty(binding.fragmentChangePasswordEtPassword.getText().toString().trim())){
            binding.fragmentChangePasswordTilPassword.setError(getResources().getString(R.string.require));
            return false;
        }

        if (TextUtils.isEmpty(binding.fragmentChangePasswordEtNewPassword.getText().toString().trim())){
            binding.fragmentChangePasswordTilNewPassword.setError(getResources().getString(R.string.require));
            return false;
        }

        if (TextUtils.isEmpty(binding.fragmentChangePasswordEtConfirmNewPassword.getText().toString().trim())){
            binding.fragmentChangePasswordTilConfirmNewPassword.setError(getResources().getString(R.string.require));
            return false;
        }

        return true;
    }

    private void changePassword(){
        if (!checkInput()){
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getResources().getString(R.string.progressbar_tittle));
        progressDialog.setMessage(getResources().getString(R.string.progressbar_login));
        progressDialog.setCancelable(false);
        progressDialog.show();

        final User user = new User(binding.fragmentChangePasswordEtUsername.getText().toString(),
                binding.fragmentChangePasswordEtConfirmNewPassword.getText().toString());

        viewModel.updatePassword(user).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null){
                    if (baseResponse.getIsSuccess().equals(Constants.successfully)){

                        DataLocalManager.setPassword(user.getPassword());

                    //    callProfileFragment();

                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);

                        StyleableToast.makeText(getContext(), getString(R.string.update_success),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                        progressDialog.dismiss();

                    } else {
                        StyleableToast.makeText(getContext(), baseResponse.getMessage(),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                StyleableToast.makeText(getContext(), t.getMessage(),
                        Toast.LENGTH_LONG, R.style.myToast).show();
                progressDialog.dismiss();
            }
        });

    }

    public void callProfileFragment(){

        ProfileFragment profileFragment = new ProfileFragment();
        binding.fragmentChangePasswordLinearlayout2.setVisibility(View.GONE);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_change_password_linearlayout, profileFragment);

        transaction.commit();

    }

    public void resetError(){
        binding.fragmentChangePasswordEtUsername.setError(null);
        binding.fragmentChangePasswordEtPassword.setError(null);
        binding.fragmentChangePasswordEtNewPassword.setError(null);
        binding.fragmentChangePasswordEtConfirmNewPassword.setError(null);
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