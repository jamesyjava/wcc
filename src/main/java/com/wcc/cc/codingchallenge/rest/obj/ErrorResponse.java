package com.wcc.cc.codingchallenge.rest.obj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Object to return error message when exception happens.
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private String errorMessage;
}
