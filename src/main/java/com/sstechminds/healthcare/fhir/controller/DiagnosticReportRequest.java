package com.sstechminds.healthcare.fhir.controller;

import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiagnosticReportRequest {
  private String filePath;
  private Map<Object, Object> dicomTags;
}
