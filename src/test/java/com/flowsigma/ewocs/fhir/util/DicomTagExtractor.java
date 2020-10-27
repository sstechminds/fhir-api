package com.flowsigma.ewocs.fhir.util;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;

import java.io.File;
import java.io.IOException;

public class DicomTagExtractor {

    public static void main(String[] args) {
        String dicomFilePath = "src/test/resources/dicom/ImageWithAccession.dcm";

        DicomObject dataset = new DicomTagExtractor().getTags(dicomFilePath);
        //System.out.println("dataset" + dataset);
        System.out.println("Patient ID:" + new String(dataset.get(Tag.PatientID).getBytes()));
        System.out.println("Patient Name:" + new String(dataset.get(Tag.PatientName).getBytes()));
        System.out.println("Accession Number:" + new String(dataset.get(Tag.AccessionNumber).getBytes()));
    }

    public String getPatientId(String filePath) {
        DicomObject tags = getTags(filePath);
        return tags.getString(Tag.PatientID);
    }

    public String getAccessionNumber(String filePath) {
        return getExamId(filePath);
    }

    public String getExamId(String filePath) {
        DicomObject tags = getTags(filePath);
        return tags.getString(Tag.AccessionNumber);
    }

    public DicomObject getTags(String filePath) {
        try {
            return TagLoader.loadTags(new File(filePath));
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }
}
