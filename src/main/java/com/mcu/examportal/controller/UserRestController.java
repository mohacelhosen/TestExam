package com.mcu.examportal.controller;

import com.mcu.examportal.exception.UserNotFoundException;
import com.mcu.examportal.model.LoginModel;
import com.mcu.examportal.model.UserModel;
import com.mcu.examportal.service.IUserService;
import com.mcu.examportal.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/exam-portal")
public class UserRestController {
    Logger logger = LoggerFactory.getLogger(UserRestController.class);
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private IUserService userService;

    //    <--------------------- register the user ------------------->
    @PostMapping("/signup")
    public ResponseEntity<UserModel> signUp(@RequestBody UserModel userModel) {
        logger.info("UserRestController::signUp, 🤖Model=>"+userModel.toString());
        UserModel registerUser = userService.registerUser(userModel);
        logger.info("UserRestController:signUp, 🤖model::" + registerUser.toString());

        // Generate the JWT token with roles information
        String jwtToken = jwtService.generateToken(registerUser.getEmail());
        logger.info("UserRestController::signup, ⭕Token::"+jwtToken);
        return new ResponseEntity<>(registerUser, HttpStatus.CREATED);
    }

    //    <-------------------- Find user by Email ------------------>
    @GetMapping("/find/{userEmail}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','TEACHER')")
    public ResponseEntity<UserModel> findUserByEmail(@PathVariable String userEmail) {
        UserModel user = userService.getUser(userEmail);
        logger.info("✅UserRestController:findUserByEmail, user::" + user.toString());
        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }

    //    <-------------------- update user information ------------------>
    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','TEACHER')")
    public ResponseEntity<UserModel> updateUserInfo(@RequestBody UserModel user) {
        UserModel updateUserInfo = userService.updateUserInfo(user);
        logger.info("✅UserRestController:updateUserInfo, model::" + user.toString());
        return new ResponseEntity<>(updateUserInfo, HttpStatus.OK);
    }

    //    <-------------------- update user information ------------------>
    @DeleteMapping("/delete/{userEmail}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','TEACHER')")
    public ResponseEntity<String> deleteUser(@PathVariable String userEmail) {
        String message = userService.deleteUser(userEmail);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //    <------------------------ get all user info --------------------->

    @GetMapping("/users/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserModel>> allUsers() {
        List<UserModel> userModels = userService.allUser();
        logger.info("✅UserRestController:allUsers");
        return new ResponseEntity<>(userModels, HttpStatus.OK);
    }

    //    <---------------------- login -------------------------->
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginModel loginInfo) {
        boolean login = userService.login(loginInfo);
        String message = null;
        if (login) {
            message = "login Successful😇";
        } else {
            message = "login Fail😵";
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //    <----------- forget password ---------->
    @GetMapping("/forget/{email}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','TEACHER')")
    public ResponseEntity<String> forgetPassword(@PathVariable String email) {
        String forget = userService.forget(email);
        return new ResponseEntity<>(forget, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody LoginModel authRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()){
            return  jwtService.generateToken(authRequest.getEmail());
        }
        throw new UserNotFoundException("Invalid email or password!");
    }


}
