package com.wcc.cc.codingchallenge.rest;

import com.wcc.cc.codingchallenge.exception.IllegalPostcodeUpdateException;
import com.wcc.cc.codingchallenge.exception.PostcodeNotFoundException;
import com.wcc.cc.codingchallenge.exception.ValidationException;
import com.wcc.cc.codingchallenge.rest.obj.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Controller Advice to handle exception response.
 */
@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Handle postcode not found.
     * Used when postcode is not found from the database.
     * @param e PostcodeNotFoundException
     * @return ResponseEntity with HTTP Status 404
     */
    @ExceptionHandler(PostcodeNotFoundException.class)
    public ResponseEntity handleExceptionPostcodeNotFound(PostcodeNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
    }

    /**
     * Handle postcode update failure due to input error.
     * @param e IllegalPostcodeUpdateException
     * @return ResponseEntity with HTTP Status 400
     */
    @ExceptionHandler(IllegalPostcodeUpdateException.class)
    public ResponseEntity handleExceptionIllegalPostcodeUpdate(IllegalPostcodeUpdateException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }

    /**
     * Handle form validation failure
     * @param e ValidationException
     * @return ResponseEntity with HTTP Status 400
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleMethodArgumentNotValidException(ValidationException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
    }
}