package com.haulmont.testtask.components;

import com.haulmont.testtask.MainUI;
import com.vaadin.ui.MenuBar;

public class NavigationMenuBar extends MenuBar {
    public NavigationMenuBar(MainUI mainUI) {
        super();
        addItem("Доктор", null, menuItem -> {
            mainUI.getNavigator().navigateTo("doctor");
        });
        addItem("Пациент", null, menuItem -> {
            mainUI.getNavigator().navigateTo("patient");
        });
        addItem("Рецепт", null, menuItem -> {
            mainUI.getNavigator().navigateTo("recipe");
        });
    }
}
