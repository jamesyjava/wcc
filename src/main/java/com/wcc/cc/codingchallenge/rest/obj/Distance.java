package com.wcc.cc.codingchallenge.rest.obj;

import com.wcc.cc.codingchallenge.entity.PostCodeLatLng;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Response object for get postcode distance.
 */
@Getter
@Setter
@AllArgsConstructor
public class Distance {
    private String distance;
    private PostCodeLatLng postcode1;
    private PostCodeLatLng postcode2;
}
