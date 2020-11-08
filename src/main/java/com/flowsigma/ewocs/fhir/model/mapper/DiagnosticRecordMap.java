package com.flowsigma.ewocs.fhir.model.mapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.flowsigma.ewocs.fhir.model.DiagnosticOrderRecord;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DiagnosticRecordMap {
  private FhirContext ctx;
  private List<DiagnosticOrderRecord> diagnosticOrderRecords;
  private IParser parser;

  public DiagnosticRecordMap(String response) throws JSONException {
    diagnosticOrderRecords = new ArrayList<>();
    ctx = FhirContext.forR4();

    List<JSONObject> resources = getResources(response);
    for (JSONObject resource : resources) {
      addDiagnosticRecords(resource);
    }
  }

  public List<DiagnosticOrderRecord> getDiagnosticRecords() {
    return this.diagnosticOrderRecords;
  }

  private void addDiagnosticRecords(JSONObject resource) {
    parser = ctx.newJsonParser();
    parser.setPrettyPrint(true);

    ServiceRequest serviceRequest = parser.parseResource(ServiceRequest.class, resource.toString());
    DiagnosticOrderRecord diagnosticOrderRecord = new DiagnosticOrderRecord();
    diagnosticOrderRecord.setStatus(serviceRequest.getStatus().getDisplay());
    diagnosticOrderRecord.setCodeText(serviceRequest.getCode().getText());
    diagnosticOrderRecord.setCodingCode(serviceRequest.getCode().getCoding().get(0).getCode());
    diagnosticOrderRecord.setReasonCodeText(serviceRequest.getReasonCode().get(0).getText());
    diagnosticOrderRecord.setOccuranceDateTime(serviceRequest.getOccurrenceDateTimeType().asStringValue());
    this.diagnosticOrderRecords.add(diagnosticOrderRecord);
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
