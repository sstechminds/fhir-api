package com.sstechminds.healthcare.fhir.controller;

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sstechminds.healthcare.fhir.model.DiagnosticReportRecord;
import com.sstechminds.healthcare.fhir.model.PatientDiagnosticReportRecord;
import com.sstechminds.healthcare.fhir.model.PatientRecord;
import com.sstechminds.healthcare.fhir.service.FhirPatientService;
import com.sstechminds.healthcare.fhir.util.SerDe;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PatientController {
	final static Logger log = LoggerFactory.getLogger(PatientController.class);

	private FhirPatientService fhirPatientService;
	private ObjectMapper objectMapper;
	private FhirContext fhirContext;

	public PatientController(FhirPatientService fhirPatientService) {
		this.fhirPatientService = fhirPatientService;
		this.fhirContext = FhirContext.forR4();
		this.objectMapper = new ObjectMapper();
	}

	@GetMapping(value = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<PatientRecord>> getPatients() {
		log.info("getPatients");
		List<PatientRecord> patients = fhirPatientService.getPatients("Patient");

		return new ResponseEntity<>(patients, HttpStatus.OK);
	}

	@GetMapping(value = "/patient/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getPatient(@PathVariable("id") String id) {
		log.info("getPatient");
		Patient patient = fhirPatientService.getPatient("Patient/" + id);

		return new ResponseEntity<>(SerDe.fhirSerialization(patient), HttpStatus.OK);
	}

	/**
	 * http://dicom.nema.org/medical/dicom/current/output/chtml/part03/sect_C.4.12.html
	 * @param patientId
	 * @return
	 */
	@GetMapping(value = "/patient/{patientId}/diagnosticorder", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getDiagnosticOrders(@PathVariable String patientId) {
		List<ServiceRequest> orders = fhirPatientService.getDiagnosticOrders("ServiceRequest?patient=" + patientId);
//		List<ServiceRequest> orders = fhirPatientService.getDiagnosticOrders();
//		List<ServiceRequest> orders = fhirPatientService.getTodaysDiagnosticOrders();

		return new ResponseEntity<>(SerDe.fhirSerialization(orders), HttpStatus.OK);
	}

	@PostMapping(value = "/patient/{patientId}/diagnosticreport")
	public ResponseEntity<Void> createDiagnosticReport(@PathVariable String patientId) {
		String analyticResults = "SSTECHMINDS workflow analytic results.";
		fhirPatientService.createDiagnosticReport(patientId, analyticResults);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/patient/{patientId}/diagnosticreport", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getPatientReports(@PathVariable String patientId,
													@RequestParam(name = "issuedate", required = false) String issueDate) {
		List<DiagnosticReportRecord> drs = fhirPatientService.getPatientDiagnosticReports("DiagnosticReport?patient=" + patientId, issueDate);

		return new ResponseEntity<>(SerDe.serialize(drs), HttpStatus.OK);
	}

	@PostMapping(value = "/diagnosticreport",
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createDiagnosticReport(@RequestBody DiagnosticReportRequest request) {
    String response;
	  String analyticResults = "SSTECHMINDS workflow analytic results.";
		if(StringUtils.isBlank(request.getFilePath())) {
      response  = fhirPatientService.createDiagnosticReport(request.getDicomTags(), analyticResults);
		} else {
			String filePath = getFileClassPath(request);
			response = fhirPatientService.createDiagnosticReport(filePath, analyticResults);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private String getFileClassPath(DiagnosticReportRequest request) {
		try {
			return new ClassPathResource(request.getFilePath()).getFile().getPath();
		} catch (IOException e) {
			throw new IllegalArgumentException("File not found in the path.");
		}
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

		return new ResponseEntity<>(SerDe.serialize(pdrrs), HttpStatus.OK);
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
}
