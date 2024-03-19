package com.wcc.cc.codingchallenge.repository;

import com.wcc.cc.codingchallenge.entity.PostCodeLatLng;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
class PostCodeLatLngRepositoryTest {

  @Autowired
  private PostCodeLatLngRepository postCodeLatLngRepository;

  @Test
  void performFindByName_Found() {
    Assertions.assertThat(postCodeLatLngRepository.findByPostcode("AB10 7JB")).isNotNull();
    Assertions.assertThat(postCodeLatLngRepository.findByPostcode("AB10 7JB").getPostcode()).isEqualTo("AB10 7JB");
  }

  @Test
  void performFindByName_NotFound() {
    //postCodeLatLngRepository.findByPostcode("ABC DEF");
    Assertions.assertThat(postCodeLatLngRepository.findByPostcode("ABC DEF")).isNull();
  }

  @Test
  void performSave() {
    double latitude = -1.234;
    double longitude = 0.987;
    PostCodeLatLng postCodeLatLng = postCodeLatLngRepository.findById(1);
    postCodeLatLng.setLatitude(latitude);
    postCodeLatLng.setLongitude(longitude);
    postCodeLatLngRepository.save(postCodeLatLng);

    Assertions.assertThat(postCodeLatLngRepository.findById(1).getLatitude()).isEqualTo(latitude);
    Assertions.assertThat(postCodeLatLngRepository.findById(1).getLongitude()).isEqualTo(longitude);
  }
}