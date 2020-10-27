package com.flowsigma.ewocs.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.AdditionalRequestHeadersInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.DiagnosticReport;
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
  private FhirContext fhirContext;
  private RestTemplate restTemplate;


  public FhirRepository(@Value("${spring.fhir.host}") String fhirHostUrl,
      @Value("${spring.fhir.apiKey}") String fhirApiKey) {
    this.fhirHostUrl = fhirHostUrl;
    this.fhirApiKey = fhirApiKey;
    this.fhirContext = FhirContext.forR4();
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

  public MethodOutcome createDiagnosticReport(DiagnosticReport diagnosticReport) {
    // https://smilecdr.com/hapi-fhir/docs/interceptors/built_in_client_interceptors.html
    AdditionalRequestHeadersInterceptor interceptor = new AdditionalRequestHeadersInterceptor();
    interceptor.addHeaderValue(API_KEY, fhirApiKey);

    // https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-client-okhttp/src/test/java/ca/uhn/fhir/okhttp/GenericOkHttpClientDstu2Test.java
    IGenericClient client = fhirContext.newRestfulGenericClient(fhirHostUrl);
    client.registerInterceptor(interceptor);

    return client.create().resource(diagnosticReport).encodedJson().execute();
  }
}
