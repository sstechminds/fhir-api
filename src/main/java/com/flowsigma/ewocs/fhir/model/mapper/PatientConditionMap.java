package com.flowsigma.ewocs.fhir.model.mapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Condition;
import org.json.JSONException;
import org.json.JSONObject;

public class PatientConditionMap {
  private FhirContext ctx;
  private List<Condition> conditions;
  private IParser parser;

  public PatientConditionMap(String response) throws JSONException {
    conditions = new ArrayList<>();
    ctx = FhirContext.forDstu3();

    JSONObject resource = getResource(response);

    addCondition(resource);
  }

  public List<Condition> getPatientConditions() {
    return this.conditions;
  }

  private void addCondition(JSONObject resource) {
    parser = ctx.newJsonParser();
    parser.setPrettyPrint(true);

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
