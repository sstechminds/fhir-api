package com.flowsigma.ewocs.fhir.dicom;

import com.flowsigma.ewocs.fhir.model.DicomTags;
import java.io.File;
import java.io.IOException;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.springframework.stereotype.Component;

@Component
public class DicomTagBuilder {

  public DicomTags build(String filePath) throws IOException {
    return build(DicomTagLoader.loadTags(new File(filePath)));
  }

  public DicomTags build(DicomObject tags) {
    DicomTags dicomTags = new DicomTags();

    dicomTags.setPatientID(tags.getString(Tag.PatientID));
    dicomTags.setAccessionNumber(tags.getString(Tag.AccessionNumber));

    return dicomTags;
  }
}
