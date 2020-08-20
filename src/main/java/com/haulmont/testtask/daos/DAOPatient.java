package com.haulmont.testtask.daos;

import com.haulmont.testtask.daos.interfaces.DAO;
import com.haulmont.testtask.entities.Patient;
import com.haulmont.testtask.utils.JDBCconnection;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DAOPatient implements DAO<Patient> {

    @Override
    public Optional<Patient> getById(long id) {
        try (Connection connection = JDBCconnection.getConnection()) {
            PreparedStatement preStatement =
                    connection.prepareStatement("SELECT * FROM patients WHERE PATIENTID = ?");
            preStatement.setLong(1, id);
            ResultSet resultSet = preStatement.executeQuery();
            Patient patient = null;
            while (resultSet.next()) {
                patient = parsePatient(resultSet);
            }
            return Optional.ofNullable(patient);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    @Override
    public Set<Patient> getAll() {
        try (Connection connection = JDBCconnection.getConnection()) {
            Set<Patient> patients = new HashSet<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM patients");
            while (resultSet.next()) {
                Patient patient = parsePatient(resultSet);
                patients.add(patient);
            }
            return patients;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    @Override
    public int getStatisticCount() {
        try (Connection connection = JDBCconnection.getConnection()) {
            int count = 0;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM PATIENTS");
            while (resultSet.next()) {
                count = resultSet.getInt("C1");
            }
            return count;
        } catch (SQLException e) {
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    public int getStatisticSick() {
        try (Connection connection = JDBCconnection.getConnection()) {
            int count = 0;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                    "SELECT COUNT(DISTINCT PATIENTID) FROM RECIPES");

            while (resultSet.next()) {
                count = resultSet.getInt("C1");
            }
            return count;
        } catch (SQLException e) {
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    public Set<Patient> getAll(String filter) {
        try (Connection connection = JDBCconnection.getConnection()) {
            Set<Patient> patients = new HashSet<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM patients WHERE LOWER(PATIENTSURNAME) " +
                    "LIKE LOWER(\'%" + filter + "%')");
            while (resultSet.next()) {
                Patient patient = parsePatient(resultSet);
                patients.add(patient);
            }
            return patients;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    @Override
    public void save(Patient patient) {
        try (Connection connection = JDBCconnection.getConnection()) {
            PreparedStatement preStatement =
                    connection.prepareStatement("INSERT INTO patients (patientName, " +
                    "PATIENTSURNAME, PATIENTPATRONYMIC, number)"
                    + " VALUES (?, ?, ?, ?)");
            preStatement.setString(1, patient.getName());
            preStatement.setString(2, patient.getSurname());
            preStatement.setString(3, patient.getPatronymic());
            preStatement.setString(4, patient.getNumber());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    @Override
    public void update(Patient patient) {
        try (Connection connection = JDBCconnection.getConnection()) {
            PreparedStatement preStatement =
                    connection.prepareStatement("UPDATE patients SET patientName = ?, " +
                    "patientSurname = ?, PATIENTPATRONYMIC = ?, number = ? WHERE PATIENTID = ?");
            preStatement.setString(1, patient.getName());
            preStatement.setString(2, patient.getSurname());
            preStatement.setString(3, patient.getPatronymic());
            preStatement.setString(4, patient.getNumber());
            preStatement.setLong(5, patient.getId());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    @Override
    public void delete(Patient patient) {
        try (Connection connection = JDBCconnection.getConnection()) {
            PreparedStatement preStatement =
                    connection.prepareStatement("DELETE FROM patients WHERE PATIENTID = ?");
            preStatement.setLong(1, patient.getId());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    private Patient parsePatient(ResultSet resultSet) throws SQLException {
        return new Patient(
                resultSet.getLong("patientId"),
                resultSet.getString("patientName"),
                resultSet.getString("patientSurname"),
                resultSet.getString("patientPatronymic"),
                resultSet.getString("number"));
    }
}
