package com.flowsigma.ewocs.fhir.model;

import lombok.Data;

@Data
public class DicomTags {
  private String patientID;
  private String accessionNumber;
}
