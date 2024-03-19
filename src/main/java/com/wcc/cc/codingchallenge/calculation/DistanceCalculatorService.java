package com.wcc.cc.codingchallenge.calculation;

import com.wcc.cc.codingchallenge.entity.PostCodeLatLng;
import com.wcc.cc.codingchallenge.exception.PostcodeNotFoundException;
import com.wcc.cc.codingchallenge.repository.PostCodeLatLngRepository;
import com.wcc.cc.codingchallenge.rest.obj.Distance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to perform calculation of distances between 2 postcode.
 */
@Service
public class DistanceCalculatorService {

    @Autowired
    private PostCodeLatLngRepository postCodeLatLngRepository;

    private final static double EARTH_RADIUS = 6371; // radius in kilometers

    /**
     *
     * @param postcode1 first postcode to search
     * @param postcode2 second postcode to search
     * @return distance. {@see distance}
     * @throws PostcodeNotFoundException
     */
    public Distance getDistance(String postcode1, String postcode2) throws PostcodeNotFoundException {

        PostCodeLatLng postCodeLatLng1 = postCodeLatLngRepository.findByPostcode(postcode1);
        PostCodeLatLng postCodeLatLng2 = postCodeLatLngRepository.findByPostcode(postcode2);

        if (postCodeLatLng1 == null || postCodeLatLng2 == null) {
            throw new PostcodeNotFoundException("At least 1 of the postcode not found");
        }
        return distance(postCodeLatLng1, postCodeLatLng2);
    }

    /**
     *
     * @param postCodeLatLng1 first {@link com.wcc.cc.codingchallenge.entity.PostCodeLatLng  PostCodeLatLng1}
     * @param postCodeLatLng2 second {@link com.wcc.cc.codingchallenge.entity.PostCodeLatLng  PostCodeLatLng2}
     * @return Distance object containing first and second PostCodeLatLng Object and distances between the 2 in km.
     */
    private Distance distance(PostCodeLatLng postCodeLatLng1, PostCodeLatLng postCodeLatLng2) {
        double distanceInKM = calculateDistance(postCodeLatLng1.getLatitude(),
                postCodeLatLng1.getLongitude(),
                postCodeLatLng2.getLatitude(),
                postCodeLatLng2.getLongitude());
        return new Distance(String.format("%.3f km",distanceInKM), postCodeLatLng1, postCodeLatLng2);
    }

    /**
     * Copied method.
     * @param latitude first postcode latitude
     * @param longitude first postcode longitude
     * @param latitude2 second postcode latitude
     * @param longitude2 second postcode longitude
     * @return distance.
     */
    public double calculateDistance(double latitude, double longitude, double latitude2, double
            longitude2) {
        // Using Haversine formula! See Wikipedia;
        double lon1Radians = Math.toRadians(longitude);
        double lon2Radians = Math.toRadians(longitude2);
        double lat1Radians = Math.toRadians(latitude);
        double lat2Radians = Math.toRadians(latitude2);
        double a = haversine(lat1Radians, lat2Radians)
                + Math.cos(lat1Radians) * Math.cos(lat2Radians) * haversine(lon1Radians, lon2Radians);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (EARTH_RADIUS * c);
    }

    /**
     * Copied method.
     * @param deg1
     * @param deg2
     * @return
     */
    private double haversine(double deg1, double deg2) {
        return square(Math.sin((deg1 - deg2) / 2.0));
    }

    /**
     * copied method
     * @param x
     * @return
     */
    private double square(double x) {
        return x * x;
    }
}
