package org.example.hospitalemergencyprioritysystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BedsPanel extends JPanel {

    private static final boolean[] occupied = new boolean[10];

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Bed No", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JLabel summary = new JLabel();

    public BedsPanel() {
        setLayout(new BorderLayout(5, 5));

        JLabel title = new JLabel("BEDS");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(40, 150, 90));

        summary.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.add(title);
        top.add(summary);

        JButton toggle = new JButton("MARK OCCUPIED / AVAILABLE");
        toggle.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                occupied[row] = !occupied[row];
                refresh();
                table.setRowSelectionInterval(row, row);
            }
        });

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(toggle, BorderLayout.SOUTH);

        refresh();
    }

    private void refresh() {
        model.setRowCount(0);
        int free = 0;
        for (int i = 0; i < occupied.length; i++) {
            model.addRow(new Object[]{i + 1, occupied[i] ? "OCCUPIED" : "AVAILABLE"});
            if (!occupied[i]) free++;
        }
        summary.setText("Available: " + free + " / " + occupied.length);
    }
}