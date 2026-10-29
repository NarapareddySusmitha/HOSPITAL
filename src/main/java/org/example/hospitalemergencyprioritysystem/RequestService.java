package org.example.hospitalemergencyprioritysystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestService {

    public record RequestItem(int id, int patientId, String patientName,
                              int bedNo, String message, String time) {}

    public static void addRequest(int patientId, String name, int bedNo, String message) throws SQLException {
        String sql = "INSERT INTO patient_requests (patient_id, patient_name, bed_no, message) VALUES (?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setString(2, name);
            ps.setInt(3, bedNo);
            ps.setString(4, message);
            ps.executeUpdate();
        }
    }

    public static List<RequestItem> getPending() throws SQLException {
        List<RequestItem> list = new ArrayList<>();
        String sql = "SELECT * FROM patient_requests WHERE status='PENDING' ORDER BY created_at DESC";
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new RequestItem(rs.getInt("id"), rs.getInt("patient_id"),
                        rs.getString("patient_name"), rs.getInt("bed_no"),
                        rs.getString("message"), rs.getTimestamp("created_at").toString()));
            }
        }
        return list;
    }

    public static void markDone(int id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE patient_requests SET status='DONE' WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}