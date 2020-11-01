package com.flowsigma.ewocs.fhir.dicom;

import static org.junit.jupiter.api.Assertions.*;

import com.flowsigma.ewocs.fhir.model.DicomTags;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

@SpringBootTest
public class DicomTagBuilderTest {

  @Autowired DicomTagBuilder dicomTagBuilder;

  @Test
  void build() throws IOException {
    String dicomFilePath = new ClassPathResource("dicom/ImageWithAccession.dcm").getFile().getAbsolutePath();

    DicomTags dicomTags = dicomTagBuilder.build(dicomFilePath);

    assertEquals("123456789", dicomTags.getAccessionNumber());
  }
}