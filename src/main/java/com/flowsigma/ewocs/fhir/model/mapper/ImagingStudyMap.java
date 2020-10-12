package com.flowsigma.ewocs.fhir.model.mapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.ImagingStudy;
import org.json.JSONException;
import org.json.JSONObject;

public class ImagingStudyMap {
  private FhirContext ctx = FhirContext.forDstu3();

  private ImagingStudy imagingStudy;
  private IParser parser;

  public ImagingStudyMap(String response) throws JSONException {
    JSONObject resource = getResource(response);
    parser = ctx.newJsonParser();
    parser.setPrettyPrint(true);

    addImagingStudy(resource);
  }

  public ImagingStudy getImagingStudy() {
    return this.imagingStudy;
  }

  private void addImagingStudy(JSONObject resource) {
    System.out.println("ImagingStudy:" + resource.toString());
    Bundle bundle = (Bundle) parser.parseResource(resource.toString());
    if(bundle.getEntry() != null && !bundle.getEntry().isEmpty()) {
      String id = bundle.getEntry().get(0).getId();
      ImagingStudy imagingStudy = (ImagingStudy) bundle.getEntry().get(0).getResource();
      this.imagingStudy = imagingStudy;
    }
  }

  private JSONObject getResource(String response) throws JSONException {
    return new JSONObject(response);
  }
}
