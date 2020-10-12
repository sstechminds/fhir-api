package com.flowsigma.ewocs.fhir.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

//https://howtodoinjava.com/spring-boot2/testing/junit5-with-spring-boot2/
//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PatientControllerRestTemplateTest {

	private static final ObjectMapper om = new ObjectMapper();

	@LocalServerPort
	int randomServerPort;

	@Value("${spring.fhir.host}")
	String fhirHostUrl;

	@Test
	void findPatients() throws JSONException, URISyntaxException {
		String expected = "{id:1,name:\"Book Name\",author:\"Mkyong\",price:9.99}";

		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = "http://localhost:"+randomServerPort+"/patient/";
		URI uri = new URI(baseUrl);
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		HttpEntity requestEntity = new HttpEntity<>(null, headers);

		ResponseEntity<List<Map<String, String>>> response =
				restTemplate.exchange(uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Map<String, String>>>() {});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
		List<Map<String, String>> body = response.getBody();
//		System.out.println("Body" + body);
//TODO:		JSONAssert.assertEquals(expected, response.getBody(), false);
//		verify(mockRepository, times(1)).findById(1L);
	}
}
