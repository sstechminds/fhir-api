package com.sstechminds.healthcare.fhir.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiagnosticReportRecord {
  private String code;
  private String text;
}
