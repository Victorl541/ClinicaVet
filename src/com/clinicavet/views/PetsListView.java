package com.clinicavet.views;

import com.clinicavet.model.entities.Pet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PetsListView extends JPanel {

    public JTable table;
    public JTextField txtSearch;
    public JButton btnSearch;
    public JButton btnNew;
    public JButton btnEdit;

    public PetsListView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // --- PANEL SUPERIOR: BÚSQUEDA ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(new JLabel("Buscar por nombre / especie / dueño / raza:"));
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

        btnNew = new JButton("Nueva Mascota");
        btnEdit = new JButton("Editar");

        bottomPanel.add(btnNew);
        bottomPanel.add(btnEdit);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setTableData(List<Pet> pets) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Especie", "Raza", "Sexo", "Edad", "Peso", "Dueño"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Pet p : pets) {
            model.addRow(new Object[]{
                    p.getId().toString().substring(0, 8) + "...",
                    p.getName(),
                    p.getSpecies(),
                    p.getBreed(),
                    p.getSex(),
                    p.getAge(),
                    p.getWeight(),
                    p.getOwner().getName()
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

    public String getPetIdAt(int row) {
        return table.getValueAt(row, 0).toString();
    }

    public void clearSearch() {
        txtSearch.setText("");
    }
}