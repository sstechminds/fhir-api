package com.flowsigma.ewocs.fhir.model.mapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.flowsigma.ewocs.fhir.model.DiagnosticRecord;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DiagnosticRecordMap {
  private FhirContext ctx;
  private List<DiagnosticRecord> diagnosticRecords;
  private IParser parser;

  public DiagnosticRecordMap(String response) throws JSONException {
    diagnosticRecords = new ArrayList<>();
    ctx = FhirContext.forR4();

    List<JSONObject> resources = getResources(response);
    for (JSONObject resource : resources) {
      addDiagnosticRecords(resource);
    }
  }

  public List<DiagnosticRecord> getDiagnosticRecords() {
    return this.diagnosticRecords;
  }

  private void addDiagnosticRecords(JSONObject resource) {
    parser = ctx.newJsonParser();
    parser.setPrettyPrint(true);

    ServiceRequest serviceRequest = parser.parseResource(ServiceRequest.class, resource.toString());
    DiagnosticRecord diagnosticRecord = new DiagnosticRecord();
    diagnosticRecord.setStatus(serviceRequest.getStatus().getDisplay());
    diagnosticRecord.setCodeText(serviceRequest.getCode().getText());
    diagnosticRecord.setCodingCode(serviceRequest.getCode().getCoding().get(0).getCode());
    diagnosticRecord.setReasonCodeText(serviceRequest.getReasonCode().get(0).getText());
    diagnosticRecord.setOccuranceDateTime(serviceRequest.getOccurrenceDateTimeType().asStringValue());
    this.diagnosticRecords.add(diagnosticRecord);
  }

  private ArrayList<JSONObject> getResources(String response) throws JSONException {
    ArrayList<JSONObject> resources = new ArrayList<>();

    JSONObject json = new JSONObject(response);
    if(json.has("entry")) {
      JSONArray entries = json.getJSONArray("entry");
      for (int i = 0; i < entries.length(); i++) {
        JSONObject entry = entries.getJSONObject(i);
        JSONObject resource = entry.getJSONObject("resource");
        resources.add(resource);
      }
    }
    return resources;
  }
}
