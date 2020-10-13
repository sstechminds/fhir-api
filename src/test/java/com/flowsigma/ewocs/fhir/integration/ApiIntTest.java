package com.flowsigma.ewocs.fhir.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.json.JSONException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

//https://howtodoinjava.com/spring-boot2/testing/junit5-with-spring-boot2/
//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Disabled
class ApiIntTest {

	private static final ObjectMapper om = new ObjectMapper();

	@LocalServerPort
	int randomServerPort;

	@Value("${spring.fhir.host}")
	String fhirHostUrl;

	@Test
	void findPatients() throws JSONException, URISyntaxException {
		String expected = "{\"id\":\"siimravi/_hist\",\"name\":\"Ravi SIIM\",\"gender\":\"Male\",\"birthDate\":\"Mon Mar 31\"}";

		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = "http://localhost:"+randomServerPort+"/patient/";
		URI uri = new URI(baseUrl);

		ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		assertTrue(response.getBody().contains(expected));
	}

	@Test
	void findPatientReports() throws JSONException, URISyntaxException {
		String expected = "[{\"code\":\"24627-2\",\"text\":\"CT Chest\"},{\"code\":\"36572-6\",\"text\":\"Chest AP\"}]";

		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = "http://localhost:"+randomServerPort+"/patient/siimravi/diagnosticreport";
		URI uri = new URI(baseUrl);

		ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

		assertTrue(response.getBody().contains(expected));
	}
}
