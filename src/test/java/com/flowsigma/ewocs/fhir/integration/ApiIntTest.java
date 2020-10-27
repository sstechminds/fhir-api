package com.flowsigma.ewocs.fhir.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import org.json.JSONException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

//https://howtodoinjava.com/spring-boot2/testing/junit5-with-spring-boot2/
//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Disabled
class ApiIntTest {

	@LocalServerPort
	private int randomServerPort; //bind to the above RANDOM_PORT

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void findPatients() throws JSONException, URISyntaxException {
		String expected = "{\"id\":\"siimravi2/_hist\",\"name\":\"Ravi SIIM\",\"gender\":\"Male\",\"birthDate\":\"Mon Mar 31\"}";

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

		final String baseUrl = "http://localhost:"+randomServerPort+"/patient/siimravi2/diagnosticreport";
		URI uri = new URI(baseUrl);

		ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		assertTrue(response.getBody().contains(expected));
	}

	@Test
	void findPatientReportsByDate() throws JSONException, URISyntaxException {
		String expected = "[{\"code\":\"36572-6\",\"text\":\"Chest AP\"}]";

		final String baseUrl = "http://localhost:"+randomServerPort+"/patient/siimravi2/diagnosticreport?issuedate=1-1-2000";
		URI uri = new URI(baseUrl);

		ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		assertTrue(response.getBody().contains("36572-6"));
	}
}
