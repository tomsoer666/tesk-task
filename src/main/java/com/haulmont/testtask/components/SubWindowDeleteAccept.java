package com.haulmont.testtask.components;

import com.haulmont.testtask.daos.DAODoctor;
import com.haulmont.testtask.daos.interfaces.DAO;
import com.haulmont.testtask.entities.interfaces.DataBaseEntity;
import com.haulmont.testtask.views.interfaces.PageView;
import com.vaadin.ui.*;

import java.sql.SQLException;

public class SubWindowDeleteAccept extends Window {
    private final Label message = new Label("Вы действительно хотите удалить?");
    private final Button acceptButton = new Button("Да");
    private final Button declineButton = new Button("Нет");
    private final VerticalLayout verticalLayout = new VerticalLayout(message);
    private final HorizontalLayout horizontalLayout = new HorizontalLayout(acceptButton, declineButton);
    private final PageView pageView;

    public SubWindowDeleteAccept(PageView pageView, DataBaseEntity dataBaseEntity, DAO dao) {
        super("Подтверждение удаления");
        center();
        setModal(true);
        this.pageView = pageView;
        setContent(verticalLayout);
        horizontalLayout.addComponents(acceptButton, declineButton);
        verticalLayout.addComponents(message, horizontalLayout);
        acceptButton.addClickListener(e -> {
            try {
                this.confirmDelete(dataBaseEntity, dao);
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
        declineButton.addClickListener(e -> close());
        setClosable(false);
        setResizable(false);
    }

    protected void confirmDelete(DataBaseEntity dataBaseEntity, DAO dao) throws SQLException {
        try {
            dao.delete(dataBaseEntity);
            Notification.show("Удаление успешно завершено!");
            pageView.updateList();
            close();
        } catch (NullPointerException e) {
            String people;
            if (dao instanceof DAODoctor) {
                people = "доктора";
            } else {
                people = "пациента";
            }
            Notification.show("Невозможно удалить " + people + "! Существует актуальный рецепт!",
                    Notification.Type.ERROR_MESSAGE);
            pageView.updateList();
            close();
        }

    }

}
