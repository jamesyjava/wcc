package com.wcc.cc.codingchallenge.rest;

import com.wcc.cc.codingchallenge.calculation.DistanceCalculatorService;
import com.wcc.cc.codingchallenge.entity.PostCodeLatLng;
import com.wcc.cc.codingchallenge.exception.IllegalPostcodeUpdateException;
import com.wcc.cc.codingchallenge.exception.PostcodeNotFoundException;
import com.wcc.cc.codingchallenge.exception.ValidationException;
import com.wcc.cc.codingchallenge.repository.PostCodeLatLngRepository;
import com.wcc.cc.codingchallenge.rest.form.PostcodeDistanceForm;
import com.wcc.cc.codingchallenge.rest.form.PostcodeForm;
import com.wcc.cc.codingchallenge.rest.obj.Distance;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest Controller class.
 * All http endpoints configured here.
 *
 * Annotation usage:
 * @RestController used to create RESTful web service.
 * @Slf4j used to simplify logging setup.
 */
@RestController
@Slf4j
public class WCCRestController {

    /**
     * Distance calculator service that will calculate distances for 2 given postcode
     */
    @Autowired
    private DistanceCalculatorService distanceCalculatorService;

    @Autowired
    private PostCodeLatLngRepository postCodeLatLngRepository;

    /**
     *
     * @param postcodeDistanceForm postcodes receive in json format.
     * @return ResponseEntity with Distance object. Response as json to caller with http status 200
     * @throws PostcodeNotFoundException thrown if at least 1 of the postcode supplied is not found.
     *                                  @see com.wcc.cc.codingchallenge.rest.RestExceptionHandler for more info.
     */


    /**
     *
     * @param postcodeDistanceForm postcodes receive in json format.
     * @param bindingResult input validation results.
     * @return ResponseEntity with Distance object. Response as json to caller with http status 200
     * @throws PostcodeNotFoundException thrown if at least 1 of the postcode supplied is not found.
     *      *                                  @see com.wcc.cc.codingchallenge.rest.RestExceptionHandler for more info.
     * @throws ValidationException thrown if input is not valid
     *      *                                  @see com.wcc.cc.codingchallenge.rest.ValidationException for more info.
     */
    @PostMapping(value = "/distance", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Distance> getDistance(@RequestBody @Valid PostcodeDistanceForm postcodeDistanceForm,
                                                BindingResult bindingResult) throws PostcodeNotFoundException, ValidationException {

        if(bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream().reduce("Input validation failure on ", (message, fieldError) ->
                    message + " " + fieldError.getField() + " " + fieldError.getDefaultMessage(), String::concat
            );
            throw new ValidationException(errorMessage);
        }

        Distance distance = distanceCalculatorService.getDistance(postcodeDistanceForm.getPostcode1(),
                                                                postcodeDistanceForm.getPostcode2());

        //Add self link into postcode object
        distance.getPostcode1().add(buildPostcodeLinkWithName(distance.getPostcode1().getId(), "postcode1"));
        distance.getPostcode2().add(buildPostcodeLinkWithName(distance.getPostcode2().getId(), "postcode2"));

        log.info("Postcode search postcode1={} and postcode2={} distance={}",
                postcodeDistanceForm.getPostcode1(),
                postcodeDistanceForm.getPostcode2(),
                distance.getDistance());

        return ResponseEntity.ok(distance);
    }

    /**
     * Get individual postcode
     * @param id id of the postcode
     * @return postcode object
     * @throws PostcodeNotFoundException when postcode not found.
     */
    @GetMapping(value = "/postcode/{id}", produces = "application/json")
    public PostCodeLatLng getPostcode(@PathVariable Long id) throws PostcodeNotFoundException {

        PostCodeLatLng postCodeLatLng = postCodeLatLngRepository.findById(id);
        if(postCodeLatLng == null) {
            throw new PostcodeNotFoundException(String.format("Postcode with id %s not found", id));
        }

        postCodeLatLng.add(buildPostcodeLink(postCodeLatLng.getId()));

        return postCodeLatLng;
    }

    /**
     * Update postcode
     * @param postcodeForm form containing postcode to update.
     * @param id id of postcode to update
     * @return updated postcode.
     * @throws PostcodeNotFoundException when postcode not found
     * @throws IllegalPostcodeUpdateException when postcode update failed.
     */
    @PutMapping(value = "/postcode/{id}", consumes = "application/json", produces = "application/json")
    public PostCodeLatLng patchPostcode(@RequestBody PostcodeForm postcodeForm, @PathVariable Long id) throws PostcodeNotFoundException, IllegalPostcodeUpdateException {

        log.info("Patching postcode {} id {} with latitude={} and longitude={}", id, postcodeForm.getPostcode(), postcodeForm.getLatitude(), postcodeForm.getLongitude());
        PostCodeLatLng postCodeLatLng = postCodeLatLngRepository.findById(id);
        if(postCodeLatLng == null) {
            throw new IllegalPostcodeUpdateException(String.format("Postcode with id %s not found", id));
        }

        if(!postcodeForm.getPostcode().equals(postCodeLatLng.getPostcode())) {
            throw new IllegalPostcodeUpdateException(String.format("Postcode %s and id %s not match. Cannot update", postcodeForm.getPostcode(), id));
        }
        postCodeLatLng.setLatitude(postcodeForm.getLatitude());
        postCodeLatLng.setLongitude(postcodeForm.getLongitude());

        postCodeLatLngRepository.save(postCodeLatLng);

        postCodeLatLng.add(buildPostcodeLink(postCodeLatLng.getId()));

        return postCodeLatLng;
    }

    /**
     * Spring hateos build link with name
     * @param postcodeId postcode id to link with
     * @param linkName link name
     * @return link.
     */
    private Link buildPostcodeLinkWithName(long postcodeId, String linkName) {
        Link link = WebMvcLinkBuilder.linkTo(WCCRestController.class)
                .slash("postcode")
                .slash(postcodeId)
                .withRel(linkName);
        return link;
    }

    /**
     * Spring hateos build self link
     * @param postcodeId postode id to link with
     * @return link.
     */
    private Link buildPostcodeLink(long postcodeId) {
        Link selfLink = WebMvcLinkBuilder.linkTo(WCCRestController.class)
                .slash("postcode")
                .slash(postcodeId)
                .withSelfRel();
        return selfLink;
    }
}
