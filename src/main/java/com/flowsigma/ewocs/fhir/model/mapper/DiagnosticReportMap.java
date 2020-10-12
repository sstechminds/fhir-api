package com.flowsigma.ewocs.fhir.model.mapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.json.JSONException;
import org.json.JSONObject;

public class DiagnosticReportMap {
  private FhirContext ctx = FhirContext.forDstu3();

  private DiagnosticReport diagnosticReport;
  private IParser parser;

  public DiagnosticReportMap(String response) throws JSONException {

    JSONObject resource = getResource(response);
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
