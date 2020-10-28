package com.flowsigma.ewocs.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class FhirRepository {

  private String fhirHostUrl;
  private FhirContext fhirContext;
  private IGenericClient fhirGenericClient;
  private RestTemplate restTemplate;

  public FhirRepository(
      @Value("${spring.fhir.host}") String fhirHostUrl,
      FhirRestClient fhirRestClient) {

    this.fhirHostUrl = fhirHostUrl;
    this.fhirContext = FhirContext.forR4();
    this.fhirGenericClient = fhirRestClient.getGenericClient();
    this.restTemplate = fhirRestClient.getRestTemplate();
  }

  public String getResponse(String path) {
    HttpEntity<String> resp = restTemplate.exchange(fhirHostUrl + path, HttpMethod.GET, HttpEntity.EMPTY, String.class);
    return resp.getBody();
  }

  public MethodOutcome createDiagnosticReport(DiagnosticReport diagnosticReport) {
    return
        fhirGenericClient
            .create()
            .resource(diagnosticReport)
            .encodedJson()
            .execute();
  }

  public <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
    String resName = fhirContext.getResourceType(theClass);
    log.info("Attempting to fetch resource {} at URL: {}", resName, theUri);

    try {
      return
          fhirGenericClient
          .fetchResourceFromUrl(theClass, theUri);

    } catch (BaseServerResponseException e) {
      throw new RuntimeException("FAILURE: Received HTTP " + e.getStatusCode() + ": " + e.getMessage());
    }
  }
}
