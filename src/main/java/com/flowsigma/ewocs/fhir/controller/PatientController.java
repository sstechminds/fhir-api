package com.flowsigma.ewocs.fhir.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowsigma.ewocs.fhir.model.DiagnosticRecord;
import com.flowsigma.ewocs.fhir.model.DiagnosticReportRecord;
import com.flowsigma.ewocs.fhir.model.PatientDiagnosticReportRecord;
import com.flowsigma.ewocs.fhir.model.PatientRecord;
import com.flowsigma.ewocs.fhir.service.FhirPatientService;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PatientController {
	final static Logger log = LoggerFactory.getLogger(PatientController.class);

	private FhirPatientService fhirPatientService;
	private ObjectMapper objectMapper;

	public PatientController(FhirPatientService fhirPatientService) {
		this.fhirPatientService = fhirPatientService;
		this.objectMapper = new ObjectMapper();
	}

	@GetMapping(value = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<PatientRecord>> getPatients() {
		log.info("getPatients");
		List<PatientRecord> patients = fhirPatientService.getPatients("Patient");

		return new ResponseEntity<>(patients, HttpStatus.OK);
	}

	@GetMapping(value = "/patient/{patientId}/diagnosticorder", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<DiagnosticRecord>> getDiagnosticOrders(@PathVariable String patientId) {
		List<DiagnosticRecord> orders = fhirPatientService.getDiagnosticOrders("ServiceRequest?patient=" + patientId);

		return new ResponseEntity<>(orders, HttpStatus.OK);
	}

	@GetMapping(value = "/patient/{patientId}/condition", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getCondition(@PathVariable String patientId) {
		String conditions = fhirPatientService.getPatientCondition("Condition?patient=" + patientId);

		return new ResponseEntity<>(conditions, HttpStatus.OK);
	}

	@GetMapping(value = "/imagingstudy/{studyId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getPatientImageStudy(@PathVariable String studyId) {
		String imagingStudy= fhirPatientService.getImagingStudy("ImagingStudy?_id=" + studyId);

		return new ResponseEntity<>(imagingStudy, HttpStatus.OK);
	}

	@GetMapping(value = "/patient/{patientId}/diagnosticreport", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getPatientReports(@PathVariable String patientId,
													@RequestParam(name = "issuedate", required = false) String issueDate) {
		List<DiagnosticReportRecord> drs = fhirPatientService.getPatientDiagnosticReports("DiagnosticReport?patient=" + patientId, issueDate);

		return new ResponseEntity<>(serialize(drs), HttpStatus.OK);
	}

	@GetMapping(value = "/diagnosticreport/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getDiagnosticReport(@PathVariable String reportId) {
		String dr = fhirPatientService.getPatientDiagnosticReport("DiagnosticReport/" + reportId);

		return new ResponseEntity<>(dr, HttpStatus.OK);
	}

	@GetMapping(value = "/patient/{patientId}/diagnosticreport/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getPatientReport(@PathVariable String patientId, @PathVariable String reportId) {
		List<DiagnosticReportRecord> drrs = fhirPatientService.getPatientDiagnosticReports("DiagnosticReport?patient=" + patientId);
		List<PatientDiagnosticReportRecord> pdrrs = drrs.stream().map(dr -> new PatientDiagnosticReportRecord(patientId, dr)).collect(Collectors.toList());

		return new ResponseEntity<>(serialize(pdrrs), HttpStatus.OK);
	}

	private String serialize(List<?> nonFhirResource) {
		try {
			return objectMapper.writeValueAsString(nonFhirResource);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
