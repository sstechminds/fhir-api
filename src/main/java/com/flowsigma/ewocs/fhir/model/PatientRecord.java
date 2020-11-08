package com.flowsigma.ewocs.fhir.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PatientRecord {
	private String id;
	private String name;
	private String gender;
	private String birthDate;
}
