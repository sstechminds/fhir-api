#!/bin/bash

# https://tools.knowledgewalls.com/jsontostring
#curl -X POST http://hackathon.siim.org/fhir/Patient -H 'apikey:dd6f7f1d-1586-438f-8d35-ff589a12f4df' -H "Content-Type: application/fhir+json" -d "{ \"resourceType\": \"Patient\" , \"id\": \"post-curl\" , \"name\": [ { \"family\": \"GUMERCINDO\", \"given\": [ \"ALMEIDA\", \"PEREIRA\" ] } ] , \"gender\": \"male\" , \"birthDate\": \"1970-01-21\"}"

curl -X POST http://hackathon.siim.org/fhir/Patient -H 'apikey:dd6f7f1d-1586-438f-8d35-ff589a12f4df' -H "Content-Type: application/fhir+json"  --data @files/patient.json
curl -X POST http://hackathon.siim.org/fhir/ServiceRequest -H 'apikey:dd6f7f1d-1586-438f-8d35-ff589a12f4df' -H 'Content-type: application/json+fhir' --data @files/servicerequest.json
curl -X POST http://hackathon.siim.org/fhir/DiagnosticReport -H 'apikey:dd6f7f1d-1586-438f-8d35-ff589a12f4df' -H 'Content-type: application/json+fhir' --data @files/diagnosticreport.json
