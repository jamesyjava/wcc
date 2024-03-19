package com.wcc.cc.codingchallenge.rest.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Object to accept post request for get 2 postcode distance.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostcodeDistanceForm {
    @NotBlank(message = "Please provide postcode1")
    private String postcode1;
    @NotBlank(message = "Please provide postcode2")
    private String postcode2;
}
