package com.mcu.examportal.exception;

public class IncompleteUserInfoException extends RuntimeException{
    public IncompleteUserInfoException(){}
    public IncompleteUserInfoException(String message){super(message);}
}
