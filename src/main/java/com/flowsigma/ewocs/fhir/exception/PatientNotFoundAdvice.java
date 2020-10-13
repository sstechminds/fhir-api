package com.flowsigma.ewocs.fhir.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class PatientNotFoundAdvice {

	@ResponseBody
	@ExceptionHandler(PatientNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String patientNotFoundHandler(PatientNotFoundException ex) {
		return ex.getMessage();
	}
}
