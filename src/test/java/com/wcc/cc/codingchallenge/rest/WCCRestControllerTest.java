package com.wcc.cc.codingchallenge.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcc.cc.codingchallenge.rest.form.PostcodeDistanceForm;
import com.wcc.cc.codingchallenge.rest.form.PostcodeForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(value = "user")
class WCCRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //Test variable
    double latitude = -1.99;
    double longitude = 9.888;

    @Test
    void calculateDistance_Postcode_Found() throws Exception {
        this.mockMvc.perform(post("/distance")
                            .content(asJsonString(new PostcodeDistanceForm("AB10 7JB", "AB21 0AL")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.distance").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode1").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode1.links[:1].rel").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode1.links[:1].rel").value("postcode1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode1.links[:1].href").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode1.links[:1].href").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode2").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode2.links[:1].rel").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode2.links[:1].rel").value("postcode2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode2.links[:1].href").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode2.links[:1].href").isNotEmpty());
    }

    @Test
    void calculateDistance_InvalidInputPostcode1() throws Exception {
        PostcodeDistanceForm postcodeDistanceForm = new PostcodeDistanceForm();
        postcodeDistanceForm.setPostcode2("AB10 7JB");
        this.mockMvc.perform(post("/distance")
                        .content(asJsonString(postcodeDistanceForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Input validation failure on  postcode1 Please provide postcode1"));
    }

    @Test
    void calculateDistance_InvalidInputPostcode2() throws Exception {
        PostcodeDistanceForm postcodeDistanceForm = new PostcodeDistanceForm();
        postcodeDistanceForm.setPostcode1("AB10 7JB");
        this.mockMvc.perform(post("/distance")
                        .content(asJsonString(postcodeDistanceForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Input validation failure on  postcode2 Please provide postcode2"));
    }

    @Test
    void calculateDistance_NoPostcodeInput() throws Exception {

        PostcodeDistanceForm postcodeDistanceForm = new PostcodeDistanceForm();
        this.mockMvc.perform(post("/distance")
                        .content(asJsonString(postcodeDistanceForm))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").exists());
    }

    @Test
    void calculateDistance_Postcode_FirstPostcode_NotFound() throws Exception {
        this.mockMvc.perform(post("/distance")
                        .content(asJsonString(new PostcodeDistanceForm("ABC DEF", "AB21 0AL")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("At least 1 of the postcode not found"));
    }

    @Test
    void calculateDistance_Postcode_SecondPostcode_NotFound() throws Exception {
        this.mockMvc.perform(post("/distance")
                        .content(asJsonString(new PostcodeDistanceForm("AB21 0AL", "ABC DEF")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("At least 1 of the postcode not found"));
    }

    @Test
    void calculateDistance_Postcode_BothPostcode_NotFound() throws Exception {
        this.mockMvc.perform(post("/distance")
                        .content(asJsonString(new PostcodeDistanceForm("ABC DEF", "GHI JKL")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("At least 1 of the postcode not found"));
    }

    @Test
    void performPostcodeGetWithId_ShouldReturnPostcode() throws Exception {
        this.mockMvc.perform(get("/postcode/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode").value("AB10 1XG"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href").isNotEmpty());
    }

    @Test
    void performPostcodeGetWithInvalidId_ShouldReturnNotFound() throws Exception {

        this.mockMvc.perform(get("/postcode/1000")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Postcode with id 1000 not found"));
    }

    @Test
    void performUpdateWithIdAndPostcode_ShouldReturnUpdatedPostcode() throws Exception {

        this.mockMvc.perform(put("/postcode/1")
                        .content(asJsonString(new PostcodeForm("AB10 1XG", latitude, longitude)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postcode").value("AB10 1XG"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.latitude").value(latitude))
                .andExpect(MockMvcResultMatchers.jsonPath("$.longitude").value(longitude))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self.href").isNotEmpty());
    }

    @Test
    void performUpdateWithInvalidId_ShouldReturnBadRequest() throws Exception {

        this.mockMvc.perform(put("/postcode/1000")
                        .content(asJsonString(new PostcodeForm("AB10 1XG", latitude, longitude)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Postcode with id 1000 not found"));
    }
    @Test
    void performUpdateWithMismatchedIdPostcode_ShouldReturnBadRequest() throws Exception {

        this.mockMvc.perform(put("/postcode/1")
                        .content(asJsonString(new PostcodeForm("ABC DEF", latitude, longitude)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Postcode ABC DEF and id 1 not match. Cannot update"));
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}