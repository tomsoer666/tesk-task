package com.haulmont.testtask.utils;

import java.sql.*;
import java.util.ResourceBundle;

public class JDBCconnection {
    public static Connection getConnection() throws SQLException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Connection");
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер hsql не найден");
            e.printStackTrace();
        }
        return DriverManager.getConnection(resourceBundle.getString("db.url")+
                        resourceBundle.getString("db.databasename"), resourceBundle.getString("db.user"),
                resourceBundle.getString("db.password")
        );

    }
}
