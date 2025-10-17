package com.sstechminds.healthcare.fhir.model.mapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.json.JSONException;
import org.json.JSONObject;

public class DiagnosticReportMap {
  private FhirContext ctx;
  private DiagnosticReport diagnosticReport;
  private IParser parser;

  public DiagnosticReportMap(String response) throws JSONException {
    ctx = FhirContext.forR4();

    JSONObject resource = getResource(response);

    addDiagnosticReport(resource);
  }

  public DiagnosticReport getDiagnosticReport() {
    return this.diagnosticReport;
  }

  private void addDiagnosticReport(JSONObject resource) {
    parser = ctx.newJsonParser();
    parser.setPrettyPrint(true);

    this.diagnosticReport = parser.parseResource(DiagnosticReport.class, resource.toString());
  }

  private JSONObject getResource(String response) throws JSONException {
    return new JSONObject(response);
  }
}
