package com.haulmont.testtask.entities;

import com.haulmont.testtask.entities.interfaces.DataBaseEntity;

public class Patient implements DataBaseEntity {
    private long id;
    private String name;
    private String surname;
    private String patronymic;
    private String number;

    public Patient(long id, String name, String surname, String patronymic, String number) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.number = number;
    }

    public Patient() {
    }

    public Patient(String name, String surname, String patronymic, String number) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.number = number;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
