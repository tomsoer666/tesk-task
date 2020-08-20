package com.haulmont.testtask.components;

import com.haulmont.testtask.daos.DAODoctor;
import com.haulmont.testtask.daos.DAOPatient;
import com.haulmont.testtask.daos.DAORecipe;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class SubWindowStatistic extends Window {
    private final Label countDoctor;
    private final Label countPatient;
    private final Label countRecipe;
    private final Label countSickedPeople;
    private final Button closeButton;
    private final DAODoctor daoDoctor;
    private final DAOPatient daoPatient;
    private final DAORecipe daoRecipe;
    private final VerticalLayout verticalLayout;
    public SubWindowStatistic() {
        super("Статистика");
        center();
        setResizable(false);
        setModal(true);
        setClosable(false);
        daoDoctor = new DAODoctor();
        daoPatient = new DAOPatient();
        daoRecipe = new DAORecipe();
        countDoctor = new Label("Количество врачей: " + daoDoctor.getStatisticCount());
        countPatient = new Label("Количество пациентов: " + daoPatient.getStatisticCount());
        countRecipe = new Label("Количество рецептов: " + daoRecipe.getStatisticCount());
        countSickedPeople = new Label("На больничном: " + daoPatient.getStatisticSick());
        closeButton = new Button("Ок", clickEvent -> close());
        verticalLayout = new VerticalLayout(countDoctor, countPatient, countRecipe, countSickedPeople, closeButton);
        setContent(verticalLayout);
    }
}
