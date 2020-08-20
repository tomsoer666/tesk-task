package com.haulmont.testtask.daos;

import com.haulmont.testtask.daos.interfaces.DAO;
import com.haulmont.testtask.entities.Recipe;
import com.haulmont.testtask.utils.JDBCconnection;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DAORecipe implements DAO<Recipe> {

    @Override
    public Optional getById(long id) {
        try (Connection connection = JDBCconnection.getConnection()) {
            PreparedStatement preStatement = connection.prepareStatement("SELECT r.id, r.description, " +
                    "r.patientID, r.doctorId, r.dateOfCreation, r.validity, r.priority, \n" +
                    "d.doctorName, d. doctorSurname, d.doctorPatronymic, d.specialization, \n" +
                    "p.patientName, p.patientSurname, p.patientPatronymic, p.number\n" +
                    " FROM  recipes as r\n" +
                    "JOIN doctors as d on r.doctorId = d.doctorId \n" +
                    "JOIN patients as p on r.patientId = p.patientId WHERE r.id = ?");
            preStatement.setLong(1, id);
            ResultSet resultSet = preStatement.executeQuery();
            Recipe recipe = null;
            while (resultSet.next()) {
                recipe = parseRecipe(resultSet);
            }
            return Optional.ofNullable(recipe);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    @Override
    public Set<Recipe> getAll() {
        try (Connection connection = JDBCconnection.getConnection()) {
            Set<Recipe> recipes = new HashSet<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT r.id, r.description, " +
                    "r.patientID, r.doctorId, r.dateOfCreation, r.validity, r.priority, \n" +
                    "d.doctorName, d. doctorSurname, d.doctorPatronymic, d.specialization, \n" +
                    "p.patientName, p.patientSurname, p.patientPatronymic, p.number\n" +
                    " FROM  recipes as r\n" +
                    "JOIN doctors as d on r.doctorId = d.doctorId \n" +
                    "JOIN patients as p on r.patientId = p.patientId");
            while (resultSet.next()) {
                Recipe recipe = parseRecipe(resultSet);
                recipes.add(recipe);
            }
            return recipes;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    public Set<Recipe> getAll(String name, String surname, String patronymic, String number, String priority,
                              String description) {
        try (Connection connection = JDBCconnection.getConnection()) {
            Set<Recipe> recipes = new HashSet<>();
            if (number == null) {
                PreparedStatement preStatement = connection.prepareStatement("SELECT r.id, r.description, " +
                        "r.patientID, r.doctorId, r.dateOfCreation, r.validity, r.priority, " +
                        "d.doctorName, d. doctorSurname, d.doctorPatronymic, d.specialization, " +
                        "p.patientName, p.patientSurname, p.patientPatronymic, p.number " +
                        "FROM recipes as r " +
                        "JOIN doctors as d on r.doctorId = d.doctorId " +
                        "JOIN patients as p on r.patientId = p.patientId WHERE (LOWER(p.PATIENTNAME) LIKE " +
                        "LOWER('%' + ? + '%') AND LOWER(p.PATIENTSURNAME) LIKE LOWER('%' + ? + '%') " +
                        "AND LOWER(p.PATIENTPATRONYMIC) LIKE LOWER('%' + ? + '%') " +
                        "AND LOWER(r.PRIORITY) LIKE LOWER('%' + ? + '%') " +
                        "AND LOWER(r.DESCRIPTION) LIKE LOWER('%' + ? + '%'))");
                preStatement.setString(1, name);
                preStatement.setString(2, surname);
                preStatement.setString(3, patronymic);
                preStatement.setString(4, priority);
                preStatement.setString(5, description);
                ResultSet resultSet = preStatement.executeQuery();
                while (resultSet.next()) {
                    Recipe recipe = parseRecipe(resultSet);
                    recipes.add(recipe);
                }
            } else {
                PreparedStatement preStatement = connection.prepareStatement("SELECT r.id, r.description, " +
                        "r.patientID, r.doctorId, r.dateOfCreation, r.validity, r.priority, " +
                        "d.doctorName, d. doctorSurname, d.doctorPatronymic, d.specialization, " +
                        "p.patientName, p.patientSurname, p.patientPatronymic, p.number " +
                        "FROM recipes as r " +
                        "JOIN doctors as d on r.doctorId = d.doctorId " +
                        "JOIN patients as p on r.patientId = p.patientId WHERE (LOWER(p.PATIENTNAME) LIKE " +
                        "LOWER('%' + ? + '%') AND LOWER(p.PATIENTSURNAME) LIKE LOWER('%' + ? + '%') " +
                        "AND LOWER(p.PATIENTPATRONYMIC) LIKE LOWER('%' + ? + '%') AND LOWER(p.NUMBER) " +
                        "LIKE LOWER('%' + ? + '%') AND LOWER(r.PRIORITY) LIKE LOWER('%' + ? + '%') " +
                        "AND LOWER(r.DESCRIPTION) LIKE LOWER('%' + ? + '%'))");
                preStatement.setString(1, name);
                preStatement.setString(2, surname);
                preStatement.setString(3, patronymic);
                preStatement.setString(4, number);
                preStatement.setString(5, priority);
                preStatement.setString(6, description);
                ResultSet resultSet = preStatement.executeQuery();
                while (resultSet.next()) {
                    Recipe recipe = parseRecipe(resultSet);
                    recipes.add(recipe);
                }
            }
            return recipes;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }



    public int getStatisticCount() {
        try (Connection connection = JDBCconnection.getConnection()) {
            int count = 0;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM RECIPES");
            while (resultSet.next()) {
                count = resultSet.getInt("C1");
            }
            return count;
        } catch (SQLException e) {
            throw new NullPointerException();
        }
    }
    @Override
    public void save(Recipe recipe) {
        try (Connection connection = JDBCconnection.getConnection()) {
            PreparedStatement preStatement =
                    connection.prepareStatement("INSERT INTO RECIPES (description, " +
                    "PATIENTID, DOCTORID, DATEOFCREATION, VALIDITY, PRIORITY)"
                    + " VALUES (?, ?, ?, ?, ?, ?)");
            preStatement.setString(1, recipe.getDescription());
            preStatement.setLong(2, recipe.getPatientId());
            preStatement.setLong(3, recipe.getDoctorId());
            preStatement.setDate(4, recipe.getDateOfCreation());
            preStatement.setString(5, recipe.getValidity());
            preStatement.setString(6, recipe.getPriority());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    @Override
    public void update(Recipe recipe) {
        try (Connection connection = JDBCconnection.getConnection()) {
            PreparedStatement preStatement =
                    connection.prepareStatement("UPDATE RECIPES SET description = ?, " +
                    "PATIENTID = ?, DOCTORID = ?, DATEOFCREATION = ?, VALIDITY = ?, " +
                    "PRIORITY = ? WHERE id = ?");
            preStatement.setString(1, recipe.getDescription());
            preStatement.setLong(2, recipe.getPatientId());
            preStatement.setLong(3, recipe.getDoctorId());
            preStatement.setDate(4, recipe.getDateOfCreation());
            preStatement.setString(5, recipe.getValidity());
            preStatement.setString(6, recipe.getPriority());
            preStatement.setLong(7, recipe.getId());
            preStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    @Override
    public void delete(Recipe recipe) throws SQLException {
        try (Connection connection = JDBCconnection.getConnection()) {
            PreparedStatement preStatement =
                    connection.prepareStatement("DELETE FROM RECIPES WHERE id = ?");
            preStatement.setLong(1, recipe.getId());
            preStatement.executeUpdate();
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    private Recipe parseRecipe(ResultSet resultSet) throws SQLException {
        return new Recipe(
                resultSet.getLong("id"),
                resultSet.getLong("patientID"),
                resultSet.getLong("doctorId"),
                resultSet.getString("description"),
                resultSet.getString("patientSurname") + " " + resultSet.getString("patientName")
                        + " " + resultSet.getString("patientPatronymic"),
                resultSet.getString("number"),
                resultSet.getString("doctorSurname") + " " + resultSet.getString("doctorName") +
                        " " + resultSet.getString("doctorPatronymic"),
                resultSet.getString("specialization"),
                resultSet.getDate("dateOfCreation"), resultSet.getString("validity"),
                resultSet.getString("priority"));
    }
}
