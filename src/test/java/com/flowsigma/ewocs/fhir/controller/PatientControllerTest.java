package com.flowsigma.ewocs.fhir.controller;

import com.flowsigma.ewocs.fhir.service.FhirPatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private FhirPatientService service;

  // write test cases here

  @Test
  void getPatients() {
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