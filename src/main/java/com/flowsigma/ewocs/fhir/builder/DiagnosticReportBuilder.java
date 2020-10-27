package com.flowsigma.ewocs.fhir.builder;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DiagnosticReport.DiagnosticReportStatus;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Identifier.IdentifierUse;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Reference;

/**
 * @see <a href="https://www.hl7.org/fhir/diagnosticreport.html">DiagnosticReport</a>
 */
public class DiagnosticReportBuilder {

    public DiagnosticReport build(String patientID) {
        DiagnosticReport dr = new DiagnosticReport();
        dr.setId(UUID.randomUUID().toString());

        Meta meta = new Meta();
        meta.setVersionId("1");
        dr.setMeta(meta);
        
        List<Identifier> identifiers = new ArrayList<>();
        Identifier identifier = new Identifier();
        identifier.setUse(IdentifierUse.USUAL);
        identifier.setSystem("http://flowsigma.com");
        identifier.setValue(UUID.randomUUID().toString());
        identifiers.add(identifier);
        dr.setIdentifier(identifiers);
        
        dr.setStatus(DiagnosticReportStatus.FINAL);

        List<CodeableConcept> cccs = new ArrayList<>();
        CodeableConcept ccc = new CodeableConcept();
        List<Coding> theCoding2 = new ArrayList<>();
        Coding coding2 = new Coding();
        coding2.setSystem("http://hl7.org/fhir/v2/0074");
        coding2.setCode("RAD");
        theCoding2.add(coding2);
        ccc.setCoding(theCoding2);
        cccs.add(ccc);
        dr.setCategory(cccs);

        CodeableConcept cc = new CodeableConcept();
        List<Coding> theCoding = new ArrayList<>();
        Coding coding = new Coding();
        coding.setSystem("http://flowsigma.com");
        coding.setCode("24627-2");
        theCoding.add(coding);
        cc.setCoding(theCoding);
        cc.setText("CT Chest");
        dr.setCode(cc);
        
        dr.setSubject(new Reference("Patient/" + patientID));
        dr.setEffective(new DateTimeType(new Date()));
        dr.setIssued(getCurrentUTCDate());

        List<Reference> performers = new ArrayList<>();
        Reference performerRef = new Reference();
        performerRef.setReference("Organization/siim");
        performerRef.setDisplay("Society of Imaging Informatics in Medicine");
        performers.add(performerRef);
        dr.setPerformer(performers);

        List<Reference> imagingStudies = new ArrayList<>();
        Reference imageStudyRef = new Reference();
        imageStudyRef.setReference("imageStudyRef");
        imageStudyRef.setDisplay("FlowSIGMA DICOM image analytics");
        imagingStudies.add(imageStudyRef);
        dr.setImagingStudy(imagingStudies);

        List<Reference> results = new ArrayList<>();
        Reference resultRef = new Reference();
        resultRef.setReference("Observation/sample02ob03"); //TODO: Create Observation
        resultRef.setDisplay("FlowSIGMA DICOM analytic results");
        results.add(resultRef);
        dr.setResult(results);

//  Create Specimen reference Ex: refer https://github.com/synthetichealth/gofhir/issues/29
//        List<Reference> specimens = new ArrayList<>();
//        Reference specimenRef = new Reference();
//        specimenRef.setReference("specimenRef");
//        specimenRef.setDisplay("FlowSIGMA DICOM analytic specimen");
//        specimens.add(specimenRef);
//        dr.setSpecimen(specimens);

        dr.setConclusion("conclusion");

        return dr;
    }

    private Date getCurrentUTCDate() {
        //DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(LocalDateTime.now());
        OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.UTC);
        Date currentDate = Date.from(utc.toInstant());
        return currentDate;
    }
}
