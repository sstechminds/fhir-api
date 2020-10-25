#!/bin/bash

curl -X GET 'http://hackathon.siim.org/fhir/Patient' -H 'apikey:dd6f7f1d-1586-438f-8d35-ff589a12f4df' -H 'Content-type: application/json+fhir'
curl -X GET 'http://hackathon.siim.org/fhir/ServiceRequest?patient=siimravi' -H 'apikey:dd6f7f1d-1586-438f-8d35-ff589a12f4df' -H 'Content-type: application/json+fhir'
curl -X GET 'http://hackathon.siim.org/fhir/DiagnosticReport?patient=siimravi' -H 'apikey:dd6f7f1d-1586-438f-8d35-ff589a12f4df' -H 'Content-type: application/json+fhir'





