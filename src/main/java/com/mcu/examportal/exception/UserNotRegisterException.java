package com.mcu.examportal.exception;

public class UserNotRegisterException extends RuntimeException{
    public UserNotRegisterException(){}
    public UserNotRegisterException(String message){super(message);}
}
