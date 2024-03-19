package com.wcc.cc.codingchallenge.exception;

/**
 * Exception class to handle postcode not found from database.
 */
public class PostcodeNotFoundException extends Exception{
    public PostcodeNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
