package com.example.imius.fragment;

import static android.graphics.Color.TRANSPARENT;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.imius.R;
import com.example.imius.data.DataLocalManager;
import com.example.imius.databinding.DialogUpdateProfileBinding;
import com.example.imius.model.BaseResponse;
import com.example.imius.network.AppUtil;
import com.example.imius.viewmodel.UserViewModel;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileDialog extends DialogFragment{

    private DialogUpdateProfileBinding binding;
    private UserViewModel viewModel;

    public static UpdateProfileDialog newInstance(){
        return new UpdateProfileDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogUpdateProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        loadData();
        init();
        setCancelable(false);

        return view;
    }

    public void init(){

        binding.dialogUpdateProfileEtUsername.setText(DataLocalManager.getNameData());

        binding.dialogUpdateProfileIvChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.dialogUpdateProfileBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        binding.dialogUpdateProfileTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public boolean checkInput(){
        resetError();

        if (TextUtils.isEmpty(binding.dialogUpdateProfileEtUsername.getText().toString().trim())){
            binding.dialogUpdateProfileEtUsername.setError(getResources().getString(R.string.require));
            return false;
        }

        return true;
    }

    public void resetError(){
        binding.dialogUpdateProfileEtUsername.setError(null);
    }

    public void updateProfile(){
        if (!checkInput())
            return;

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getResources().getString(R.string.progressbar_tittle));
        progressDialog.setMessage(getResources().getString(R.string.progressbar_login));
        progressDialog.setCancelable(false);
        progressDialog.show();

        viewModel.updateName(binding.dialogUpdateProfileEtUsername.getText().toString()
                , DataLocalManager.getUsernameData()).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                BaseResponse baseResponse = response.body();
                if (baseResponse != null){
                    if (baseResponse.getIsSuccess().equals("1")){
                        DataLocalManager.setNameData(binding.dialogUpdateProfileEtUsername.getText().toString());

                    //    dismiss();
                        StyleableToast.makeText(getContext(), getString(R.string.update_success),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                        progressDialog.dismiss();

                        callProfileFragment();

                     //   callProfileFragment();
                        StyleableToast.makeText(getContext(), getString(R.string.update_success),
                                Toast.LENGTH_LONG, R.style.myToast).show();
                        progressDialog.dismiss();
                        dismiss();

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
                progressDialog.dismiss();
            }
        });

    }

    private void callProfileFragment(){
        ProfileFragment profileFragment = new ProfileFragment();
        dismiss();

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_profile_frame_content, profileFragment);
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();

    //    loadData();

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
