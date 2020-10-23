package com.flowsigma.ewocs.fhir.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.flowsigma.ewocs.fhir.model.PatientRecord;
import com.flowsigma.ewocs.fhir.repository.FhirRepository;
import com.flowsigma.ewocs.fhir.util.FileResourcesUtils;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class FhirPatientServiceTest {

    @Mock
    private FhirRepository fhirRepository;

    private FhirPatientService helloService;

    Gson gson = new Gson();

    File patientsFile = new FileResourcesUtils().getFileAsStringFromResource("testdata/patients.json");

    @BeforeEach
    void setMockOutput() throws FileNotFoundException {
        helloService = new FhirPatientService(fhirRepository);

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
//    String patientID = "siimravi";
//    String patientCondition = new FhirPatientService().getPatientCondition("Condition?patient=" + patientID);
//    System.out.print("patientConditions: \n" + patientCondition);
    }

    @Test
    void testGetPatientDiagnosticReport() {
        //    String patientID = "siimravi";
//    String diagnosticReport = new FhirPatientService().getPatientDiagnosticReports("DiagnosticReport?patient=" + patientID);
//    System.out.print("diagnosticReports: \n" + diagnosticReport);
    }

    @Test
    void testGetDiagnosticReport() {
        //    String patientID = "siimravi";
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