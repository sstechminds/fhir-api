package com.sstechminds.healthcare.fhir.model.mapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.json.JSONException;
import org.json.JSONObject;

public class DiagnosticReportsMap {
  private FhirContext ctx;
  private List<DiagnosticReport> diagnosticReports;
  private IParser parser;

  public DiagnosticReportsMap(String response) throws JSONException {
    diagnosticReports = new ArrayList<>();
    ctx = FhirContext.forR4();

    JSONObject resource = getResource(response);

    addDiagnosticReport(resource);
  }

  public List<DiagnosticReport> getDiagnosticReports() {
    return this.diagnosticReports;
  }

  private void addDiagnosticReport(JSONObject resource) {
    parser = ctx.newJsonParser();
    parser.setPrettyPrint(true);

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
