package com.flowsigma.ewocs.fhir.builder;

import static org.junit.jupiter.api.Assertions.*;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DiagnosticReportBuilderTest {
  private static final FhirContext ctx = FhirContext.forR4();

  private DiagnosticReportBuilder builder = new DiagnosticReportBuilder();

  @Test
  void build() {

    DiagnosticReport diagnosticReport = builder.build("siimravi2");

    assertFalse(!diagnosticReport.getSubject().getReference().contains("Patient/siimravi2"));

    String reportJson = ctx.newJsonParser().encodeResourceToString(diagnosticReport);

    assertFalse(!reportJson.contains("Patient/siimravi2"));
  }
}