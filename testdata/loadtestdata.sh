#!/bin/bash

# https://tools.knowledgewalls.com/jsontostring
#curl -X POST http://hackathon.siim.org/fhir/Patient -H 'apikey:dd6f7f1d-1586-438f-8d35-ff589a12f4df' -H "Content-Type: application/fhir+json" -d "{ \"resourceType\": \"Patient\" , \"id\": \"post-curl\" , \"name\": [ { \"family\": \"GUMERCINDO\", \"given\": [ \"ALMEIDA\", \"PEREIRA\" ] } ] , \"gender\": \"male\" , \"birthDate\": \"1970-01-21\"}"

curl -X PUT http://hackathon.siim.org/fhir/Patient/siimravi2 -H 'apikey:dd6f7f1d-1586-438f-8d35-ff589a12f4df' -H "Content-Type: application/fhir+json"  --data @testdata/files/patient.json
curl -X PUT http://hackathon.siim.org/fhir/ServiceRequest/siimravi2diagnosticorder -H 'apikey:dd6f7f1d-1586-438f-8d35-ff589a12f4df' -H 'Content-type: application/json+fhir' --data @testdata/files/servicerequest.json
curl -X PUT http://hackathon.siim.org/fhir/Observation/siimravi2observation -H 'apikey:dd6f7f1d-1586-438f-8d35-ff589a12f4df' -H 'Content-type: application/json+fhir' --data @testdata/files/observation.json
curl -X PUT http://hackathon.siim.org/fhir/DiagnosticReport/siimravi2diagnosticreport -H 'apikey:dd6f7f1d-1586-438f-8d35-ff589a12f4df' -H 'Content-type: application/json+fhir' --data @testdata/files/diagnosticreport.json
