package com.haulmont.testtask.views;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.components.NavigationMenuBar;
import com.haulmont.testtask.components.SubWindowDeleteAccept;
import com.haulmont.testtask.components.SubWindowDoctor;
import com.haulmont.testtask.components.SubWindowStatistic;
import com.haulmont.testtask.daos.DAODoctor;
import com.haulmont.testtask.daos.DAOPatient;
import com.haulmont.testtask.daos.DAORecipe;
import com.haulmont.testtask.entities.Doctor;
import com.haulmont.testtask.views.interfaces.PageView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;


public class DoctorView extends VerticalLayout implements View, PageView {
    private final TextField filterDoctorText;
    private final Button clearFilterDoctorButton;
    private final Grid<Doctor> doctorGrid;
    private final DAODoctor daoDoctor;
    private final NavigationMenuBar navigateMenuBar;
    private final HorizontalLayout horizontalLayout;
    private final CssLayout filteringDoctorCSS;
    private final Button addButton;
    private final Button updateButton;
    private final Button deleteButton;
    private final Button statisticButton;
    private Doctor doctor;

    public DoctorView(MainUI myUI) {
        navigateMenuBar = new NavigationMenuBar(myUI);
        daoDoctor = new DAODoctor();
        horizontalLayout = new HorizontalLayout();
        filterDoctorText = new TextField();
        clearFilterDoctorButton = new Button(VaadinIcons.CLOSE);
        filterDoctorText.setPlaceholder("Поиск по фамилии");
        filterDoctorText.addValueChangeListener(e -> updateListDoctor());
        filterDoctorText.setValueChangeMode(ValueChangeMode.LAZY);

        clearFilterDoctorButton.addClickListener(e -> filterDoctorText.clear());

        filteringDoctorCSS = new CssLayout();
        filteringDoctorCSS.addComponents(filterDoctorText, clearFilterDoctorButton);
        filteringDoctorCSS.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        addButton = new Button("Добавить",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        SubWindowDoctor subWindowDoctor = new SubWindowDoctor(DoctorView.this);
                        UI.getCurrent().addWindow(subWindowDoctor);
                    }
                });

        statisticButton = new Button("Показать статистику", clickEvent -> {
            SubWindowStatistic statisticWindow = new SubWindowStatistic();
            UI.getCurrent().addWindow(statisticWindow);
        });

        updateButton = new Button("Редактировать",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        try {
                            SubWindowDoctor subWindowDoctor = new SubWindowDoctor(DoctorView.this, doctor);
                            UI.getCurrent().addWindow(subWindowDoctor);
                        } catch (java.lang.NullPointerException exception) {
                            Notification.show("Выберете строку для редактирования!", ERROR_MESSAGE);
                        }
                    }
                });

        deleteButton = new Button("Удалить",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        if (doctor == null) {
                            Notification.show("Выберете строку для удаления!", ERROR_MESSAGE);
                        } else {
                                SubWindowDeleteAccept subWindowDoctor = new SubWindowDeleteAccept(DoctorView.this,
                                        doctor, daoDoctor);
                                UI.getCurrent().addWindow(subWindowDoctor);
                        }
                    }
                });

        addButton.setWidth("150px");
        updateButton.setWidth("150px");
        deleteButton.setWidth("150px");
        doctorGrid = new Grid<>();
        doctorGrid.asSingleSelect().addValueChangeListener(e -> doctor = e.getValue());
        doctorGrid.addColumn(Doctor::getId).setCaption("id").setHidden(true);
        doctorGrid.addColumn(Doctor::getName).setCaption("Имя");
        doctorGrid.addColumn(Doctor::getSurname).setCaption("Фамилия");
        doctorGrid.addColumn(Doctor::getPatronymic).setCaption("Отчество");
        doctorGrid.addColumn(Doctor::getSpecialization).setCaption("Специализация");
        doctorGrid.setWidth("125%");
        horizontalLayout.addComponents(filteringDoctorCSS, addButton, updateButton, deleteButton, statisticButton);
        addComponents(navigateMenuBar, horizontalLayout, doctorGrid);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        updateList();
    }

    @Override
    public void updateList() {
        doctorGrid.setItems(daoDoctor.getAll());
    }

    private void updateListDoctor() {
        doctorGrid.setItems(daoDoctor.getAll(filterDoctorText.getValue()));
    }
}
