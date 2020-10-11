package com.flowsigma.ewocs.fhir.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Patient {
	private Long id;
	private String name;
	private String role;
}
