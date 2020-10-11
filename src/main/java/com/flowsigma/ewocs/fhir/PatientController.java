package com.flowsigma.ewocs.fhir;

import java.util.Collection;
import java.util.Collections;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PatientController {

	@GetMapping("/patient")
	Collection<Patient> getPatients() {
		Patient patient = new Patient();
		patient.setId(123l);
		return Collections.singleton(patient);
	}

	@GetMapping("/patient/order")
	Collection<Patient> getPatientOrders() {
		return Collections.emptyList();
	}

	@GetMapping("/patient/order/{id}")
	Collection<Patient> getPatientOrdersById(@PathVariable String id) {
		return Collections.emptyList();
	}
}
