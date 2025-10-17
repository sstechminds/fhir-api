package com.sstechminds.healthcare.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import com.sstechminds.healthcare.fhir.util.DateUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.ServiceRequest;
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

  public <T extends IBaseResource> List<T> search(String relativeUrl) {
    Bundle bundle = fhirGenericClient.search().byUrl(relativeUrl)
        .returnBundle(Bundle.class)
        .execute();
    log.debug("results: {}", bundle.toString());

    List<T> theResources = new ArrayList<>();
    for (BundleEntryComponent entry : bundle.getEntry()) {
      theResources.add((T) entry.getResource());
    }
    return theResources;
  }

  public <T extends IBaseResource> List<T> searchTodaysDiagnosticOrders(Class<T> theClass) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1);

    Bundle bundle = fhirGenericClient.search()
        .forResource(theClass)
        .where(ServiceRequest.OCCURRENCE.after().day(DateUtil.dateFromUTC(cal.getTime())))
        .returnBundle(Bundle.class)
        .execute();
    log.debug("results: {}", bundle.toString());

    List<T> theResources = new ArrayList<>();
    for (BundleEntryComponent entry : bundle.getEntry()) {
      theResources.add((T) entry.getResource());
    }
    return theResources;
  }

  public <T extends IBaseResource> List<T> search(Class<T> theClass) {
    Bundle bundle = fhirGenericClient.search()
        .forResource(theClass)
        //.where(Patient.INCLUDE_LINK.matches().value( "Jason"))
        //.where( Patient.FAMILY.matches().value( "Argonaut"))
        .returnBundle(Bundle.class)
        .execute();
    log.debug("results: {}", bundle.toString());

    List<T> theResources = new ArrayList<>();
    for (BundleEntryComponent entry : bundle.getEntry()) {
      theResources.add((T) entry.getResource());
    }
    return theResources;
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
