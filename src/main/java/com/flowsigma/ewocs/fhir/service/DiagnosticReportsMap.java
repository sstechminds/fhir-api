package com.flowsigma.ewocs.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;

public class DiagnosticReportsMap {

  private List<DiagnosticReport> diagnosticReports;
  private FhirContext ctx = FhirContext.forDstu3();
  private IParser parser;

  public DiagnosticReportsMap(HttpEntity<String> response) throws JSONException {
    diagnosticReports = new ArrayList<>();

    JSONObject resource = getResource(response.getBody());
    parser = ctx.newJsonParser();
    parser.setPrettyPrint(true);

    addDiagnosticReport(resource);
  }

  public List<DiagnosticReport> getDiagnosticReports() {
    return this.diagnosticReports;
  }

  private void addDiagnosticReport(JSONObject resource) {
    System.out.println("DiagnosticReport:" + resource.toString());
    Bundle bundle = (Bundle) parser.parseResource(resource.toString());
    if(bundle.getEntry() != null) {
      for(BundleEntryComponent bec : bundle.getEntry()) {
        String id = bec.getId();
        DiagnosticReport diagnosticReport = (DiagnosticReport) bec.getResource();
        this.diagnosticReports.add(diagnosticReport);
      }
    }
  }

  private JSONObject getResource(String response) throws JSONException {
    return new JSONObject(response);
  }
}
