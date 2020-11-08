package com.flowsigma.ewocs.fhir.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowsigma.ewocs.fhir.model.PatientRecord;
import com.flowsigma.ewocs.fhir.service.FhirPatientService;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(controllers = PatientController.class)
public class PatientControllerTest {
  @Autowired private ObjectMapper objectMapper;
  @Autowired private MockMvc mockMvc;

  @MockBean private FhirPatientService fhirPatientService;

  //https://github.com/thombergs/code-examples/blob/master/spring-boot/spring-boot-testing/src/test/java/io/reflectoring/testing/web/RegisterRestControllerTest.java
  @Test
  void testGetPatients_no_patients_found() throws Exception {
    when(fhirPatientService.getPatients("Patient")).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/patient"))
        .andExpect(status().isOk())
        .andExpect(content().string("[ ]"))
        .andDo(print());
  }

  @Test
  void testGetPatients() throws Exception {
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
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is("1")))
        .andDo(result -> {
          String json = result.getResponse().getContentAsString();
          TypeReference<List<Map<String, Object>>> typeRef = new TypeReference<List<Map<String, Object>>>() {};
          List<Map<String, Object>> list = new ObjectMapper().readValue(json, typeRef);
          assert list.size() == 1;
          assert list.get(0).get("id").equals("1");
        })
        .andReturn();

    String expectedResponseBody = "[{\"id\":\"1\",\"name\":\"Ravi\",\"gender\":\"M\",\"birthDate\":\"01-01-2020\"}]";
    String actualResponseBody = mvcResult.getResponse().getContentAsString();
    assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
  }

  @Test
  void testCreateDiagnosticReport() throws Exception {
    String filePath = "dicom" + File.separator + "ImageWithAccession.dcm";
    when(fhirPatientService.createDiagnosticReport(anyString(), anyString())).thenReturn("reportId");

    DiagnosticReportRequest request = new DiagnosticReportRequest();
    request.setFilePath(filePath);
    String payload = new ObjectMapper().writeValueAsString(request);

    mockMvc.perform(post("/diagnosticreport")
        .contentType(MediaType.APPLICATION_JSON)
        .content(payload))
        .andExpect(status().isOk())
        .andExpect(content().string("reportId"));

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(fhirPatientService, times(1))
        .createDiagnosticReport(captor.capture(), anyString());

    assertTrue(captor.getValue().endsWith(filePath));
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