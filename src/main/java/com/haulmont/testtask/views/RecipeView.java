package com.haulmont.testtask.views;

import com.haulmont.testtask.MainUI;
import com.haulmont.testtask.components.NavigationMenuBar;
import com.haulmont.testtask.components.SubWindowDeleteAccept;
import com.haulmont.testtask.components.SubWindowRecipe;
import com.haulmont.testtask.components.SubWindowRecipeView;
import com.haulmont.testtask.daos.DAORecipe;
import com.haulmont.testtask.entities.Recipe;
import com.haulmont.testtask.enums.Priority;
import com.haulmont.testtask.views.interfaces.PageView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.vaadin.ui.Notification.Type.ERROR_MESSAGE;

public class RecipeView extends VerticalLayout implements View, PageView {
    private final TextField filterSurnameText;
    private final TextField filterNameText;
    private final TextField filterPatronymicText;
    private final TextField filterNumberText;
    private final TextField filterDescriptionText;
    private final ComboBox<String> filterPriorityText;
    private final Button acceptFilterButton;
    private final Button clearFilterButton;
    private final NavigationMenuBar navigateMenuBar;
    private final HorizontalLayout horizontalLayout;
    private final FormLayout formLayout;
    private final Panel filterPanel;
    private final Button addButton;
    private final Button updateButton;
    private final Button deleteButton;
    private final Button viewButton;
    private final DAORecipe daoRecipe;
    private final Grid<Recipe> recipeGrid;
    private Recipe recipe;

    public RecipeView(MainUI myUI) {
        navigateMenuBar = new NavigationMenuBar(myUI);
        recipeGrid = new Grid<>();
        daoRecipe = new DAORecipe();
        horizontalLayout = new HorizontalLayout();
        filterNameText = new TextField("Имя");
        filterSurnameText = new TextField("Фамилия");
        filterPatronymicText = new TextField("Отчество");
        filterDescriptionText = new TextField("Описание");
        filterNumberText = new TextField("Номер");
        filterPriorityText = new ComboBox("Приоритет");
        clearFilterButton = new Button("Сбросить", clickEvent -> {
            filterNameText.clear();
            filterSurnameText.clear();
            filterPatronymicText.clear();
            filterNumberText.clear();
            filterPriorityText.clear();
            filterDescriptionText.clear();
            updateList();
        });
        viewButton = new Button("Просмотреть рецепт", clickEvent -> {
            if (recipe == null) {
                Notification.show("Выберете строку для просмотра!", ERROR_MESSAGE);
            } else {
                SubWindowRecipeView subWindow = new SubWindowRecipeView(recipe);
                UI.getCurrent().addWindow(subWindow);
            }
        });
        acceptFilterButton = new Button("Применить", clickEvent -> {
            if (filterPriorityText.getValue() == null) {
                recipeGrid.setItems(daoRecipe.getAll(filterNameText.getValue(), filterSurnameText.getValue(),
                        filterPatronymicText.getValue(), filterNumberText.getValue(),
                        "", filterDescriptionText.getValue()));
            } else {
                recipeGrid.setItems(daoRecipe.getAll(filterNameText.getValue(), filterSurnameText.getValue(),
                        filterPatronymicText.getValue(), filterNumberText.getValue(),
                        filterPriorityText.getValue(), filterDescriptionText.getValue()));
            }
        });
        filterPanel = new Panel("Фильтры");
        formLayout = new FormLayout();
        formLayout.setSizeUndefined();
        formLayout.setMargin(true);
        formLayout.addComponents(filterNameText, filterSurnameText, filterPatronymicText, filterNumberText,
                filterPriorityText, filterDescriptionText, new HorizontalLayout(acceptFilterButton, clearFilterButton));
        filterPanel.setContent(formLayout);
        filterPanel.setSizeUndefined();
        filterSurnameText.setPlaceholder("Поиск по фамилии");
        filterNameText.setPlaceholder("Поиск по имени");
        filterPatronymicText.setPlaceholder("Поиск по отчеству");
        filterNumberText.setPlaceholder("Поиск по номеру");
        filterDescriptionText.setPlaceholder("Поиск по описанию");
        filterPriorityText.setPlaceholder("Поиск по приоритету");
        filterPriorityText.setItems(Stream.of(Priority.values()).map(Enum::toString).collect(Collectors.toList()));
        setWidthUndefined();

        addButton = new Button("Добавить",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        SubWindowRecipe subWindow = new SubWindowRecipe(RecipeView.this);
                        UI.getCurrent().addWindow(subWindow);
                    }
                });

        updateButton = new Button("Редактировать",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        try {
                            SubWindowRecipe subWindow = new SubWindowRecipe(RecipeView.this, recipe);
                            UI.getCurrent().addWindow(subWindow);
                        } catch (NullPointerException e) {
                            Notification.show("Выберете строку для редактирования!", ERROR_MESSAGE);
                        }
                    }
                });

        deleteButton = new Button("Удалить",
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        if (recipe == null) {
                            Notification.show("Выберете строку для удаления!", ERROR_MESSAGE);
                        } else {
                            SubWindowDeleteAccept subWindow = new SubWindowDeleteAccept(RecipeView.this,
                                    recipe, daoRecipe);
                            UI.getCurrent().addWindow(subWindow);
                        }
                    }
                });

        recipeGrid.asSingleSelect().addValueChangeListener(e -> recipe = e.getValue());
        recipeGrid.addColumn(Recipe::getId).setCaption("id").setHidden(true);
        recipeGrid.addColumn(Recipe::getPatientId).setCaption("Patient id").setHidden(true);
        recipeGrid.addColumn(Recipe::getDoctorId).setCaption("Doctor id").setHidden(true);
        recipeGrid.addColumn(Recipe::getDescription).setCaption("Описание").setWidth(250);
        recipeGrid.addColumn(Recipe::getPatientFIO).setCaption("ФИО пациента");
        recipeGrid.addColumn(Recipe::getPatientNumber).setCaption("Номер");
        recipeGrid.addColumn(Recipe::getDoctorFIO).setCaption("ФИО доктора");
        recipeGrid.addColumn(Recipe::getDoctorSpecialization).setCaption("Специализация доктора");
        recipeGrid.addColumn(Recipe::getPriority).setCaption("Приоритет");
        recipeGrid.addColumn(Recipe::getDateOfCreation).setCaption("Дата создания");
        recipeGrid.addColumn(Recipe::getValidity).setCaption("Срок годности");
        recipeGrid.setWidth("800px");

        horizontalLayout.addComponents(addButton, updateButton, deleteButton, viewButton);
        addComponents(navigateMenuBar, horizontalLayout, new HorizontalLayout(filterPanel, recipeGrid));
        setWidthUndefined();
    }

    @Override
    public void updateList() {
        recipeGrid.setItems(daoRecipe.getAll());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        updateList();
    }
}
