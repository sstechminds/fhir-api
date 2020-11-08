package com.flowsigma.ewocs.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.json.JSONArray;
import org.json.JSONObject;

public class SerDe {

  public static FhirContext fhirContext = FhirContext.forR4();
  public static ObjectMapper objectMapper = new ObjectMapper();

  public static <T extends IBaseResource> String fhirSerialization(List<T> resources) {
    IParser jsonParser = fhirContext.newJsonParser();

    JSONArray array = new JSONArray();
    resources.stream()
        .map(jsonParser::encodeResourceToString)
        .map(JSONObject::new)
        .forEach(array::put);
    return serialize(array.toList());
  }

  public static <T extends IBaseResource> String fhirSerialization(T resource) {
    IParser jsonParser = fhirContext.newJsonParser();
    jsonParser.setPrettyPrint(true);

    return jsonParser.encodeResourceToString(resource);
  }

  public static String serialize(Object nonFhirResource) {
    try {
      return objectMapper.writeValueAsString(nonFhirResource);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
