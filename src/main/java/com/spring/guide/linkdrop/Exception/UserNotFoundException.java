package com.spring.guide.linkdrop.Exception;

public class UserNotFoundException extends RuntimeException{



    public UserNotFoundException(String message) {
        super(message);
    }
}
