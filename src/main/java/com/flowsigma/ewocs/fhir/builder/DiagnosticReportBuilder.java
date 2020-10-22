package com.flowsigma.ewocs.fhir.builder;

import org.hl7.fhir.dstu3.model.DiagnosticReport;

import java.util.UUID;

public class DiagnosticReportBuilder {

    public DiagnosticReport build() {
        DiagnosticReport diagnosticReport = new DiagnosticReport();
        diagnosticReport.setId(UUID.randomUUID().toString());
        return diagnosticReport;
    }
}
