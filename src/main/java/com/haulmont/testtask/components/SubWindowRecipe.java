package com.haulmont.testtask.components;

import com.haulmont.testtask.components.interfaces.SubWindowFunctions;
import com.haulmont.testtask.daos.DAODoctor;
import com.haulmont.testtask.daos.DAOPatient;
import com.haulmont.testtask.daos.DAORecipe;
import com.haulmont.testtask.entities.Doctor;
import com.haulmont.testtask.entities.Patient;
import com.haulmont.testtask.entities.Recipe;
import com.haulmont.testtask.enums.Priority;
import com.haulmont.testtask.views.interfaces.PageView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubWindowRecipe extends Window implements SubWindowFunctions {
    private final TextArea descriptionText = new TextArea("Описание");
    private final TextField validityText = new TextField("Срок годности");
    private final DateField dateOfCreationField = new DateField("Дата создания");
    private final ComboBox<String> priorityBox = new ComboBox<>("Приоритет");
    private final Button saveButton = new Button("Ок");
    private final Button cancelButton = new Button("Отменить");
    private final Button clearFilterDoctorButton = new Button(VaadinIcons.CLOSE);
    private final Button clearFilterPatientButton = new Button(VaadinIcons.CLOSE);
    private final Label doctorLabel = new Label("Доктор");
    private final Label patientLabel = new Label("Пациент");

    private final VerticalLayout mainVerticalLayout = new VerticalLayout();
    private final HorizontalLayout mainHorizontalLayout = new HorizontalLayout();
    private final VerticalLayout subDoctorLayout = new VerticalLayout();
    private final VerticalLayout subRecipeLayout = new VerticalLayout();
    private final DAORecipe daoRecipe = new DAORecipe();
    private final DAODoctor daoDoctor = new DAODoctor();
    private final DAOPatient daoPatient = new DAOPatient();
    private final Grid<Doctor> doctorGrid = new Grid<>();
    private final Grid<Patient> patientGrid = new Grid<>();
    private final TextField filterDoctorText = new TextField();
    private final TextField filterPatientText = new TextField();
    private PageView pageView;
    private Patient patient;
    private Set<Doctor> doctors = new HashSet<>();
    private Set<Patient> patients = new HashSet<>();
    private Doctor doctor;
    private Recipe recipe;
    private Recipe ifNullRecipe;

    public SubWindowRecipe(PageView pageView) {
        super("Рецепт");
        center();
        setModal(true);
        entitiesInit();
        componentInit();
        saveButton.addClickListener(e -> this.save());
        this.pageView = pageView;
    }

    public SubWindowRecipe(PageView pageView, Recipe recipe) {
        super("Рецепт: Доктор: " + recipe.getDoctorFIO() + ", " + recipe.getDoctorSpecialization() +
                "; Пациент: " + recipe.getPatientFIO() + ", " + recipe.getPatientNumber());
        center();
        setModal(true);
        entitiesInit();
        componentInit();
        saveButton.addClickListener(e -> this.update());
        this.recipe = recipe;
        this.pageView = pageView;
        ifNullRecipe = new Recipe(
                this.recipe.getId(),
                this.recipe.getPatientId(),
                this.recipe.getDoctorId(),
                this.recipe.getDescription(),
                this.recipe.getDateOfCreation(),
                this.recipe.getValidity(),
                this.recipe.getPriority());
        descriptionText.setValue(recipe.getDescription());
        validityText.setValue(recipe.getValidity());
        priorityBox.setValue(recipe.getPriority());
        dateOfCreationField.setValue(recipe.getDateOfCreation().toLocalDate());
    }

    @Override
    public void save() {
        try {
            if (StringUtils.isBlank(descriptionText.getValue()) || StringUtils.isBlank(validityText.getValue())
                    || StringUtils.isBlank(priorityBox.getValue())) {
                Notification.show("Все поля обязательны!", Notification.Type.ERROR_MESSAGE);
            } else {
                recipe = new Recipe(patient.getId(), doctor.getId(), descriptionText.getValue(),
                        java.sql.Date.valueOf(dateOfCreationField.getValue()),
                        validityText.getValue(), priorityBox.getValue());
                daoRecipe.save(recipe);
                Notification.show("Редактирование успешно завершено!");
                pageView.updateList();
                close();
            }
        } catch (NullPointerException e) {
            Notification.show("Выберите пациента и доктора! Проверьте дату!", Notification.Type.ERROR_MESSAGE);
        }

    }

    @Override
    public void update() {
        if (patient == null) {
            patient = new Patient();
            patient.setId(ifNullRecipe.getPatientId());
        }
        if (doctor == null) {
            doctor = new Doctor();
            doctor.setId(ifNullRecipe.getDoctorId());
        }
        try {
            if (StringUtils.isBlank(descriptionText.getValue()) || StringUtils.isBlank(validityText.getValue())
                    || StringUtils.isBlank(priorityBox.getValue())) {
                Notification.show("Все поля обязательны!", Notification.Type.ERROR_MESSAGE);
            } else {
                recipe = new Recipe(recipe.getId(), patient.getId(), doctor.getId(), descriptionText.getValue(),
                        java.sql.Date.valueOf(dateOfCreationField.getValue()),
                        validityText.getValue(), priorityBox.getValue());
                daoRecipe.update(recipe);
                Notification.show("Редактирование успешно завершено!");
                pageView.updateList();
                close();
            }
        } catch (NullPointerException e) {
            Notification.show("Дата введена неправильно!", Notification.Type.ERROR_MESSAGE);
        }
    }

    @Override
    public void componentInit() {
        setContent(mainVerticalLayout);
        priorityBox.setItems(Stream.of(Priority.values()).map(Enum::toString).collect(Collectors.toList()));
        doctorGrid.addColumn(Doctor::getId).setCaption("id").setHidden(true);
        doctorGrid.addColumn(Doctor::getName).setCaption("Имя");
        doctorGrid.addColumn(Doctor::getSurname).setCaption("Фамилия");
        doctorGrid.addColumn(Doctor::getPatronymic).setCaption("Отчество");
        doctorGrid.addColumn(Doctor::getSpecialization).setCaption("Специализация");
        doctorGrid.setItems(doctors);
        doctorGrid.setHeight("170px");
        doctorGrid.asSingleSelect().addValueChangeListener(e -> doctor = e.getValue());

        filterDoctorText.setPlaceholder("Поиск по фамилии");
        filterDoctorText.addValueChangeListener(e -> updateListDoctor());
        filterDoctorText.setValueChangeMode(ValueChangeMode.LAZY);

        clearFilterDoctorButton.addClickListener(e -> filterDoctorText.clear());

        HorizontalLayout filteringDoctor = new HorizontalLayout();
        CssLayout filteringDoctorCSS = new CssLayout();
        filteringDoctorCSS.addComponents(filterDoctorText, clearFilterDoctorButton);
        filteringDoctorCSS.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filteringDoctor.addComponents(doctorLabel, filteringDoctorCSS);

        subDoctorLayout.addComponent(filteringDoctor);
        subDoctorLayout.addComponent(doctorGrid);


        patientGrid.addColumn(Patient::getId).setCaption("id").setHidden(true);
        patientGrid.addColumn(Patient::getName).setCaption("Имя");
        patientGrid.addColumn(Patient::getSurname).setCaption("Фамилия");
        patientGrid.addColumn(Patient::getPatronymic).setCaption("Отчество");
        patientGrid.addColumn(Patient::getNumber).setCaption("Номер");
        patientGrid.setItems(patients);
        patientGrid.setHeight("170px");
        patientGrid.asSingleSelect().addValueChangeListener(e -> patient = e.getValue());

        filterPatientText.setPlaceholder("Поиск по фамилии");
        filterPatientText.addValueChangeListener(e -> updateListPatient());
        filterPatientText.setValueChangeMode(ValueChangeMode.LAZY);

        clearFilterPatientButton.addClickListener(e -> filterPatientText.clear());

        HorizontalLayout filteringPatient = new HorizontalLayout();
        CssLayout filteringPatientCSS = new CssLayout();
        filteringPatientCSS.addComponents(filterPatientText, clearFilterPatientButton);
        filteringPatientCSS.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filteringPatient.addComponents(patientLabel, filteringPatientCSS);

        subDoctorLayout.addComponents(filteringPatient, patientGrid);

        descriptionText.setWidth("260px");
        descriptionText.setHeight("170px");

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        subRecipeLayout.addComponents(descriptionText, priorityBox, dateOfCreationField, validityText, buttonLayout);

        mainHorizontalLayout.addComponents(subDoctorLayout, subRecipeLayout);
        mainHorizontalLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        setClosable(false);
        setResizable(false);
        mainVerticalLayout.addComponents(mainHorizontalLayout);
        cancelButton.addClickListener(e -> close());
    }/*
    @Override
    public void componentInit() {
        setContent(mainVerticalLayout);
        priorityBox.setItems(Stream.of(Priority.values()).map(Enum::toString).collect(Collectors.toList()));
        doctorGrid.addColumn(Doctor::getId).setCaption("id").setHidden(true);
        doctorGrid.addColumn(Doctor::getName).setCaption("First name");
        doctorGrid.addColumn(Doctor::getSurname).setCaption("Last name");
        doctorGrid.addColumn(Doctor::getPatronymic).setCaption("Patronymic");
        doctorGrid.addColumn(Doctor::getSpecialization).setCaption("Specialization");
        doctorGrid.setItems(doctors);
        doctorGrid.setWidth("300px");
        doctorGrid.asSingleSelect().addValueChangeListener(e -> doctor = e.getValue());

        filterDoctorText.setPlaceholder("Поиск по фамилии");
        filterDoctorText.addValueChangeListener(e -> updateListDoctor());
        filterDoctorText.setValueChangeMode(ValueChangeMode.LAZY);

        clearFilterDoctorButton.addClickListener(e -> filterDoctorText.clear());

        HorizontalLayout filteringDoctor = new HorizontalLayout();
        CssLayout filteringDoctorCSS = new CssLayout();
        filteringDoctorCSS.addComponents(filterDoctorText, clearFilterDoctorButton);
        filteringDoctorCSS.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filteringDoctor.addComponents(doctorLabel, filteringDoctorCSS);

        subDoctorLayout.addComponent(filteringDoctor);
        subDoctorLayout.addComponent(doctorGrid);


        patientGrid.addColumn(Patient::getId).setCaption("id").setHidden(true);
        patientGrid.addColumn(Patient::getName).setCaption("First name");
        patientGrid.addColumn(Patient::getSurname).setCaption("Last name");
        patientGrid.addColumn(Patient::getPatronymic).setCaption("Patronymic");
        patientGrid.addColumn(Patient::getNumber).setCaption("Number");
        patientGrid.setItems(patients);
        patientGrid.setWidth("300px");
        patientGrid.asSingleSelect().addValueChangeListener(e -> patient = e.getValue());

        filterPatientText.setPlaceholder("Поиск по фамилии");
        filterPatientText.addValueChangeListener(e -> updateListPatient());
        filterPatientText.setValueChangeMode(ValueChangeMode.LAZY);

        clearFilterPatientButton.addClickListener(e -> filterPatientText.clear());

        HorizontalLayout filteringPatient = new HorizontalLayout();
        CssLayout filteringPatientCSS = new CssLayout();
        filteringPatientCSS.addComponents(filterPatientText, clearFilterPatientButton);
        filteringPatientCSS.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        filteringPatient.addComponents(patientLabel, filteringPatientCSS);

        subPatientLayout.addComponents(filteringPatient, patientGrid);

        descriptionText.setWidth("260px");
        descriptionText.setHeight("170px");

        HorizontalLayout subRecipeForDataLayout = new HorizontalLayout();

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        subRecipeLayout.addComponents(descriptionText, priorityBox, dateOfCreationField, validityText, buttonLayout);

        mainHorizontalLayout.addComponents(subDoctorLayout, subPatientLayout, subRecipeLayout);
        mainHorizontalLayout.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        mainHorizontalLayout.setWidth("1000px");

        setClosable(false);
        setResizable(false);
        mainVerticalLayout.addComponents(mainHorizontalLayout);
        cancelButton.addClickListener(e -> close());
    }*/

    private void updateListDoctor() {
        doctors = daoDoctor.getAll(filterDoctorText.getValue());
        doctorGrid.setItems(doctors);
    }

    private void updateListPatient() {
        patients = daoPatient.getAll(filterPatientText.getValue());
        patientGrid.setItems(patients);
    }

    public void entitiesInit() {
        doctors = daoDoctor.getAll();
        patients = daoPatient.getAll();
    }
}
