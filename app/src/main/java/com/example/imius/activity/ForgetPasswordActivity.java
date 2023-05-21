package com.example.imius.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.imius.constants.Constants;
import com.example.imius.data.DataLocalManager;
import com.example.imius.databinding.ActivityForgetPasswordBinding;

import com.example.imius.R;
import com.example.imius.fragment.ChangePasswordFragment;
import com.example.imius.fragment.ForgetPasswordFragment;
import com.example.imius.fragment.NoInternetDialog;
import com.example.imius.model.BaseResponse;
import com.example.imius.network.AppUtil;
import com.example.imius.viewmodel.UserViewModel;

import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity{

    private ActivityForgetPasswordBinding binding;
    private UserViewModel viewModel;
    private int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        loadData();

        binding.activityForgetPasswordEtEmail.setText(DataLocalManager.getEmail());

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        init();
    }

    public void init(){
        binding.activityForgetPasswordCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.activityForgetPasswordBtnGetCode.setOnClickListener(v -> {
            resetError();
            if (checkEmailForm()){
                viewModel.checkEmail(binding.activityForgetPasswordEtEmail.getText().toString().trim())
                        .enqueue(new Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                BaseResponse baseResponse = response.body();
                                if (baseResponse != null){
                                    if(baseResponse.getIsSuccess().equals(Constants.failed)){
                                        binding.activityForgetPasswordTilEmail.setError(getResources().getString(R.string.email_not_match));
                                    }else {
                                        sendEmail();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {

                            }
                        });
            }

        });


        binding.activityForgetPasswordBtnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callForgetPasswordFragment();

            }
        });
    }

    private void sendEmail(){
        resetError();
        Random random = new Random();
        code = random.nextInt(8999)+1000;

        String stringHost = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", stringHost);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("imiusg@gmail.com",
                        getResources().getString(R.string.password_email_server));
            }
        });

        MimeMessage mimeMessage = new MimeMessage(session);

        try {
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(
                    binding.activityForgetPasswordEtEmail.getText().toString().trim()));

            mimeMessage.setSubject(getResources().getString(R.string.subject_email));
            mimeMessage.setText(getResources().getString(R.string.hello_email)+ "\n\n" +
                    getResources().getString(R.string.content_email) +  code + "\n\n" +
                    getResources().getString(R.string.end_email));

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Transport.send(mimeMessage);
                        javax.mail.Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            thread.start();

            StyleableToast.makeText(ForgetPasswordActivity.this,
                    getResources().getString(R.string.check_code),
                    Toast.LENGTH_LONG, R.style.myToast).show();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkInput(){
        resetError();

        if (TextUtils.isEmpty(binding.activityForgetPasswordEtEmail.getText().toString().trim())){
            binding.activityForgetPasswordTilEmail.setError(getResources().getString(R.string.require));
            return false;
        } else if (!Pattern.matches(getResources().getString(R.string.email_pattern),
                binding.activityForgetPasswordEtEmail.getText().toString().trim())) {
            binding.activityForgetPasswordTilEmail.setError(getResources().getString(R.string.format_error));
            return false;
        } else if (binding.activityForgetPasswordEtEmail.getText().toString().trim().equals(DataLocalManager.getEmail())){
            binding.activityForgetPasswordTilEmail.setError(getResources().getString(R.string.email_not_match));
        }

        if (TextUtils.isEmpty(binding.activityForgetPasswordEtConfirmCode.getText().toString().trim())){
            binding.activityForgetPasswordTilConfirmCode.setError(getResources().getString(R.string.require));
            return false;
        }else{
            try {
                if(Integer.parseInt(binding.activityForgetPasswordEtConfirmCode.getText().toString().trim()) != code) {
                    binding.activityForgetPasswordTilConfirmCode.setError(getResources().getString(R.string.code_error));
                    return false;
                }
            }catch (Exception e){
                binding.activityForgetPasswordTilConfirmCode.setError(getResources().getString(R.string.number_error));
                return false;
            }
        }
        return true;
    }

    private boolean checkEmailForm(){
        if (TextUtils.isEmpty(binding.activityForgetPasswordEtEmail.getText().toString().trim())){
            binding.activityForgetPasswordTilEmail.setError(getResources().getString(R.string.require));
            return false;
        } else if (!Pattern.matches(getResources().getString(R.string.email_pattern),
                binding.activityForgetPasswordEtEmail.getText().toString().trim())) {
            binding.activityForgetPasswordTilEmail.setError(getResources().getString(R.string.format_error));
            return false;
        }
        return true;
    }

    private boolean checkEmail(){


        return true;

    }

    private void resetError(){
        binding.activityForgetPasswordTilEmail.setError(null);
        binding.activityForgetPasswordTilConfirmCode.setError(null);
    }

    public void callForgetPasswordFragment() {
        if (!checkInput())
            return;

        ForgetPasswordFragment forgetPasswordFragment = new ForgetPasswordFragment();
        binding.activityForgetPasswordLinearLayout.setVisibility(View.GONE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_forget_password_frame_content, forgetPasswordFragment);
        transaction.commit();

    }

    private void loadData() {
        if (!AppUtil.isNetworkAvailable(this)) {
            DialogFragment dialogFragment = NoInternetDialog.newInstance();
            dialogFragment.show(getSupportFragmentManager(), "NoInternetDialog");
        }
    }


}