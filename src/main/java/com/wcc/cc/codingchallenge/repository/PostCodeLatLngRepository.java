package com.wcc.cc.codingchallenge.repository;

import com.wcc.cc.codingchallenge.entity.PostCodeLatLng;
import org.springframework.data.repository.Repository;

/**
 * postcode repository to handle database operation.
 */
public interface PostCodeLatLngRepository extends Repository<PostCodeLatLng, Long> {

    PostCodeLatLng save(PostCodeLatLng postCodeLatLng);

    PostCodeLatLng findById(long id);

    PostCodeLatLng findByPostcode(String postcode);


}
