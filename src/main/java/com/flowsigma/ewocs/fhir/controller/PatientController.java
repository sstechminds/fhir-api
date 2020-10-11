package com.flowsigma.ewocs.fhir.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowsigma.ewocs.fhir.model.Patient;
import com.flowsigma.ewocs.fhir.service.FhirPatientService;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PatientController {

	FhirPatientService fhirPatientService;
	ObjectMapper om;

	PatientController(FhirPatientService fhirPatientService) {
		this.fhirPatientService = fhirPatientService;
		this.om = new ObjectMapper();
	}

	@GetMapping("/patient")
	ResponseEntity<List<Map<String, String>>> getPatients() {
		List<Map<String, String>> patients = fhirPatientService.getPatients("Patient");

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(patients, responseHeaders, HttpStatus.OK);
	}

	@GetMapping("/patient/{patientId}/condition")
	ResponseEntity<JSONArray> getCondition(@PathVariable String patientId) {
		JSONArray conditions = fhirPatientService.getPatientCondition("Condition?patient=" + patientId);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(conditions, responseHeaders, HttpStatus.OK);
	}

	@GetMapping("/imagingstudy/{studyId}")
	ResponseEntity<String> getPatientImageStudy(@PathVariable String studyId) {
		String imagingStudy= fhirPatientService.getImagingStudy("ImagingStudy?_id=" + studyId);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(imagingStudy, responseHeaders, HttpStatus.OK);
	}

	@GetMapping("/patient/{patientId}/diagnosticreport")
	ResponseEntity<List<String>> getPatientReports(@PathVariable String patientId) {
		List<String> drs = fhirPatientService.getPatientDiagnosticReports("DiagnosticReport?patient=" + patientId);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(drs, responseHeaders, HttpStatus.OK);
	}

	@GetMapping("/diagnosticreport/{reportId}")
	ResponseEntity<String> getDiagnosticReport(@PathVariable String reportId) {
		String dr = fhirPatientService.getPatientDiagnosticReport("DiagnosticReport/" + reportId);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(dr, responseHeaders, HttpStatus.OK);
	}

	@GetMapping("/patient/{patientId}/diagnosticreport/{reportId}")
	Collection<Patient> getPatientReport(@PathVariable String patientId, @PathVariable String reportId) {
		throw new UnsupportedOperationException("Looks like this endpoint is unavailable onn SIIM FHIR endpoints.");
	}
}
