package com.flowsigma.ewocs.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import java.util.List;
import java.util.Map;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.ImagingStudy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FhirPatientService {
  private static final String API_KEY = "apikey";

  FhirContext ourCtx = FhirContext.forDstu3();

  String fhirHostUrl;
  String fhirApiKey;

  public static void main(String[] args) {

//    List<Map<String, String>> patients = new FhirPatientService().getPatients("Patient");
//    System.out.printf("Patients: \n" + patients.toString());
//
//    //https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-structures-dstu3/src/test/java/ca/uhn/fhir/parser/JsonParserDstu3Test.java
//    String patientID = "siimravi";
//    String patientCondition = new FhirPatientService().getPatientCondition("Condition?patient=" + patientID);
//    System.out.print("patientConditions: \n" + patientCondition);
//
//    String studyID = "a819497684894127";
//    String imagingStudy = new FhirPatientService().getImagingStudy("ImagingStudy?_id=" + studyID);
//    System.out.print("imagingStudy: \n" + imagingStudy);
//
//    String patientID = "siimravi";
//    String diagnosticReport = new FhirPatientService().getPatientDiagnosticReports("DiagnosticReport?patient=" + patientID);
//    System.out.print("diagnosticReports: \n" + diagnosticReport);

    String patientID = "siimravi";
    String diagnosticReportID = "a819497684894128";
    String diagnosticReport = new FhirPatientService().getPatientDiagnosticReport( "DiagnosticReport/" + diagnosticReportID);
    System.out.print("diagnosticReport: \n" + diagnosticReport);

//    String diagnosticReport2 = new FhirPatientService().getPatientDiagnosticReports(FHIR_HOST_URL + "DiagnosticReport?patient=" + patientID + "%26report=" + diagnosticReportID);
//    System.out.print("diagnosticReport2: \n" + diagnosticReport2);
  }

  FhirPatientService() {
    this( "http://hackathon.siim.org/fhir/","dd6f7f1d-1586-438f-8d35-ff589a12f4df");
  }

  FhirPatientService(@Value("${spring.fhir.host}") String fhirHostUrl,
      @Value("${spring.fhir.apiKey}") String fhirApiKey) {
    this.fhirHostUrl = fhirHostUrl;
    this.fhirApiKey = fhirApiKey;
  }

  public List<Map<String, String>> getPatients(String path) {
    PatientMap patientMap = new PatientMap(getResponse(path));
    return patientMap.getPatientRecords();
  }

  public JSONArray getPatientCondition(String path) {
    JSONArray array = new JSONArray();

    PatientConditionMap patientMap = new PatientConditionMap(getResponse(path));
    List<Condition> conditions = patientMap.getPatientConditions();
    if(conditions != null) {
      conditions.stream()
          .map(condition -> ourCtx.newJsonParser().encodeResourceToString(condition))
          .forEach(s -> array.put(new JSONObject(s)));
    }
    return array;
  }

  public String getPatientDiagnosticReport(String path) {
    DiagnosticReportMap patientMap = new DiagnosticReportMap(getResponse(path));
    DiagnosticReport diagnosticReport = patientMap.getDiagnosticReport();
    return diagnosticReport == null ? "{}" : ourCtx.newJsonParser().encodeResourceToString(diagnosticReport);
  }

  public JSONArray getPatientDiagnosticReports(String path) {
    JSONArray array = new JSONArray();

    DiagnosticReportsMap patientMap = new DiagnosticReportsMap(getResponse(path));
    List<DiagnosticReport> diagnosticReports = patientMap.getDiagnosticReports();
    if(diagnosticReports != null) {
      diagnosticReports.stream()
          .map(diagnosticReport -> ourCtx.newJsonParser().encodeResourceToString(diagnosticReport))
          .forEach(s -> array.put(new JSONObject(s)));
    }
    return array;
  }

  public String getImagingStudy(String path) {
    ImagingStudyMap imagingStudyMap = new ImagingStudyMap(getResponse(path));
    ImagingStudy imagingStudy = imagingStudyMap.getImagingStudy();
    return imagingStudy == null ? "{}" : ourCtx.newJsonParser().encodeResourceToString(imagingStudy);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private HttpEntity<String> getResponse(String path) {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
    headers.add(API_KEY, fhirApiKey);

    HttpEntity entity = new HttpEntity(headers);
    return restTemplate.exchange(fhirHostUrl + path, HttpMethod.GET, entity, String.class);
  }
}
