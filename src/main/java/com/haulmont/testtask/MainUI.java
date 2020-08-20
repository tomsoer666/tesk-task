package com.haulmont.testtask;

import com.haulmont.testtask.components.NavigationMenuBar;
import com.haulmont.testtask.views.MainView;
import com.haulmont.testtask.views.PatientView;
import com.haulmont.testtask.views.DoctorView;
import com.haulmont.testtask.views.RecipeView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {
    private Navigator navigator;
    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Haulmont Task");
        CssLayout viewDisplay = new CssLayout();
        VerticalLayout content = new VerticalLayout(viewDisplay);
        setContent(content);
        navigator = new Navigator(this, viewDisplay);
        navigator.addView("", new MainView(this));
        navigator.addView("doctor", new DoctorView(this));
        navigator.addView("recipe", new RecipeView(this));
        navigator.addView("patient", new PatientView(this));
    }
}