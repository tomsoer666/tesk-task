package com.haulmont.testtask.components;

import com.haulmont.testtask.components.interfaces.SubWindowFunctions;
import com.haulmont.testtask.daos.DAODoctor;
import com.haulmont.testtask.entities.Doctor;
import com.haulmont.testtask.views.interfaces.PageView;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;

public class SubWindowDoctor extends Window implements SubWindowFunctions {
    private final DAODoctor daoDoctor;
    private final TextField nameText = new TextField("Имя");
    private final TextField surnameText = new TextField("Фамилия");
    private final TextField patronymicText = new TextField("Отчество");
    private final TextField specializationText = new TextField("Специализация");
    private final Button saveButton = new Button("Ок");
    private final Button cancelButton = new Button("Отменить");
    private final VerticalLayout subContent = new VerticalLayout();
    private final PageView pageView;
    private Doctor doctor;

    public SubWindowDoctor(PageView pageView) {
        super("Доктор");
        setModal(true);
        center();
        daoDoctor = new DAODoctor();
        componentInit();
        saveButton.addClickListener(e -> this.save());
        this.pageView = pageView;
    }

    public SubWindowDoctor(PageView pageView, Doctor doctor) {
        super("Доктор");
        setModal(true);
        center();
        daoDoctor = new DAODoctor();
        componentInit();
        saveButton.addClickListener(e -> this.update());
        this.doctor = doctor;
        this.pageView = pageView;
        nameText.setValue(doctor.getName());
        surnameText.setValue(doctor.getSurname());
        patronymicText.setValue(doctor.getPatronymic());
        specializationText.setValue(doctor.getSpecialization());
    }

    @Override
    public void save() {
        if ((StringUtils.isBlank(nameText.getValue()) || StringUtils.isBlank(surnameText.getValue())
                || StringUtils.isBlank(patronymicText.getValue()) || StringUtils.isBlank(specializationText.getValue()))) {
            Notification.show("Заполните все поля!",
                    Notification.Type.ERROR_MESSAGE);
        } else {
            doctor = new Doctor(nameText.getValue(), surnameText.getValue(), patronymicText.getValue(),
                    specializationText.getValue());
            daoDoctor.save(doctor);
            Notification.show("Добавление успешно завершено!");
            pageView.updateList();
            close();
        }
    }

    @Override
    public void update() {
        if ((StringUtils.isBlank(nameText.getValue()) || StringUtils.isBlank(surnameText.getValue())
                || StringUtils.isBlank(patronymicText.getValue()) || StringUtils.isBlank(specializationText.getValue()))) {
            Notification.show("Заполните все поля!",
                    Notification.Type.ERROR_MESSAGE);
        } else {
            doctor = new Doctor(doctor.getId(), nameText.getValue(), surnameText.getValue(), patronymicText.getValue(),
                    specializationText.getValue());
            daoDoctor.update(doctor);
            Notification.show("Редактирование успешно завершено!");
            pageView.updateList();
            close();
        }
    }

    @Override
    public void componentInit() {
        nameText.setMaxLength(50);
        surnameText.setMaxLength(50);
        patronymicText.setMaxLength(50);
        specializationText.setMaxLength(150);
        setContent(subContent);
        subContent.addComponents(nameText, surnameText, patronymicText, specializationText,
                new HorizontalLayout(saveButton, cancelButton));
        cancelButton.addClickListener(e -> close());
        setClosable(false);
        setResizable(false);
    }
}
