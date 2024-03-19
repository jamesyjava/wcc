package com.wcc.cc.codingchallenge.rest.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Object for update postcode.
 */
@Setter
@Getter
@AllArgsConstructor
public class PostcodeForm {
    private String postcode;
    private double latitude;
    private double longitude;
}
