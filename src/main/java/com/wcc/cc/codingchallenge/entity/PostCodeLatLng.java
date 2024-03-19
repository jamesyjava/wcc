package com.wcc.cc.codingchallenge.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Getter
@Setter
public class PostCodeLatLng extends RepresentationModel<PostCodeLatLng> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String postcode;
    private double latitude;
    private double longitude;
}
