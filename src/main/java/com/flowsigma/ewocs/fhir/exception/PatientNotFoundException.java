package com.flowsigma.ewocs.fhir.exception;

public class PatientNotFoundException extends RuntimeException {

	public PatientNotFoundException() {
		super("No patient found");
	}

	public PatientNotFoundException(String id) {
		super("No patient found for ID: " + id);
	}
}
