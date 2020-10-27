package com.flowsigma.ewocs.fhir.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiagnosticOrderRecord {
  private String status;
  private String codingCode;
  private String codeText;
  private String reasonCodeText;
  private String occuranceDateTime;
}
