package com.wcc.cc.codingchallenge.exception;

/**
 * Exception class to handle input validation failure.
 */
public class ValidationException extends Exception{
    public ValidationException(String errorMessage) {
        super(errorMessage);
    }
}
