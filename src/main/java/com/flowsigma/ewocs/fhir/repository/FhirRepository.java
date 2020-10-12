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
  private RestTemplate restTemplate;


  public FhirRepository(@Value("${spring.fhir.host}") String fhirHostUrl,
      @Value("${spring.fhir.apiKey}") String fhirApiKey) {
    this.fhirHostUrl = fhirHostUrl;
    this.fhirApiKey = fhirApiKey;
    this.restTemplate = new RestTemplate();
  }

  public String getResponse(String path) {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.add(API_KEY, fhirApiKey);
    HttpEntity entity = new HttpEntity(headers);

    HttpEntity<String> resp = restTemplate.exchange(fhirHostUrl + path, HttpMethod.GET, entity, String.class);

    return resp.getBody();
  }
}
