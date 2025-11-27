package com.clinicavet.views;

import com.clinicavet.model.entities.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsersListView extends JPanel {

    public JTable table;
    public JTextField txtSearch;
    public JButton btnSearch;
    public JButton btnNew;
    public JButton btnEdit;

    public UsersListView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // --- PANEL SUPERIOR: BÚSQUEDA ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(new JLabel("Buscar por nombre / email / rol:"));
        txtSearch = new JTextField(25);
        topPanel.add(txtSearch);

        btnSearch = new JButton("Buscar");
        topPanel.add(btnSearch);

        add(topPanel, BorderLayout.NORTH);

        // --- TABLA CENTRAL ---
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(26);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // --- PANEL INFERIOR: BOTONES ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(Color.WHITE);

        btnNew = new JButton("Nuevo Usuario");
        btnEdit = new JButton("Editar");

        bottomPanel.add(btnNew);
        bottomPanel.add(btnEdit);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setTableData(List<User> users) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Email", "Rol", "Activo"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (User u : users) {
            model.addRow(new Object[]{
                    u.getId(),
                    u.getName(),
                    u.getEmail(),
                    u.getRol() != null ? u.getRol().getName() : "Sin rol",
                    u.isActivo() ? "Sí" : "No"
            });
        }

        table.setModel(model);
    }

    public String getSearchText() {
        return txtSearch.getText().trim();
    }

    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public long getUserIdAt(int row) {
        return Long.parseLong(table.getValueAt(row, 0).toString());
    }

    public void clearSearch() {
        txtSearch.setText("");
    }
}