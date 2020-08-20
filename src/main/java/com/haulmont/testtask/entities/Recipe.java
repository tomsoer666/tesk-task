package com.haulmont.testtask.entities;

import com.haulmont.testtask.entities.interfaces.DataBaseEntity;

import java.sql.Date;

public class Recipe implements DataBaseEntity {
    private long id;
    private long patientId;
    private long doctorId;
    private String description;
    private String patientFIO;
    private String patientNumber;
    private String doctorFIO;
    private String doctorSpecialization;
    private Date dateOfCreation;
    private String validity;
    private String priority;

    public Recipe(long id, long patientId, long doctorId, String description, String patientFIO, String patientNumber, String doctorFIO, String doctorSpecialization, Date dateOfCreation, String validity, String priority) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.description = description;
        this.patientFIO = patientFIO;
        this.patientNumber = patientNumber;
        this.doctorFIO = doctorFIO;
        this.doctorSpecialization = doctorSpecialization;
        this.dateOfCreation = dateOfCreation;
        this.validity = validity;
        this.priority = priority;
    }
    public Recipe(long id, long patientId, long doctorId, String description, Date dateOfCreation, String validity, String priority) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.description = description;
        this.dateOfCreation = dateOfCreation;
        this.validity = validity;
        this.priority = priority;
    }
    public Recipe(long patientId, long doctorId, String description, Date dateOfCreation, String validity, String priority) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.description = description;
        this.dateOfCreation = dateOfCreation;
        this.validity = validity;
        this.priority = priority;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPatientFIO() {
        return patientFIO;
    }

    public void setPatientFIO(String patientFIO) {
        this.patientFIO = patientFIO;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public String getDoctorFIO() {
        return doctorFIO;
    }

    public void setDoctorFIO(String doctorFIO) {
        this.doctorFIO = doctorFIO;
    }

    public String getDoctorSpecialization() {
        return doctorSpecialization;
    }

    public void setDoctorSpecialization(String doctorSpecialization) {
        this.doctorSpecialization = doctorSpecialization;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
