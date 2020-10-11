package com.flowsigma.ewocs.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hl7.fhir.dstu3.model.Patient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;

public class PatientMap {

  private List<Map<String, String>> patients;
  private FhirContext ctx = FhirContext.forDstu3();
  private IParser parser;

  public PatientMap(HttpEntity<String> response) throws JSONException {
    patients = new ArrayList<>();

    List<JSONObject> resources = getResources(response.getBody());
    for (JSONObject resource : resources) {
      parser = ctx.newJsonParser();
      parser.setPrettyPrint(true);

      addPatients(resource);
    }
  }

  public List<Map<String, String>> getPatientRecords() {
    return patients;
  }

  private void addPatients(JSONObject resource) {
    Map<String, String> patientRecord = new HashMap<>();
    Patient patientRes = parser.parseResource(Patient.class, resource.toString());

    patientRecord.put("id", patientRes.getId().substring(8, 22));
    patientRecord.put("name",
        patientRes.getName().get(0).getGivenAsSingleString() + " " + patientRes.getName().get(0).getFamily());
//  String last = patientRes.getName().get(0).getFamily();
//  String first = patientRes.getName().get(0).getGiven().get(0); //patientRes.getName().get(0).getGivenAsSingleString()
//  String PID = patientRes.getIdentifier().get(0).getValue();
    patientRecord.put("gender", patientRes.getGender().getDisplay());
    patientRecord.put("birthDate", patientRes.getBirthDate().toString().substring(0, 10));
    this.patients.add(patientRecord);
  }

  private ArrayList<JSONObject> getResources(String response) throws JSONException {
    ArrayList<JSONObject> resources = new ArrayList<>();

    JSONObject json = new JSONObject(response);
    JSONArray entries = json.getJSONArray("entry");
    for (int i = 0; i < entries.length(); i++) {
      JSONObject entry = entries.getJSONObject(i);
      JSONObject resource = entry.getJSONObject("resource");
      resources.add(resource);
    }

    return resources;
  }
}
