package com.sstechminds.healthcare.fhir.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.uhn.fhir.context.FhirContext;
import com.sstechminds.healthcare.fhir.model.DicomTags;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.junit.jupiter.api.Test;

class DiagnosticReportBuilderTest {
  private static final FhirContext ctx = FhirContext.forR4();

  private DiagnosticReportBuilder builder = new DiagnosticReportBuilder();

  @Test
  void build() {
    DicomTags dicomTags = new DicomTags();
    dicomTags.setPatientID("siimravi2");

    DiagnosticReport diagnosticReport = builder.build(dicomTags, "analytic results");

    assertTrue(diagnosticReport.getSubject().getReference().contains("Patient/siimravi2"));
    assertEquals("analytic results", diagnosticReport.getConclusion());

    String reportJson = ctx.newJsonParser().encodeResourceToString(diagnosticReport);
    assertTrue(reportJson.contains("Patient/siimravi2"));
  }
}