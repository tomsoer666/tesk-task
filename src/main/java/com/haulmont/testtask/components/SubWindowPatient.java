package com.haulmont.testtask.components;

import com.haulmont.testtask.components.interfaces.SubWindowFunctions;
import com.haulmont.testtask.daos.DAOPatient;
import com.haulmont.testtask.entities.Patient;
import com.haulmont.testtask.views.interfaces.PageView;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;

public class SubWindowPatient extends Window implements SubWindowFunctions {
    private final Button saveButton = new Button("Ок");
    private final Button cancelButton = new Button("Отменить");
    private final DAOPatient daoPatient = new DAOPatient();
    private TextField nameText = new TextField("Имя");
    private TextField surnameText = new TextField("Фамилия");
    private TextField patronymicText = new TextField("Отчество");
    private TextField numberText = new TextField("Номер телефона");
    private VerticalLayout subContent = new VerticalLayout();
    private PageView pageView;
    private Patient patient;

    public SubWindowPatient(PageView pageView) {
        super("Пациент");
        center();
        setModal(true);
        componentInit();
        saveButton.addClickListener(e -> this.save());
        this.pageView = pageView;
    }

    public SubWindowPatient(PageView pageView, Patient patient) {
        super("Пациент");
        center();
        setModal(true);
        componentInit();
        saveButton.addClickListener(e -> this.update());
        this.pageView = pageView;
        this.patient = patient;
        nameText.setValue(patient.getName());
        surnameText.setValue(patient.getSurname());
        patronymicText.setValue(patient.getPatronymic());
        numberText.setValue(patient.getNumber());
    }

    @Override
    public void save() {
        if (StringUtils.isBlank(nameText.getValue()) || StringUtils.isBlank(surnameText.getValue())
                || StringUtils.isBlank(patronymicText.getValue()) || StringUtils.isBlank(numberText.getValue())) {
            Notification.show("Заполните все поля!",
                    Notification.Type.ERROR_MESSAGE);
        } else {
            if (!numberText.getValue().matches("(\\+*)\\d{5,14}")) {
                Notification.show("Неправильно набран номер! Должны быть только цифры!",
                        Notification.Type.ERROR_MESSAGE);
            } else {
                patient = new Patient(nameText.getValue(), surnameText.getValue(), patronymicText.getValue(),
                        (numberText.getValue()));
                daoPatient.save(patient);
                Notification.show("Добавление успешно завершено!");
                pageView.updateList();
                close();
            }
        }

    }

    @Override
    public void update() {
        if (StringUtils.isBlank(nameText.getValue()) || StringUtils.isBlank(surnameText.getValue())
                || StringUtils.isBlank(patronymicText.getValue()) || StringUtils.isBlank(numberText.getValue())) {
            Notification.show("Заполните все поля!",
                    Notification.Type.ERROR_MESSAGE);
        } else {
            if (!numberText.getValue().matches("(\\+*)\\d{5,14}")) {
                Notification.show("Неправильно набран номер! Должны быть только цифры!",
                        Notification.Type.ERROR_MESSAGE);
            } else {
                patient = new Patient(patient.getId(), nameText.getValue(), surnameText.getValue(), patronymicText.getValue(),
                        numberText.getValue());
                daoPatient.update(patient);
                Notification.show("Редактирование успешно завершено!");
                pageView.updateList();
                close();
            }
        }

    }

    @Override
    public void componentInit() {
        nameText.setMaxLength(50);
        surnameText.setMaxLength(50);
        patronymicText.setMaxLength(50);
        numberText.setMaxLength(15);
        setContent(subContent);
        subContent.addComponents(nameText, surnameText, patronymicText, numberText,
                new HorizontalLayout(saveButton, cancelButton));
        cancelButton.addClickListener(e -> close());
        setClosable(false);
        setResizable(false);
    }
}
