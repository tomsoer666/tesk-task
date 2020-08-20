package com.haulmont.testtask.components;

import com.haulmont.testtask.entities.Recipe;
import com.vaadin.ui.*;

public class SubWindowRecipeView extends Window {
    private final TextArea descriptionText;
    private final Label patientFIOText;
    private final Label doctorFIOText;
    private final Label patientNumberText;
    private final Label doctorSpecializationText;
    private final Label dateOfCreationText;
    private final Label validityText;
    private final Label priorityText;
    private final VerticalLayout verticalLayout;
    public SubWindowRecipeView(Recipe recipe) {
        super("Просмотр рецепта");
        center();
        setModal(true);
        setResizable(false);
        setClosable(false);
        descriptionText = new TextArea("Описание", recipe.getDescription());
        descriptionText.setWidth("400px");
        descriptionText.setReadOnly(true);
        patientFIOText = new Label("ФИО пациента:\n" + recipe.getPatientFIO());
        doctorFIOText = new Label("ФИО доктора:\n" + recipe.getDoctorFIO());
        patientNumberText = new Label("Номер пациента:\n" + recipe.getPatientNumber());
        doctorSpecializationText = new Label("Специализация доктора:\n" + recipe.getDoctorSpecialization());
        dateOfCreationText = new Label("Дата создания:\n" + recipe.getDateOfCreation());
        validityText = new Label("Срок годности:\n" + recipe.getValidity());
        priorityText = new Label("Приоритет:\n" + recipe.getPriority());
        verticalLayout = new VerticalLayout();
        setContent(verticalLayout);
        verticalLayout.addComponents(descriptionText, patientFIOText, doctorFIOText, patientNumberText,
                doctorSpecializationText, dateOfCreationText, validityText, priorityText,
                new Button("Закрыть", clickEvent -> close()));
    }
}
