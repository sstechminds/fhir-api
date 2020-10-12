package com.flowsigma.ewocs.fhir.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.flowsigma.ewocs.fhir.model.PatientRecord;
import java.util.ArrayList;
import java.util.List;
import org.hl7.fhir.dstu3.model.Patient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;

public class PatientMap {

  private List<PatientRecord> patients;
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

  public List<PatientRecord> getPatientRecords() {
    return patients;
  }

  private void addPatients(JSONObject resource) {
    PatientRecord patientRecord = new PatientRecord();

    Patient patientRes = parser.parseResource(Patient.class, resource.toString());

    patientRecord.setId(patientRes.getId().substring(8, 22));
    patientRecord.setName(patientRes.getName().get(0).getGivenAsSingleString() + " " + patientRes.getName().get(0).getFamily());
    patientRecord.setGender(patientRes.getGender().getDisplay());
    patientRecord.setBirthDate(patientRes.getBirthDate().toString().substring(0, 10));

//      String last = patientRes.getName().get(0).getFamily();
//      String first = patientRes.getName().get(0).getGiven().get(0); //patientRes.getName().get(0).getGivenAsSingleString()
//      String PID = patientRes.getIdentifier().get(0).getValue();

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
