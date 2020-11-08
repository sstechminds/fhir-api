package com.flowsigma.ewocs.fhir.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatientDiagnosticReportRecord {
  private String patientId;
  private DiagnosticReportRecord diagnosticReportRecord;
}
