package com.example.imius.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.imius.model.BaseResponse;
import com.example.imius.model.User;
import com.example.imius.repository.UserRepository;

import retrofit2.Call;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);

        userRepository = new UserRepository();
    }

    public Call<BaseResponse> login (User user){
        return userRepository.login(user);
    }

    public Call<BaseResponse> signup (User user) {
        return userRepository.signup(user);
    }

    public Call<BaseResponse> checkUsername (String username) {
        return userRepository.checkUsername(username);
    }

    public Call<BaseResponse> checkEmail(String email){
        return userRepository.checkEmail(email);
    }

    public Call<BaseResponse> updatePassword(User user){
        return userRepository.updatePassword(user);
    }

    public Call<BaseResponse> updateName(String name, String username){
        return userRepository.updateName(name, username);
    }

}
