package com.flowsigma.ewocs.fhir.service;

import com.flowsigma.ewocs.fhir.repository.FhirRepository;
import org.junit.jupiter.api.Test;

class FhirPatientServiceTest {

    FhirRepository repo = new FhirRepository( "http://hackathon.siim.org/fhir/","dd6f7f1d-1586-438f-8d35-ff589a12f4df");


    @Test
    void testGetPatients() {
        //    List<Map<String, String>> patients = new FhirPatientService().getPatients("Patient");
//    System.out.printf("Patients: \n" + patients.toString());
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