package com.flowsigma.ewocs.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import com.flowsigma.ewocs.fhir.model.DiagnosticReportRecord;
import com.flowsigma.ewocs.fhir.model.PatientRecord;
import com.flowsigma.ewocs.fhir.model.mapper.DiagnosticReportMap;
import com.flowsigma.ewocs.fhir.model.mapper.DiagnosticReportsMap;
import com.flowsigma.ewocs.fhir.model.mapper.ImagingStudyMap;
import com.flowsigma.ewocs.fhir.model.mapper.PatientConditionMap;
import com.flowsigma.ewocs.fhir.model.mapper.PatientMap;
import com.flowsigma.ewocs.fhir.repository.FhirRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.flowsigma.ewocs.fhir.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.ImagingStudy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import static com.flowsigma.ewocs.fhir.util.DateUtil.formattedDate;

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
    FhirRepository repo = new FhirRepository( "http://hackathon.siim.org/fhir/","dd6f7f1d-1586-438f-8d35-ff589a12f4df");

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

//    String patientID = "siimravi";
//    String diagnosticReportID = "a819497684894128";
//    String diagnosticReport = new FhirPatientService(new FhirRepository()).getPatientDiagnosticReport( "DiagnosticReport/" + diagnosticReportID);
//    System.out.print("diagnosticReport: \n" + diagnosticReport);

//    String diagnosticReport2 = new FhirPatientService().getPatientDiagnosticReports(FHIR_HOST_URL + "DiagnosticReport?patient=" + patientID + "%26report=" + diagnosticReportID);
//    System.out.print("diagnosticReport2: \n" + diagnosticReport2);
  }

  public List<PatientRecord> getPatients(String path) {
    PatientMap patientMap = new PatientMap(fhirRepository.getResponse(path));
    return patientMap.getPatientRecords();
  }

  public String getPatientCondition(String path) {
    JSONArray array = new JSONArray();

    PatientConditionMap patientMap = new PatientConditionMap(fhirRepository.getResponse(path));
    List<Condition> conditions = patientMap.getPatientConditions();
    if(conditions != null) {
      conditions.stream()
              .map(condition -> ourCtx.newJsonParser().encodeResourceToString(condition))
              .forEach(s -> array.put(new JSONObject(s)));
    }
    return array.toString();
  }

  public String getPatientDiagnosticReport(String path) {
    DiagnosticReportMap patientMap = new DiagnosticReportMap(fhirRepository.getResponse(path));
    DiagnosticReport diagnosticReport = patientMap.getDiagnosticReport();
    return diagnosticReport == null ? "{}" : ourCtx.newJsonParser().encodeResourceToString(diagnosticReport);
  }

  public List<DiagnosticReportRecord> getPatientDiagnosticReports(String path) {
    DiagnosticReportsMap patientMap = new DiagnosticReportsMap(fhirRepository.getResponse(path));
    List<DiagnosticReport> diagnosticReports = patientMap.getDiagnosticReports();

    return buildCustomDiagnosticRecords(diagnosticReports, null);
  }

  public List<DiagnosticReportRecord> getPatientDiagnosticReports(String path, String issuedDate) {
    DiagnosticReportsMap patientMap = new DiagnosticReportsMap(fhirRepository.getResponse(path));
    List<DiagnosticReport> diagnosticReports = patientMap.getDiagnosticReports();

    return buildCustomDiagnosticRecords(diagnosticReports, issuedDate);
  }

  private List<DiagnosticReportRecord> buildCustomDiagnosticRecords(List<DiagnosticReport> diagnosticReports, String issuedDate) {
    List<DiagnosticReportRecord> records = Collections.emptyList();

    if(diagnosticReports != null) {
      records = diagnosticReports.stream()
              //.filter(diagnosticReport -> diagnosticReport.getEffective().equals(inputDate))
              .filter(diagnosticReport -> includeRecordByDate(diagnosticReport.getIssued(), issuedDate))
              .map(diagnosticReport -> {
                  DiagnosticReportRecord record = new DiagnosticReportRecord();
                  record.setCode(diagnosticReport.getCode().getCoding().get(0).getCode());
                  record.setText(diagnosticReport.getCode().getText());
                  return record;
              })
              .collect(Collectors.toList());
    }
    return records;
  }

  private boolean includeRecordByDate(Date issuedDate, String inputDate) {
    return (inputDate == null) || formattedDate(issuedDate).equals(DateUtil.parse(inputDate));
  }

  private String buildFullDiagnosticRecords(List<DiagnosticReport> diagnosticReports) {
    JSONArray array = new JSONArray();

    if(diagnosticReports != null) {
      diagnosticReports.stream()
              .map(diagnosticReport -> ourCtx.newJsonParser().encodeResourceToString(diagnosticReport))
              .forEach(s -> array.put(new JSONObject(s)));
    }
    return array.toString();
  }

  public String getImagingStudy(String path) {
    ImagingStudyMap imagingStudyMap = new ImagingStudyMap(fhirRepository.getResponse(path));
    ImagingStudy imagingStudy = imagingStudyMap.getImagingStudy();
    return imagingStudy == null ? "{}" : ourCtx.newJsonParser().encodeResourceToString(imagingStudy);
  }
}
