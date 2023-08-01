package com.mcu.examportal.service;

import com.mcu.examportal.model.LoginModel;
import com.mcu.examportal.model.UserModel;

import java.util.List;


public interface IUserService {
    public UserModel registerUser(UserModel userModel);
    public UserModel getUser(String userEmail);

    public String deleteUser(String email);
    public UserModel updateUserInfo(UserModel user);
    public List<UserModel> allUser();

    public boolean login(LoginModel loginInfo);

    public String forget(String email);
}
