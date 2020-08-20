package com.haulmont.testtask.views;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.components.NavigationMenuBar;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MainView extends VerticalLayout implements View {
    public MainView(MainUI mainUI) {
        NavigationMenuBar navigationMenuBar = new NavigationMenuBar(mainUI);
        addComponent(navigationMenuBar);
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Notification notification = new Notification("");
        notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS + " " + ValoTheme.NOTIFICATION_CLOSABLE);
        Notification.show("Добро пожаловать!\n Выберете страницу для начала работы!");
    }
}
