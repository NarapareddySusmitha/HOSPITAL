package org.example.hospitalemergencyprioritysystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Hospital Emergency Priority System
 * Simple Swing based frontend.
 */
public class Main{

    // ---------- COLORS ----------
    private static final Color DARK_BLUE = new Color(20, 55, 95);
    private static final Color BLUE = new Color(35, 105, 170);
    private static final Color LIGHT_BLUE = new Color(235, 245, 255);
    private static final Color GREEN = new Color(40, 150, 90);
    private static final Color RED = new Color(210, 55, 55);
    private static final Color ORANGE = new Color(230, 140, 40);
    private static final Color GRAY = new Color(245, 247, 250);

    // ---------- SAMPLE DATA ----------
    private static final List<Patient> patients = new ArrayList<>();
    private static final List<EmergencyCase> emergencyCases = new ArrayList<>();

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            loadSampleData();

            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
            }

            showLoginScreen();
        });
    }

    // =========================================================
    // SAMPLE DATA
    // =========================================================

    private static void loadSampleData() {

        patients.clear();
        emergencyCases.clear();

        patients.add(new Patient(
                101,
                "Rahul",
                "9876543210",
                "Fever"
        ));

        patients.add(new Patient(
                102,
                "Anjali",
                "9876501234",
                "Chest Pain"
        ));

        patients.add(new Patient(
                103,
                "Kiran",
                "9123456780",
                "Accident"
        ));

        emergencyCases.add(new EmergencyCase(
                101,
                "Rahul",
                "HIGH",
                "Waiting"
        ));

        emergencyCases.add(new EmergencyCase(
                102,
                "Anjali",
                "CRITICAL",
                "Waiting"
        ));

        emergencyCases.add(new EmergencyCase(
                103,
                "Kiran",
                "MEDIUM",
                "Waiting"
        ));
    }

    // =========================================================
    // LOGIN SCREEN
    // =========================================================

    private static void showLoginScreen() {

        JFrame frame = new JFrame("Hospital Emergency Priority System");

        frame.setSize(850, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(LIGHT_BLUE);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(450, 470));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(30, 40, 30, 40));

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("HOSPITAL");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(DARK_BLUE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Emergency Priority System");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setForeground(BLUE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(330, 40));

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(330, 40));

        JLabel roleLabel = new JLabel("Login As");
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JComboBox<String> roleBox = new JComboBox<>(
                new String[]{"Patient", "Nurse"}
        );

        roleBox.setMaximumSize(new Dimension(330, 40));

        JButton loginButton = createButton(
                "LOGIN",
                BLUE
        );

        JButton emergencyButton = createButton(
                "EMERGENCY - NEED HELP",
                RED
        );

        loginButton.addActionListener(e -> {

            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = roleBox.getSelectedItem().toString();

            if (username.isEmpty() || password.isEmpty()) {

                showMessage(
                        frame,
                        "Please enter username and password.",
                        "Login Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (role.equals("Nurse")) {

                if (username.equalsIgnoreCase("nurse")
                        && password.equals("1234")) {

                    frame.dispose();
                    showNurseDashboard();

                } else {

                    showMessage(
                            frame,
                            "Invalid nurse login.\nUse: nurse / 1234",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } else {

                Patient patient = findPatientByUsername(username);

                if (patient != null
                        && password.equals("1234")) {

                    frame.dispose();
                    showPatientDashboard(patient);

                } else {

                    showMessage(
                            frame,
                            "Invalid patient login.\nTry Rahul, Anjali or Kiran\nPassword: 1234",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        emergencyButton.addActionListener(e -> {

            String name = JOptionPane.showInputDialog(
                    frame,
                    "Enter patient name:"
            );

            if (name == null || name.trim().isEmpty()) {
                return;
            }

            EmergencyCase emergencyCase =
                    new EmergencyCase(
                            999,
                            name,
                            "CRITICAL",
                            "Waiting"
                    );

            emergencyCases.add(emergencyCase);

            showMessage(
                    frame,
                    "Emergency request registered!\n\n"
                            + "Patient: " + name
                            + "\nPriority: CRITICAL"
                            + "\nStatus: Waiting",
                    "Emergency Request",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(25));

        card.add(loginLabel);
        card.add(Box.createVerticalStrut(15));

        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(usernameField);

        card.add(Box.createVerticalStrut(10));

        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(passwordField);

        card.add(Box.createVerticalStrut(10));

        card.add(roleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(roleBox);

        card.add(Box.createVerticalStrut(20));
        card.add(loginButton);

        card.add(Box.createVerticalStrut(10));
        card.add(emergencyButton);

        mainPanel.add(card);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // =========================================================
    // PATIENT DASHBOARD
    // =========================================================

    private static void showPatientDashboard(Patient patient) {

        JFrame frame = createFrame(
                "Patient Dashboard - Hospital Emergency System",
                900,
                650
        );

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(new EmptyBorder(20, 20, 20, 20));
        main.setBackground(GRAY);

        JLabel header = new JLabel(
                "PATIENT DASHBOARD"
        );

        header.setFont(new Font(
                "Arial",
                Font.BOLD,
                28
        ));

        header.setForeground(DARK_BLUE);

        main.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setBackground(Color.WHITE);
        center.setBorder(new EmptyBorder(25, 30, 25, 30));

        center.setLayout(
                new BoxLayout(center, BoxLayout.Y_AXIS)
        );

        JLabel welcome = new JLabel(
                "Welcome, " + patient.name
        );

        welcome.setFont(
                new Font("Arial", Font.BOLD, 25)
        );

        welcome.setForeground(BLUE);

        center.add(welcome);
        center.add(Box.createVerticalStrut(20));

        center.add(infoLabel(
                "Patient ID: " + patient.id
        ));

        center.add(infoLabel(
                "Name: " + patient.name
        ));

        center.add(infoLabel(
                "Phone: " + patient.phone
        ));

        center.add(infoLabel(
                "Medical Problem: " + patient.problem
        ));

        center.add(Box.createVerticalStrut(20));

        EmergencyCase caseData =
                findEmergencyCase(patient.id);

        if (caseData != null) {

            center.add(infoLabel(
                    "Emergency Priority: "
                            + caseData.priority
            ));

            center.add(infoLabel(
                    "Status: " + caseData.status
            ));

        } else {

            center.add(infoLabel(
                    "Emergency Priority: No active case"
            ));
        }

        center.add(Box.createVerticalStrut(25));

        JButton helpButton = createButton(
                "NEED HELP",
                RED
        );

        helpButton.setMaximumSize(
                new Dimension(300, 50)
        );

        helpButton.addActionListener(e -> sendPatientRequest(frame, patient));

        center.add(helpButton);

        JButton logout = createButton(
                "LOGOUT",
                DARK_BLUE
        );

        logout.setMaximumSize(
                new Dimension(180, 45)
        );

        logout.addActionListener(e -> {

            frame.dispose();
            showLoginScreen();
        });

        center.add(Box.createVerticalStrut(15));
        center.add(logout);

        main.add(center, BorderLayout.CENTER);

        frame.add(main);
        frame.setVisible(true);
    }

    // =========================================================
    // NURSE DASHBOARD
    // =========================================================

    private static void showNurseDashboard() {

        JFrame frame = createFrame(
                "Nurse Dashboard - Hospital Emergency System",
                1100,
                700
        );

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(new EmptyBorder(15, 15, 15, 15));
        main.setBackground(GRAY);

        JLabel title = new JLabel(
                "NURSE DASHBOARD"
        );

        title.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        title.setForeground(DARK_BLUE);

        main.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(
                new BorderLayout(10, 10)
        );

        content.setBackground(GRAY);

        // ---------- STATISTICS ----------

        JPanel stats = new JPanel(
                new GridLayout(1, 4, 10, 10)
        );

        stats.setBackground(GRAY);

        int critical = countPriority("CRITICAL");
        int high = countPriority("HIGH");
        int medium = countPriority("MEDIUM");
        int low = countPriority("LOW");

        stats.add(
                createStatCard(
                        "CRITICAL",
                        critical,
                        RED
                )
        );

        stats.add(
                createStatCard(
                        "HIGH",
                        high,
                        ORANGE
                )
        );

        stats.add(
                createStatCard(
                        "MEDIUM",
                        medium,
                        BLUE
                )
        );

        stats.add(
                createStatCard(
                        "LOW",
                        low,
                        GREEN
                )
        );

        content.add(stats, BorderLayout.NORTH);

        // ---------- TABLE ----------

        String[] columns = {
                "Patient ID",
                "Patient Name",
                "Priority",
                "Status"
        };

        Object[][] data =
                new Object[emergencyCases.size()][4];

        List<EmergencyCase> sortedCases =
                new ArrayList<>(emergencyCases);

        sortedCases.sort(
                Comparator.comparingInt(
                        Main::priorityValue
                ).reversed()
        );

        for (int i = 0; i < sortedCases.size(); i++) {

            EmergencyCase c = sortedCases.get(i);

            data[i][0] = c.patientId;
            data[i][1] = c.patientName;
            data[i][2] = c.priority;
            data[i][3] = c.status;
        }

        JTable table = new JTable(data, columns);

        table.setRowHeight(32);
        table.setFont(
                new Font("Arial", Font.PLAIN, 14)
        );

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        JScrollPane scrollPane =
                new JScrollPane(table);

        content.add(
                scrollPane,
                BorderLayout.CENTER
        );
        JPanel side = new JPanel(new GridLayout(2, 1, 10, 10));
        side.setBackground(GRAY);
        side.setPreferredSize(new Dimension(320, 0));
        side.add(new RequestsPanel());
        side.add(new BedsPanel());
        content.add(side, BorderLayout.EAST);
        // ---------- BUTTONS ----------


        JPanel buttons = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        15,
                        10
                )
        );

        buttons.setBackground(GRAY);

        JButton refresh = createButton(
                "REFRESH",
                BLUE
        );

        refresh.addActionListener(e -> {

            frame.dispose();
            showNurseDashboard();
        });

        JButton treat = createButton(
                "MARK AS TREATED",
                GREEN
        );

        treat.addActionListener(e -> {

            int selected =
                    table.getSelectedRow();

            if (selected == -1) {

                showMessage(
                        frame,
                        "Please select an emergency case.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String name =
                    table.getValueAt(
                            selected,
                            1
                    ).toString();

            EmergencyCase selectedCase =
                    findEmergencyCaseByName(name);

            if (selectedCase != null) {

                selectedCase.status = "Treated";

                showMessage(
                        frame,
                        "Patient marked as treated.",
                        "Updated",
                        JOptionPane.INFORMATION_MESSAGE
                );

                frame.dispose();
                showNurseDashboard();
            }
        });

        JButton logout = createButton(
                "LOGOUT",
                DARK_BLUE
        );

        logout.addActionListener(e -> {

            frame.dispose();
            showLoginScreen();
        });
        JButton addBtn = createButton(
                "ADD PATIENT",
                DARK_BLUE
        );

        addBtn.addActionListener(e -> {

            JTextField nameField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField problemField = new JTextField();
            JComboBox<String> priorityBox = new JComboBox<>(
                    new String[]{"CRITICAL", "HIGH", "MEDIUM", "LOW"}
            );
            priorityBox.setSelectedItem("MEDIUM");

            Object[] form = {
                    "Patient Name:", nameField,
                    "Phone:", phoneField,
                    "Problem:", problemField,
                    "Priority:", priorityBox
            };

            int result = JOptionPane.showConfirmDialog(
                    frame,
                    form,
                    "Add New Patient",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) return;

            String newName = nameField.getText().trim();

            if (newName.isEmpty()) {
                showMessage(
                        frame,
                        "Patient name is required.",
                        "Missing Name",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (findEmergencyCaseByName(newName) != null) {
                showMessage(
                        frame,
                        "A patient with this name already exists.",
                        "Duplicate",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int newId = 101;
            for (Patient p : patients) {
                newId = Math.max(newId, p.id + 1);
            }
            for (EmergencyCase c : emergencyCases) {
                newId = Math.max(newId, c.patientId + 1);
            }

            patients.add(new Patient(
                    newId,
                    newName,
                    phoneField.getText().trim(),
                    problemField.getText().trim()
            ));

            emergencyCases.add(new EmergencyCase(
                    newId,
                    newName,
                    (String) priorityBox.getSelectedItem(),
                    "Waiting"
            ));

            frame.dispose();
            showNurseDashboard();
        });

        JButton priorityBtn = createButton(
                "CHANGE PRIORITY",
                ORANGE
        );

        priorityBtn.addActionListener(e -> {

            int selected = table.getSelectedRow();

            if (selected == -1) {
                showMessage(
                        frame,
                        "Please select a patient.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String name = table.getValueAt(selected, 1).toString();
            EmergencyCase c = findEmergencyCaseByName(name);

            if (c == null) return;

            String[] options = {"CRITICAL", "HIGH", "MEDIUM", "LOW"};

            String choice = (String) JOptionPane.showInputDialog(
                    frame,
                    "Select priority for " + name,
                    "Update Priority",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    c.priority
            );

            if (choice != null) {
                c.priority = choice;
                frame.dispose();
                showNurseDashboard();
            }
        });
        buttons.add(refresh);
        buttons.add(addBtn);
        buttons.add(treat);
        buttons.add(priorityBtn);
        buttons.add(logout);

        main.add(content, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);

        frame.add(main);
        frame.setVisible(true);
    }
    private static void sendPatientRequest(Component parent, Patient patient) {

        String[] types = {
                "Need nurse", "Need water", "Need medicine",
                "Pain / discomfort", "Washroom help", "EMERGENCY"
        };

        JComboBox<String> typeBox = new JComboBox<>(types);

        JComboBox<Integer> bedBox = new JComboBox<>(
                new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
        );

        Object[] form = {
                "Your Bed No:", bedBox,
                "What do you need?", typeBox
        };

        int result = JOptionPane.showConfirmDialog(
                parent,
                form,
                "Request Help",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {
            RequestService.addRequest(
                    patient.id,
                    patient.name,
                    (Integer) bedBox.getSelectedItem(),
                    (String) typeBox.getSelectedItem()
            );
            if ("EMERGENCY".equals(typeBox.getSelectedItem())) {
                EmergencyCase existing = findEmergencyCase(patient.id);
                if (existing != null) {
                    existing.priority = "CRITICAL";
                    existing.status = "Waiting";
                } else {
                    emergencyCases.add(new EmergencyCase(
                            patient.id, patient.name, "CRITICAL", "Waiting"
                    ));
                }
            }
            showMessage(
                    parent,
                    "Your request was sent to the nurse.",
                    "Request Sent",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            showMessage(
                    parent,
                    "Could not send request: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    // =========================================================
    // UI HELPERS
    // =========================================================

    private static JFrame createFrame(
            String title,
            int width,
            int height
    ) {

        JFrame frame = new JFrame(title);

        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        return frame;
    }

    private static JButton createButton(
            String text,
            Color color
    ) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Arial", Font.BOLD, 14)
        );

        button.setForeground(Color.WHITE);
        button.setBackground(color);

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setPreferredSize(
                new Dimension(180, 45)
        );

        return button;
    }

    private static JLabel infoLabel(
            String text
    ) {

        JLabel label = new JLabel(text);

        label.setFont(
                new Font("Arial", Font.PLAIN, 18)
        );

        label.setBorder(
                new EmptyBorder(8, 0, 8, 0)
        );

        return label;
    }

    private static JPanel createStatCard(
            String title,
            int value,
            Color color
    ) {

        JPanel panel = new JPanel();

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setBackground(Color.WHITE);
        panel.setBorder(
                new EmptyBorder(15, 15, 15, 15)
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        titleLabel.setForeground(color);
        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel valueLabel =
                new JLabel(
                        String.valueOf(value)
                );

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        valueLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(valueLabel);

        return panel;
    }

    private static void showMessage(
            Component parent,
            String message,
            String title,
            int type
    ) {

        JOptionPane.showMessageDialog(
                parent,
                message,
                title,
                type
        );
    }

    // =========================================================
    // DATA METHODS
    // =========================================================

    private static Patient findPatientByUsername(
            String username
    ) {

        for (Patient patient : patients) {

            if (patient.name.equalsIgnoreCase(username)) {
                return patient;
            }
        }

        return null;
    }

    private static EmergencyCase findEmergencyCase(
            int patientId
    ) {

        for (EmergencyCase c : emergencyCases) {

            if (c.patientId == patientId) {
                return c;
            }
        }

        return null;
    }

    private static EmergencyCase findEmergencyCaseByName(
            String name
    ) {

        for (EmergencyCase c : emergencyCases) {

            if (c.patientName.equalsIgnoreCase(name)) {
                return c;
            }
        }

        return null;
    }

    private static int countPriority(
            String priority
    ) {

        int count = 0;

        for (EmergencyCase c : emergencyCases) {

            if (c.priority.equals(priority)) {
                count++;
            }
        }

        return count;
    }

    private static int priorityValue(
            EmergencyCase c
    ) {

        return switch (c.priority) {

            case "CRITICAL" -> 4;
            case "HIGH" -> 3;
            case "MEDIUM" -> 2;
            case "LOW" -> 1;

            default -> 0;
        };
    }

    // =========================================================
    // PATIENT CLASS
    // =========================================================

    static class Patient {

        int id;
        String name;
        String phone;
        String problem;

        Patient(
                int id,
                String name,
                String phone,
                String problem
        ) {

            this.id = id;
            this.name = name;
            this.phone = phone;
            this.problem = problem;
        }
    }

    // =========================================================
    // EMERGENCY CASE CLASS
    // =========================================================

    static class EmergencyCase {

        int patientId;
        String patientName;
        String priority;
        String status;

        EmergencyCase(
                int patientId,
                String patientName,
                String priority,
                String status
        ) {

            this.patientId = patientId;
            this.patientName = patientName;
            this.priority = priority;
            this.status = status;
        }
    }
}