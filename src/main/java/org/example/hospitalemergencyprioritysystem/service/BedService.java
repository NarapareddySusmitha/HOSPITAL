package org.example.hospitalemergencyprioritysystem.service;

import org.example.hospitalemergencyprioritysystem.DatabaseConnection;
import org.example.hospitalemergencyprioritysystem.exception.BedNotAvailableException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BedService {

    // Synchronization prevents two operations
    // from assigning the same bed at the same time.
    public synchronized void assignBed(
            String bedNumber,
            String patientName)
            throws SQLException, BedNotAvailableException {

        String checkSql =
                "SELECT status FROM beds WHERE bed_number=? FOR UPDATE";

        String updateSql =
                "UPDATE beds SET status='OCCUPIED', patient_name=? " +
                        "WHERE bed_number=?";

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                try (PreparedStatement statement =
                             connection.prepareStatement(checkSql)) {

                    statement.setString(1, bedNumber);

                    try (ResultSet result =
                                 statement.executeQuery()) {

                        if (!result.next()) {
                            throw new BedNotAvailableException(
                                    "Bed does not exist.");
                        }

                        String status =
                                result.getString("status");

                        if (!"AVAILABLE".equalsIgnoreCase(status)) {
                            throw new BedNotAvailableException(
                                    "Bed " + bedNumber +
                                            " is not available.");
                        }
                    }
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(updateSql)) {

                    statement.setString(1, patientName);
                    statement.setString(2, bedNumber);

                    statement.executeUpdate();
                }

                connection.commit();

            } catch (BedNotAvailableException e) {

                connection.rollback();
                throw e;

            } catch (SQLException e) {

                connection.rollback();
                throw e;
            }
        }
    }

    public synchronized void releaseBed(String bedNumber)
            throws SQLException {

        String sql =
                "UPDATE beds SET status='AVAILABLE', " +
                        "patient_name=NULL WHERE bed_number=?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, bedNumber);
            statement.executeUpdate();
        }
    }
}