package com.flowsigma.ewocs.fhir.model;

public class PatientDiagnosticReportRecord {
  private String patientId;
  private DiagnosticReportRecord diagnosticReportRecord;

  public String getPatientId() {
    return patientId;
  }

  public void setPatientId(String patientId) {
    this.patientId = patientId;
  }

  public DiagnosticReportRecord getDiagnosticReportRecord() {
    return diagnosticReportRecord;
  }

  public void setDiagnosticReportRecord(DiagnosticReportRecord diagnosticReportRecord) {
    this.diagnosticReportRecord = diagnosticReportRecord;
  }

  @Override
  public String toString() {
    return "PatientDiagnosticReportRecord{" +
            "patientId='" + patientId + '\'' +
            ", diagnosticReportRecord=" + diagnosticReportRecord +
            '}';
  }
}
