package com.example.demo.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserNotFoundException extends RuntimeException{
    private static final Logger logger = LoggerFactory.getLogger(UserNotFoundException.class);

    public UserNotFoundException(String message) {
        super(message);
        logger.error(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.error(message, cause);
    }
}