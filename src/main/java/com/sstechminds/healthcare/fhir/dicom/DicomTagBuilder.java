package com.sstechminds.healthcare.fhir.dicom;

import com.sstechminds.healthcare.fhir.model.DicomTags;
import java.io.File;
import java.util.Map;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;
import org.springframework.stereotype.Component;

@Component
public class DicomTagBuilder {

  public DicomTags build(String filePath)  {
    return build(DicomTagLoader.loadTags(new File(filePath)));
  }

  public DicomTags build(DicomObject tags) {
    DicomTags dicomTags = new DicomTags();

    dicomTags.setPatientID(tags.getString(Tag.PatientID));
    dicomTags.setAccessionNumber(tags.getString(Tag.AccessionNumber));

    return dicomTags;
  }

  public DicomTags build(Map<Object, Object> tags) {
    DicomTags dicomTags = new DicomTags();

    dicomTags.setPatientID((String) tags.get(Tag.PatientID));
    dicomTags.setAccessionNumber((String)tags.get(Tag.AccessionNumber));

    return dicomTags;
  }
}
