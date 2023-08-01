package com.mcu.examportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotRegisterException.class)
    public  ResponseEntity<ExceptionModel> userNotRegister(UserNotRegisterException unre){
        ExceptionModel model = new ExceptionModel();
        model.setCode("DB-101");
        model.setExceptionName(unre.getMessage());
        return  new ResponseEntity<>(model, HttpStatus.ALREADY_REPORTED);
    }
    @ExceptionHandler(IncompleteUserInfoException.class)
    public  ResponseEntity<ExceptionModel> userNotRegister(IncompleteUserInfoException incompleteUserInfo){
        ExceptionModel model = new ExceptionModel();
        model.setCode("InCompleteUserInfo");
        model.setExceptionName(incompleteUserInfo.getMessage());
        return  new ResponseEntity<>(model, HttpStatus.ALREADY_REPORTED);
    }
}
