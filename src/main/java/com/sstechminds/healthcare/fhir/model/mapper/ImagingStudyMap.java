package com.sstechminds.healthcare.fhir.model.mapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.json.JSONException;
import org.json.JSONObject;

public class ImagingStudyMap {
  private FhirContext ctx;
  private ImagingStudy imagingStudy;
  private IParser parser;

  public ImagingStudyMap(String response) throws JSONException {
    ctx = FhirContext.forR4();

    JSONObject resource = getResource(response);

    addImagingStudy(resource);
  }

  public ImagingStudy getImagingStudy() {
    return this.imagingStudy;
  }

  private void addImagingStudy(JSONObject resource) {
    parser = ctx.newJsonParser();
    parser.setPrettyPrint(true);

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
