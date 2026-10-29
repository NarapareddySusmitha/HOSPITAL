package org.example.hospitalemergencyprioritysystem;

import java.sql.*;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/hospital_db?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";

    // CHANGE THIS to your MySQL password
    private static final String PASSWORD = "priyadarshiniP*24";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {

        String createUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(100) UNIQUE NOT NULL,
                    password VARCHAR(100) NOT NULL,
                    role VARCHAR(30) NOT NULL
                )
                """;

        String createPatients = """
                CREATE TABLE IF NOT EXISTS patients (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(100) UNIQUE NOT NULL,
                    name VARCHAR(150) NOT NULL,
                    age INT NOT NULL,
                    gender VARCHAR(20),
                    phone VARCHAR(30),
                    address VARCHAR(255)
                )
                """;

        String createEmergencyCases = """
                CREATE TABLE IF NOT EXISTS emergency_cases (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    patient_id INT NOT NULL,
                    problem VARCHAR(255) NOT NULL,
                    priority VARCHAR(30) NOT NULL,
                    status VARCHAR(30) NOT NULL DEFAULT 'WAITING',
                    bed_no INT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (patient_id) REFERENCES patients(id)
                )
                """;

        String createBeds = """
                CREATE TABLE IF NOT EXISTS beds (
                    bed_no INT PRIMARY KEY,
                    ward VARCHAR(100) NOT NULL,
                    status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE'
                )
                """;

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate(createUsers);
            st.executeUpdate(createPatients);
            st.executeUpdate(createEmergencyCases);
            st.executeUpdate(createBeds);

            insertDefaultUsers(con);
            insertDefaultBeds(con);

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertDefaultUsers(Connection con) throws SQLException {

        String sql = """
                INSERT IGNORE INTO users(username,password,role)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "nurse");
            ps.setString(2, "nurse123");
            ps.setString(3, "NURSE");
            ps.executeUpdate();

            ps.setString(1, "admin");
            ps.setString(2, "admin123");
            ps.setString(3, "ADMIN");
            ps.executeUpdate();
        }
    }

    private static void insertDefaultBeds(Connection con) throws SQLException {

        String check = "SELECT COUNT(*) FROM beds";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(check)) {

            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
        }

        String sql =
                "INSERT INTO beds(bed_no,ward,status) VALUES (?, ?, 'AVAILABLE')";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 1; i <= 20; i++) {

                ps.setInt(1, i);

                if (i <= 10) {
                    ps.setString(2, "Emergency Ward");
                } else {
                    ps.setString(2, "General Ward");
                }

                ps.executeUpdate();
            }
        }
    }
}