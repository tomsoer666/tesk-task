package com.haulmont.testtask.daos;

import com.haulmont.testtask.daos.interfaces.DAO;
import com.haulmont.testtask.entities.Doctor;
import com.haulmont.testtask.utils.JDBCconnection;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DAODoctor implements DAO<Doctor> {

    @Override
    public Optional<Doctor> getById(long id) throws SQLException {
        try (Connection connection = JDBCconnection.getConnection()) {
            PreparedStatement preStatement = connection.prepareStatement("SELECT * FROM doctors WHERE doctorId = ?");
            preStatement.setLong(1, id);
            ResultSet resultSet = preStatement.executeQuery();
            Doctor doctor = null;
            while (resultSet.next()) {
                doctor = parseDoctor(resultSet);
            }
            return Optional.ofNullable(doctor);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    @Override
    public Set<Doctor> getAll() {
        try (Connection connection = JDBCconnection.getConnection()) {
            Set<Doctor> doctors = new HashSet<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM doctors");
            while (resultSet.next()) {
                Doctor doctor = parseDoctor(resultSet);
                doctors.add(doctor);
            }
            return doctors;
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
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM DOCTORS");
            while (resultSet.next()) {
                count = resultSet.getInt("C1");
            }
            return count;
        } catch (SQLException e) {
            throw new NullPointerException();
        }
    }

    public Set<Doctor> getAll(String filter) {
        try (Connection connection = JDBCconnection.getConnection()) {
            Set<Doctor> doctors = new HashSet<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM doctors WHERE LOWER(doctorSurname)" +
                    " LIKE LOWER(\'%" + filter + "%')");
            while (resultSet.next()) {
                Doctor doctor = parseDoctor(resultSet);
                doctors.add(doctor);
            }
            return doctors;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    private Doctor parseDoctor(ResultSet resultSet) throws SQLException {
        return new Doctor(
                resultSet.getLong("doctorId"),
                resultSet.getString("doctorName"),
                resultSet.getString("doctorSurname"),
                resultSet.getString("doctorPatronymic"),
                resultSet.getString("specialization"));
    }

    @Override
    public void save(Doctor doctor) {
        try (Connection connection = JDBCconnection.getConnection()) {
            PreparedStatement preStatement = connection.prepareStatement("INSERT INTO DOCTORS (DOCTORNAME, " +
                    "DOCTORSURNAME, DOCTORPATRONYMIC, SPECIALIZATION)"
                    + " VALUES (?, ?, ?, ?)");
            preStatement.setString(1, doctor.getName());
            preStatement.setString(2, doctor.getSurname());
            preStatement.setString(3, doctor.getPatronymic());
            preStatement.setString(4, doctor.getSpecialization());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    @Override
    public void update(Doctor doctor) {
        try (Connection connection = JDBCconnection.getConnection()) {
            PreparedStatement preStatement = connection.prepareStatement("UPDATE doctors SET doctorName = ?, " +
                    "doctorSurname = ?, doctorPatronymic = ?, specialization = ? WHERE doctorId = ?");
            preStatement.setString(1, doctor.getName());
            preStatement.setString(2, doctor.getSurname());
            preStatement.setString(3, doctor.getPatronymic());
            preStatement.setString(4, doctor.getSpecialization());
            preStatement.setLong(5, doctor.getId());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }

    @Override
    public void delete(Doctor doctor) {
        try (Connection connection = JDBCconnection.getConnection()) {
            PreparedStatement preStatement = connection.prepareStatement("DELETE FROM doctors " +
                    "WHERE doctorId = ?");
            preStatement.setLong(1, doctor.getId());
            preStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException("Произошла неизвестная ошибка");
        }
    }
}
