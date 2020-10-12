package com.flowsigma.ewocs.fhir.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class FhirRepository {
  private static final String API_KEY = "apikey";

  private String fhirHostUrl;
  private String fhirApiKey;

  public FhirRepository() {
    this( "http://hackathon.siim.org/fhir/","dd6f7f1d-1586-438f-8d35-ff589a12f4df");
  }

  public FhirRepository(@Value("${spring.fhir.host}") String fhirHostUrl,
      @Value("${spring.fhir.apiKey}") String fhirApiKey) {
    this.fhirHostUrl = fhirHostUrl;
    this.fhirApiKey = fhirApiKey;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public HttpEntity<String> getResponse(String path) {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.add(API_KEY, fhirApiKey);

    HttpEntity entity = new HttpEntity(headers);

    return restTemplate.exchange(fhirHostUrl + path, HttpMethod.GET, entity, String.class);
  }
}
