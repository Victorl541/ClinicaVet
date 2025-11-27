package com.clinicavet.views;

import com.clinicavet.model.entities.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentsListView extends JPanel {

    public JTable table;
    public JTextField txtSearch;
    public JButton btnSearch;
    public JButton btnNew;
    public JButton btnEdit;
    public JButton btnCancel;

    public AppointmentsListView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // --- PANEL SUPERIOR: BÚSQUEDA ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(new JLabel("Buscar por mascota / dueño / médico / estado:"));
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

        btnNew = new JButton("Nueva Cita");
        btnEdit = new JButton("Editar");
        btnCancel = new JButton("Cancelar Cita");

        bottomPanel.add(btnNew);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnCancel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setTableData(List<Appointment> appointments) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Mascota", "Dueño", "Médico", "Fecha", "Hora", "Motivo", "Estado"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Appointment a : appointments) {
            model.addRow(new Object[]{
                    a.getId().toString().substring(0, 8) + "...",
                    a.getMascota().getName(),
                    a.getMascota().getOwner().getName(),
                    a.getMedico().getName(),
                    a.getFecha(),
                    a.getHora(),
                    a.getMotivo(),
                    a.getEstado()
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

    public void clearSearch() {
        txtSearch.setText("");
    }
}