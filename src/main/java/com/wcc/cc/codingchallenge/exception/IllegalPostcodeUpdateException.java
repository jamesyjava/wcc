package com.wcc.cc.codingchallenge.exception;
/**
 * Exception class to handle illegal update operation on postcode.
 */
public class IllegalPostcodeUpdateException extends Exception {
    public IllegalPostcodeUpdateException(String errorMessage) {
        super(errorMessage);
    }
}