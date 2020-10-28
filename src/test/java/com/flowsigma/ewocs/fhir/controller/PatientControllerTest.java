package com.flowsigma.ewocs.fhir.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowsigma.ewocs.fhir.model.PatientRecord;
import com.flowsigma.ewocs.fhir.service.FhirPatientService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(controllers = PatientController.class)
public class PatientControllerTest {
  @Autowired private ObjectMapper objectMapper;
  @Autowired private MockMvc mockMvc;

  @MockBean private FhirPatientService fhirPatientService;

  //https://github.com/thombergs/code-examples/blob/master/spring-boot/spring-boot-testing/src/test/java/io/reflectoring/testing/web/RegisterRestControllerTest.java
  @Test
  void getPatients_no_patients_found() throws Exception {
    when(fhirPatientService.getPatients("Patient")).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/patient"))
        .andExpect(status().isOk())
        .andExpect(content().string("[ ]"))
        .andDo(print());
  }

  @Test
  void getPatients() throws Exception {
    List<PatientRecord> patientRecords = new ArrayList<>();

    PatientRecord patientRecord = new PatientRecord();
    patientRecord.setGender("M");
    patientRecord.setName("Ravi");
    patientRecord.setId("1");
    patientRecord.setBirthDate("01-01-2020");

    patientRecords.add(patientRecord);

    when(fhirPatientService.getPatients("Patient")).thenReturn(patientRecords);

    MvcResult mvcResult = mockMvc.perform(get("/patient"))
        .andExpect(status().isOk())
        // .andExpect(content().string(objectMapper.writeValueAsString(patientRecords)))
        // .andExpect(jsonPath("$[?(@.id == 1)]", is("1")))
        // .andDo(print())
        .andDo(result -> {
          String json = result.getResponse().getContentAsString();
          List list = new ObjectMapper().readValue(json, List.class);
          assert list.size() == 1;
        })
        .andReturn();

    String expectedResponseBody = "[{\"id\":\"1\",\"name\":\"Ravi\",\"gender\":\"M\",\"birthDate\":\"01-01-2020\"}]";
    String actualResponseBody = mvcResult.getResponse().getContentAsString();
    assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
  }

  @Test
  void getCondition() {
  }

  @Test
  void getPatientImageStudy() {
  }

  @Test
  void getPatientReports() {
  }

  @Test
  void getDiagnosticReport() {
  }

  @Test
  void getPatientReport() {
  }
}