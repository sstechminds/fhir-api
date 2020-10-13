package com.flowsigma.ewocs.fhir.exception;

class PatientNotFoundException extends RuntimeException {

	PatientNotFoundException(Long id) {
		super("No patient found for ID: " + id);
	}
}
