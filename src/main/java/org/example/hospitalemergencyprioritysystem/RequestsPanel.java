package org.example.hospitalemergencyprioritysystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RequestsPanel extends JPanel {

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Bed", "Patient", "Request"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private int lastCount = 0;

    public RequestsPanel() {
        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(320, 0));

        JLabel title = new JLabel("PATIENT REQUESTS");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(new Color(192, 57, 43));

        JButton done = new JButton("MARK REQUEST DONE");
        done.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                try {
                    int id = (int) model.getValueAt(row, 0);
                    RequestService.markDone(id);
                    refresh(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(done, BorderLayout.SOUTH);

        refresh(false);
        new javax.swing.Timer(5000, e -> refresh(true)).start();
    }

    private void refresh(boolean alertOnNew) {
        try {
            List<RequestService.RequestItem> list = RequestService.getPending();
            model.setRowCount(0);
            for (var r : list) {
                model.addRow(new Object[]{r.id(), r.bedNo(), r.patientName(), r.message()});
            }
            if (alertOnNew && list.size() > lastCount) {
                var r = list.get(0);
                JOptionPane.showMessageDialog(this,
                        "Bed " + r.bedNo() + ": " + r.message() + " (" + r.patientName() + ")",
                        "NEW PATIENT REQUEST!", JOptionPane.WARNING_MESSAGE);
            }
            lastCount = list.size();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}