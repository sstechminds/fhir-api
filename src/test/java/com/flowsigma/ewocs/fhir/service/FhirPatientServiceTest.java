package com.flowsigma.ewocs.fhir.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.flowsigma.ewocs.fhir.model.PatientRecord;
import com.flowsigma.ewocs.fhir.repository.FhirRepository;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@SpringBootTest
class FhirPatientServiceTest {
    Gson gson = new Gson();
    Resource patientsFileResource = new ClassPathResource("testdata/patients.json");

    @Mock private FhirRepository fhirRepository;

    private FhirPatientService helloService;

    @BeforeEach
    void setMockOutput() throws IOException {
        helloService = new FhirPatientService(fhirRepository);

        File patientsFile = patientsFileResource.getFile();
        Object patients = gson.fromJson(new FileReader(patientsFile), Object.class);
        when(fhirRepository.getResponse("/patient")).thenReturn(gson.toJson(patients));
    }

    @Test
    void testGetPatients() {
        List<PatientRecord> patients = helloService.getPatients("/patient");
        assertEquals(15, patients.size());
    }

    @Test
    void testGetPatientCondition() {
        //    //https://github.com/jamesagnew/hapi-fhir/blob/master/hapi-fhir-structures-dstu3/src/test/java/ca/uhn/fhir/parser/JsonParserDstu3Test.java
//    String patientID = "siimravi2";
//    String patientCondition = new FhirPatientService().getPatientCondition("Condition?patient=" + patientID);
//    System.out.print("patientConditions: \n" + patientCondition);
    }

    @Test
    void testGetPatientDiagnosticReport() {
        //    String patientID = "siimravi2";
//    String diagnosticReport = new FhirPatientService().getPatientDiagnosticReports("DiagnosticReport?patient=" + patientID);
//    System.out.print("diagnosticReports: \n" + diagnosticReport);
    }

    @Test
    void testGetDiagnosticReport() {
        //    String patientID = "siimravi2";
//    String diagnosticReportID = "a819497684894128";
//    String diagnosticReport = new FhirPatientService(new FhirRepository()).getPatientDiagnosticReport( "DiagnosticReport/" + diagnosticReportID);
//    System.out.print("diagnosticReport: \n" + diagnosticReport);
    }

    @Test
    void testGetPatientDiagnosticReports() {
        //    String diagnosticReport2 = new FhirPatientService().getPatientDiagnosticReports(FHIR_HOST_URL + "DiagnosticReport?patient=" + patientID + "%26report=" + diagnosticReportID);
//    System.out.print("diagnosticReport2: \n" + diagnosticReport2);
    }

    @Test
    void testGetImagingStudy() {
        //    String studyID = "a819497684894127";
//    String imagingStudy = new FhirPatientService().getImagingStudy("ImagingStudy?_id=" + studyID);
//    System.out.print("imagingStudy: \n" + imagingStudy);
    }
}