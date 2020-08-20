package com.haulmont.testtask.views;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.components.NavigationMenuBar;
import com.haulmont.testtask.components.SubWindowDeleteAccept;
import com.haulmont.testtask.components.SubWindowPatient;
import com.haulmont.testtask.daos.DAOPatient;
import com.haulmont.testtask.entities.Patient;
import com.haulmont.testtask.views.interfaces.PageView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;


public class PatientView extends VerticalLayout implements View, PageView {
    private final TextField filterPatientText;
    private final Button clearFilterPatientButton;
    private final Grid<Patient> patientGrid;
    private final DAOPatient daoPatient;
    private final NavigationMenuBar navigateMenuBar;
    private final HorizontalLayout horizontalLayout;
    private final CssLayout filteringPatientCSS;
    private final Button addButton;
    private final Button updateButton;
    private final Button deleteButton;
    private Patient patient;


    public PatientView(MainUI myUI) {
        navigateMenuBar = new NavigationMenuBar(myUI);
        daoPatient = new DAOPatient();
        horizontalLayout = new HorizontalLayout();
        filterPatientText = new TextField();
        clearFilterPatientButton = new Button(VaadinIcons.CLOSE);

        filterPatientText.setPlaceholder("Поиск по фамилии");
        filterPatientText.addValueChangeListener(e -> updateListPatient());
        filterPatientText.setValueChangeMode(ValueChangeMode.LAZY);

        clearFilterPatientButton.addClickListener(e -> filterPatientText.clear());

        filteringPatientCSS = new CssLayout();
        filteringPatientCSS.addComponents(filterPatientText, clearFilterPatientButton);
        filteringPatientCSS.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        addButton = new Button("Добавить",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        SubWindowPatient subWindow = new SubWindowPatient(PatientView.this);
                        UI.getCurrent().addWindow(subWindow);
                    }
                });
        updateButton = new Button("Редактировать",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        try {
                            SubWindowPatient subWindow = new SubWindowPatient(PatientView.this, patient);
                            UI.getCurrent().addWindow(subWindow);
                        } catch (java.lang.NullPointerException exception) {
                            Notification.show("Выберете строку для редактирования!", ERROR_MESSAGE);
                        }
                    }
                });
        deleteButton = new Button("Удалить",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        if (patient == null) {
                            Notification.show("Выберете строку для удаления!", ERROR_MESSAGE);
                        } else {
                            SubWindowDeleteAccept subWindow = new SubWindowDeleteAccept(PatientView.this,
                                    patient, daoPatient);
                            UI.getCurrent().addWindow(subWindow);
                        }
                    }
                });

        addButton.setWidth("150px");
        updateButton.setWidth("150px");
        deleteButton.setWidth("150px");
        patientGrid = new Grid<>();
        patientGrid.asSingleSelect().addValueChangeListener(e -> patient = e.getValue());
        patientGrid.addColumn(Patient::getId).setCaption("id").setHidden(true);
        patientGrid.addColumn(Patient::getName).setCaption("Имя");
        patientGrid.addColumn(Patient::getSurname).setCaption("Фамилия");
        patientGrid.addColumn(Patient::getPatronymic).setCaption("Отчество");
        patientGrid.addColumn(Patient::getNumber).setCaption("Номер");
        patientGrid.setWidth("150%");
        horizontalLayout.addComponents(filteringPatientCSS, addButton, updateButton, deleteButton);
        addComponents(navigateMenuBar, horizontalLayout, patientGrid);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        updateList();
    }

    @Override
    public void updateList() {
        patientGrid.setItems(daoPatient.getAll());
    }

    private void updateListPatient() {
        patientGrid.setItems(daoPatient.getAll(filterPatientText.getValue()));
    }
}

