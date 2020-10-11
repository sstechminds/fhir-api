package com.flowsigma.ewocs.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.Patient;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;

public class DiagnosticReportMap {

  private DiagnosticReport diagnosticReport;
  private FhirContext ctx = FhirContext.forDstu3();
  private IParser parser;

  public DiagnosticReportMap(HttpEntity<String> response) throws JSONException {

    JSONObject resource = getResource(response.getBody());
    parser = ctx.newJsonParser();
    parser.setPrettyPrint(true);

    addDiagnosticReport(resource);
  }

  public DiagnosticReport getDiagnosticReport() {
    return this.diagnosticReport;
  }

  private void addDiagnosticReport(JSONObject resource) {
    System.out.println("DiagnosticReport:" + resource.toString());
    this.diagnosticReport = parser.parseResource(DiagnosticReport.class, resource.toString());
  }

  private JSONObject getResource(String response) throws JSONException {
    return new JSONObject(response);
  }
}
