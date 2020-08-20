package com.haulmont.testtask.entities;

import com.haulmont.testtask.entities.interfaces.DataBaseEntity;

public class Doctor implements DataBaseEntity {
    private long id;
    private String name;
    private String surname;
    private String patronymic;
    private String specialization;

    public Doctor(long id, String name, String surname, String patronymic, String specialization) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.specialization = specialization;
    }
    public Doctor(String name, String surname, String patronymic, String specialization) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.specialization = specialization;
    }

    public Doctor() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
