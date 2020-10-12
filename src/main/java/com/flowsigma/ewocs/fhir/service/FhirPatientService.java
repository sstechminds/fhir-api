package com.flowsigma.ewocs.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import com.flowsigma.ewocs.fhir.model.PatientRecord;
import com.flowsigma.ewocs.fhir.repository.FhirRepository;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.ImagingStudy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FhirPatientService {
  private FhirContext ourCtx;
  private FhirRepository fhirRepository;

  public FhirPatientService(FhirRepository fhirRepository) {
    this.fhirRepository = fhirRepository;
    this.ourCtx = FhirContext.forDstu3();
  }

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
    String diagnosticReport = new FhirPatientService(new FhirRepository()).getPatientDiagnosticReport( "DiagnosticReport/" + diagnosticReportID);
    System.out.print("diagnosticReport: \n" + diagnosticReport);

//    String diagnosticReport2 = new FhirPatientService().getPatientDiagnosticReports(FHIR_HOST_URL + "DiagnosticReport?patient=" + patientID + "%26report=" + diagnosticReportID);
//    System.out.print("diagnosticReport2: \n" + diagnosticReport2);
  }

  public List<PatientRecord> getPatients(String path) {
    PatientMap patientMap = new PatientMap(fhirRepository.getResponse(path));
    return patientMap.getPatientRecords();
  }

  public JSONArray getPatientCondition(String path) {
    JSONArray array = new JSONArray();

    PatientConditionMap patientMap = new PatientConditionMap(fhirRepository.getResponse(path));
    List<Condition> conditions = patientMap.getPatientConditions();
    if(conditions != null) {
      conditions.stream()
          .map(condition -> ourCtx.newJsonParser().encodeResourceToString(condition))
          .forEach(s -> array.put(new JSONObject(s)));
    }
    return array;
  }

  public String getPatientDiagnosticReport(String path) {
    DiagnosticReportMap patientMap = new DiagnosticReportMap(fhirRepository.getResponse(path));
    DiagnosticReport diagnosticReport = patientMap.getDiagnosticReport();
    return diagnosticReport == null ? "{}" : ourCtx.newJsonParser().encodeResourceToString(diagnosticReport);
  }

  public JSONArray getPatientDiagnosticReports(String path) {
    JSONArray array = new JSONArray();

    DiagnosticReportsMap patientMap = new DiagnosticReportsMap(fhirRepository.getResponse(path));
    List<DiagnosticReport> diagnosticReports = patientMap.getDiagnosticReports();
    if(diagnosticReports != null) {
      diagnosticReports.stream()
          .map(diagnosticReport -> ourCtx.newJsonParser().encodeResourceToString(diagnosticReport))
          .forEach(s -> array.put(new JSONObject(s)));
    }
    return array;
  }

  public String getImagingStudy(String path) {
    ImagingStudyMap imagingStudyMap = new ImagingStudyMap(fhirRepository.getResponse(path));
    ImagingStudy imagingStudy = imagingStudyMap.getImagingStudy();
    return imagingStudy == null ? "{}" : ourCtx.newJsonParser().encodeResourceToString(imagingStudy);
  }
}
