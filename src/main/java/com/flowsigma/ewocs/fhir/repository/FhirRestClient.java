package com.flowsigma.ewocs.fhir.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.AdditionalRequestHeadersInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FhirRestClient {
  private static final String API_KEY = "apikey";

  private String fhirHostUrl;
  private String fhirApiKey;
  private FhirContext fhirContext;

  public FhirRestClient(@Value("${spring.fhir.host}") String fhirHostUrl,
      @Value("${spring.fhir.apiKey}") String fhirApiKey) {
    this.fhirHostUrl = fhirHostUrl;
    this.fhirApiKey = fhirApiKey;
    this.fhirContext = FhirContext.forR4();
  }

  public IGenericClient getGenericClient() {
    // https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-client-okhttp/src/test/java/ca/uhn/fhir/okhttp/GenericOkHttpClientDstu2Test.java

    //fhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

    IGenericClient client = fhirContext.newRestfulGenericClient(fhirHostUrl);

    // https://smilecdr.com/hapi-fhir/docs/interceptors/built_in_client_interceptors.html
    AdditionalRequestHeadersInterceptor interceptor = new AdditionalRequestHeadersInterceptor();
    interceptor.addHeaderValue(API_KEY, fhirApiKey);
    client.registerInterceptor(interceptor);

    return client;
  }

  public RestTemplate getRestTemplate() {
    return new RestTemplateBuilder(rt -> rt.getInterceptors().add((request, body, execution) -> {
      request.getHeaders().add(API_KEY, fhirApiKey);
      return execution.execute(request, body);
    })).build();
  }
}
