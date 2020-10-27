package com.flowsigma.ewocs.fhir.service;

import static com.flowsigma.ewocs.fhir.util.DateUtil.formatToLocalDate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import com.flowsigma.ewocs.fhir.builder.DiagnosticReportBuilder;
import com.flowsigma.ewocs.fhir.model.DiagnosticOrderRecord;
import com.flowsigma.ewocs.fhir.model.DiagnosticReportRecord;
import com.flowsigma.ewocs.fhir.model.PatientRecord;
import com.flowsigma.ewocs.fhir.model.mapper.DiagnosticRecordMap;
import com.flowsigma.ewocs.fhir.model.mapper.DiagnosticReportMap;
import com.flowsigma.ewocs.fhir.model.mapper.DiagnosticReportsMap;
import com.flowsigma.ewocs.fhir.model.mapper.ImagingStudyMap;
import com.flowsigma.ewocs.fhir.model.mapper.PatientConditionMap;
import com.flowsigma.ewocs.fhir.model.mapper.PatientMap;
import com.flowsigma.ewocs.fhir.repository.FhirRepository;
import com.flowsigma.ewocs.fhir.util.DateUtil;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FhirPatientService {
  private FhirContext fhirContext;
  private FhirRepository fhirRepository;

  public FhirPatientService(FhirRepository fhirRepository) {
    this.fhirRepository = fhirRepository;
    this.fhirContext = FhirContext.forR4();
  }

  public List<PatientRecord> getPatients(String path) {
    PatientMap patientMap = new PatientMap(fhirRepository.getResponse(path));
    return patientMap.getPatientRecords();
  }

  public List<DiagnosticOrderRecord> getDiagnosticOrders(String path) {
    DiagnosticRecordMap diagnosticRecordMap = new DiagnosticRecordMap(fhirRepository.getResponse(path));

    return diagnosticRecordMap.getDiagnosticRecords();
  }

  public String getPatientCondition(String path) {
    JSONArray array = new JSONArray();

    PatientConditionMap patientMap = new PatientConditionMap(fhirRepository.getResponse(path));
    List<Condition> conditions = patientMap.getPatientConditions();
    if(conditions != null) {
      conditions.stream()
              .map(condition -> fhirContext.newJsonParser().encodeResourceToString(condition))
              .forEach(s -> array.put(new JSONObject(s)));
    }
    return array.toString();
  }

  public String getPatientDiagnosticReport(String path) {
    DiagnosticReportMap patientMap = new DiagnosticReportMap(fhirRepository.getResponse(path));
    DiagnosticReport diagnosticReport = patientMap.getDiagnosticReport();
    return diagnosticReport == null ? "{}" : fhirContext.newJsonParser().encodeResourceToString(diagnosticReport);
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
    return (inputDate == null) || formatToLocalDate(issuedDate).equals(DateUtil.parseToLocalDate(inputDate));
  }

  private String buildFullDiagnosticRecords(List<DiagnosticReport> diagnosticReports) {
    JSONArray array = new JSONArray();

    if(diagnosticReports != null) {
      diagnosticReports.stream()
              .map(diagnosticReport -> fhirContext.newJsonParser().encodeResourceToString(diagnosticReport))
              .forEach(s -> array.put(new JSONObject(s)));
    }
    return array.toString();
  }

  public String getImagingStudy(String path) {
    ImagingStudyMap imagingStudyMap = new ImagingStudyMap(fhirRepository.getResponse(path));
    ImagingStudy imagingStudy = imagingStudyMap.getImagingStudy();
    return imagingStudy == null ? "{}" : fhirContext.newJsonParser().encodeResourceToString(imagingStudy);
  }

  public void createDiagnosticReport(String patientID, String analyticResults) {
    DiagnosticReportBuilder builder = new DiagnosticReportBuilder();
    DiagnosticReport diagnosticReport = builder.build(patientID, analyticResults);

    MethodOutcome outcome = fhirRepository.createDiagnosticReport(diagnosticReport);

    log.debug("created diagnosticReport: {}", outcome.getCreated());
  }
}
