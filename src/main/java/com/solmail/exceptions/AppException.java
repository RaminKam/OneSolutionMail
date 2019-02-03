package com.solmail.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException{
    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void error(String error) {
        throw  new AppException(error);
    }
    
    public static void error(String message, Throwable cause) {
        throw  new AppException(message, cause);
    }
}
