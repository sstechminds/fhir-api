package com.flowsigma.ewocs.fhir.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowsigma.ewocs.fhir.model.PatientRecord;
import com.flowsigma.ewocs.fhir.service.FhirPatientService;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.dstu3.model.Patient;
import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
class PatientController {

	private FhirPatientService fhirPatientService;
	private ObjectMapper om;

	PatientController(FhirPatientService fhirPatientService) {
		this.fhirPatientService = fhirPatientService;
		this.om = new ObjectMapper();
	}

	@GetMapping(value = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<PatientRecord>> getPatients() {
		List<PatientRecord> patients = fhirPatientService.getPatients("Patient");

		return new ResponseEntity<>(patients, HttpStatus.OK);
	}

	@GetMapping(value = "/patient/{patientId}/condition", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getCondition(@PathVariable String patientId) {
		JSONArray conditions = fhirPatientService.getPatientCondition("Condition?patient=" + patientId);

		return new ResponseEntity<>(conditions.toString(), HttpStatus.OK);
	}

	@GetMapping(value = "/imagingstudy/{studyId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getPatientImageStudy(@PathVariable String studyId) {
		String imagingStudy= fhirPatientService.getImagingStudy("ImagingStudy?_id=" + studyId);

		return new ResponseEntity<>(imagingStudy, HttpStatus.OK);
	}

	@GetMapping(value = "/patient/{patientId}/diagnosticreport", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getPatientReports(@PathVariable String patientId) {
		JSONArray drs = fhirPatientService.getPatientDiagnosticReports("DiagnosticReport?patient=" + patientId);

		return new ResponseEntity<>(drs.toString(), HttpStatus.OK);
	}

	@GetMapping(value = "/diagnosticreport/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getDiagnosticReport(@PathVariable String reportId) {
		String dr = fhirPatientService.getPatientDiagnosticReport("DiagnosticReport/" + reportId);

		return new ResponseEntity<>(dr, HttpStatus.OK);
	}

	@GetMapping(value = "/patient/{patientId}/diagnosticreport/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Collection<Patient> getPatientReport(@PathVariable String patientId, @PathVariable String reportId) {
		throw new UnsupportedOperationException("Looks like this endpoint is unavailable onn SIIM FHIR endpoints.");
	}
}
