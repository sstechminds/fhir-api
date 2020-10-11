package com.flowsigma.ewocs.fhir;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class Patient {
	private Long id;
	private String name;
	private String role;
}
