package com.flowsigma.ewocs.fhir.model;

import lombok.Data;
import lombok.NoArgsConstructor;

public class PatientRecord {
	private String id;
	private String name;
	private String gender;
	private String birthDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "PatientRecord{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", gender='" + gender + '\'' +
				", birthDate='" + birthDate + '\'' +
				'}';
	}
}
