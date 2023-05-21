package com.example.imius.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.imius.activity.LoginActivity;
import com.example.imius.constants.Constants;
import com.example.imius.data.DataLocalManager;
import com.example.imius.databinding.FragmentForgetPasswordBinding;

import com.example.imius.R;
import com.example.imius.model.BaseResponse;
import com.example.imius.model.User;
import com.example.imius.network.AppUtil;
import com.example.imius.viewmodel.UserViewModel;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordFragment extends Fragment{

    private FragmentForgetPasswordBinding binding;
    private UserViewModel viewModel;

    public static ForgetPasswordFragment newInstance() {
        ForgetPasswordFragment fragment = new ForgetPasswordFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        loadData();

        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        binding.fragmentForgetPasswordTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        binding.fragmentForgetPasswordBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPassword();
            }
        });

        return view;
    }

    private boolean checkInput(){
        resetError();
        if (TextUtils.isEmpty(binding.fragmentForgetPasswordEtNewPassword.getText().toString().trim())){
            binding.fragmentForgetPasswordTilNewPassword.setError(getResources().getString(R.string.require));
            return false;
        }

        if (TextUtils.isEmpty(binding.fragmentForgetPasswordEtConfirmNewPassword.getText().toString().trim())){
            binding.fragmentForgetPasswordTilConfirmNewPassword.setError(getResources().getString(R.string.require));
            return false;
        } else {
            if (! binding.fragmentForgetPasswordEtConfirmNewPassword.getText().toString().trim().equals(binding.fragmentForgetPasswordEtNewPassword.getText().toString().trim())){
                binding.fragmentForgetPasswordTilConfirmNewPassword.setError(getResources().getString(R.string.compare_password_require));
                return false;
            }
        }

        return true;
    }

    private void resetError(){
        binding.fragmentForgetPasswordTilNewPassword.setError(null);
        binding.fragmentForgetPasswordTilConfirmNewPassword.setError(null);
    }

    private void forgetPassword(){

        if (!checkInput()){
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getResources().getString(R.string.progressbar_tittle));
        progressDialog.setMessage(getResources().getString(R.string.progressbar_login));
        progressDialog.setCancelable(false);
        progressDialog.show();

        final User user = new User(DataLocalManager.getUsernameData(),
                binding.fragmentForgetPasswordEtConfirmNewPassword.getText().toString());

        viewModel.updatePassword(user).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null){
                    if (baseResponse.getIsSuccess().equals(Constants.successfully)){

                        DataLocalManager.setPassword(user.getPassword());

                        //                 callProfileFragment();

                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);

                        StyleableToast.makeText(getContext(), getString(R.string.update_success),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                        progressDialog.dismiss();

                    } else {
                        StyleableToast.makeText(getContext(), getString(R.string.update_faild),
                                Toast.LENGTH_LONG, R.style.myToast).show();
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

    public void callProfileFragment() {
        ProfileFragment profileFragment = new ProfileFragment();
        binding.fragmentForgetPasswordLinearLayout.setVisibility(View.GONE);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_forget_password_frame_content, profileFragment);
        transaction.commit();

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