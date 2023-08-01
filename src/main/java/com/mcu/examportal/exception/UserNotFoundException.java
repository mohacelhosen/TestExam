package com.mcu.examportal.exception;

import lombok.Data;

@Data
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){}
    public UserNotFoundException(String message){super(message);}
}
