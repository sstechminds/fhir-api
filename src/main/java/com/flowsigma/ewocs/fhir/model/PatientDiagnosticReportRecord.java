package com.flowsigma.ewocs.fhir.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDiagnosticReportRecord {
  private String patientId;
  private DiagnosticReportRecord diagnosticReportRecord;
}
