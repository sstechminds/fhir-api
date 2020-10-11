package com.flowsigma.ewocs.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.ImagingStudy;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;

public class PatientConditionMap {

  private List<Condition> conditions;
  private FhirContext ctx = FhirContext.forDstu3();
  private IParser parser;

  public PatientConditionMap(HttpEntity<String> response) throws JSONException {
    conditions = new ArrayList<>();

    JSONObject resource = getResource(response.getBody());
    parser = ctx.newJsonParser();
    parser.setPrettyPrint(true);

    addCondition(resource);
  }

  public List<Condition> getPatientConditions() {
    return this.conditions;
  }

  private void addCondition(JSONObject resource) {
    System.out.println("Condition:" + resource.toString());
    Bundle bundle = (Bundle) parser.parseResource(resource.toString());
    if(bundle.getEntry() != null) {
      for(BundleEntryComponent bec : bundle.getEntry()) {
        String id = bec.getId();
        Condition condition = (Condition) bec.getResource();
        this.conditions.add(condition);
      }
    }
  }

  private JSONObject getResource(String response) throws JSONException {
    return new JSONObject(response);
  }
}
