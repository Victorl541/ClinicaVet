package com.clinicavet.views.panels;

import com.clinicavet.controllers.MainController;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.Owner;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ListPetsPanelAdapter extends JPanel {

    private final MainController controller;

    // Campos principales
    private final JTextField txtName = new JTextField(12);
    private final JTextField txtBreed = new JTextField(12);
    private final JComboBox<String> cmbSpecies = new JComboBox<>(new String[]{"Perro", "Gato"});
    private final JComboBox<String> cmbSex = new JComboBox<>(new String[]{"Macho", "Hembra"});
    private final JSpinner spinAge = new JSpinner(new SpinnerNumberModel(1, 0, 50, 1));
    private final JSpinner spinWeight = new JSpinner(new SpinnerNumberModel(5.0, 0.5, 200.0, 0.5));
    private final JTextField txtOwnerSearch = new JTextField(15);
    private final JLabel lblSelectedOwner = new JLabel("Seleccionar dueño...");

    // Campos de información médica
    private final JTextArea txtAllergies = new JTextArea(2, 30);
    private final JTextArea txtVaccines = new JTextArea(2, 30);
    private final JTextArea txtMedicalNotes = new JTextArea(4, 30);
    private final JTextField txtSearch = new JTextField(20);

    // Botones
    public final JButton btnAdd = new JButton("Agregar Mascota");
    public final JButton btnEdit = new JButton("Editar");
    public final JButton btnDelete = new JButton("Eliminar");
    public final JButton btnSearch = new JButton("Buscar");
    public final JButton btnRefresh = new JButton("Refrescar");
    public final JButton btnClear = new JButton("Limpiar");
    public final JButton btnSelectOwner = new JButton("Buscar Dueño");

    public JTable table;
    public DefaultTableModel tableModel;

    private UUID selectedPetId = null;
    private Owner selectedOwner = null;

    public ListPetsPanelAdapter(MainController controller) {
        this.controller = controller;
        initUI();
        load();
        initListeners();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // PANEL SUPERIOR: FORMULARIO EN DOS COLUMNAS
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);

        // TABLA CENTRAL
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Especie", "Raza", "Sexo", "Edad", "Peso", "Dueño"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(26);
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        add(scroll, BorderLayout.CENTER);

        // PANEL INFERIOR: BÚSQUEDA
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Buscar (nombre / especie / raza):"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        add(searchPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel createFormPanel() {
        JPanel mainForm = new JPanel(new BorderLayout(10, 10));
        mainForm.setBackground(Color.WHITE);
        mainForm.setBorder(BorderFactory.createTitledBorder("Información de la Mascota"));

        // Panel izquierdo: campos básicos
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Fila 1: Nombre y Especie
        gbc.gridx = 0; gbc.gridy = 0;
        leftPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(txtName, gbc);
        gbc.gridx = 2;
        leftPanel.add(new JLabel("Especie:"), gbc);
        gbc.gridx = 3;
        leftPanel.add(cmbSpecies, gbc);

        // Fila 2: Raza y Sexo
        gbc.gridx = 0; gbc.gridy = 1;
        leftPanel.add(new JLabel("Raza:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(txtBreed, gbc);
        gbc.gridx = 2;
        leftPanel.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 3;
        leftPanel.add(cmbSex, gbc);

        // Fila 3: Edad y Peso
        gbc.gridx = 0; gbc.gridy = 2;
        leftPanel.add(new JLabel("Edad (años):"), gbc);
        gbc.gridx = 1;
        leftPanel.add(spinAge, gbc);
        gbc.gridx = 2;
        leftPanel.add(new JLabel("Peso (kg):"), gbc);
        gbc.gridx = 3;
        leftPanel.add(spinWeight, gbc);

        // Fila 4: Dueño
        gbc.gridx = 0; gbc.gridy = 3;
        leftPanel.add(new JLabel("Dueño:"), gbc);
        gbc.gridx = 1;
        leftPanel.add(lblSelectedOwner, gbc);
        gbc.gridx = 2; gbc.gridwidth = 2;
        leftPanel.add(btnSelectOwner, gbc);
        gbc.gridwidth = 1;

        mainForm.add(leftPanel, BorderLayout.NORTH);

        // Panel derecho: campos médicos (con scroll)
        JPanel medicalPanel = new JPanel(new GridBagLayout());
        medicalPanel.setBackground(Color.WHITE);
        medicalPanel.setBorder(BorderFactory.createTitledBorder("Información Médica"));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // Alergias
        gbc.gridx = 0; gbc.gridy = 0;
        medicalPanel.add(new JLabel("Alergias:"), gbc);
        gbc.gridy = 1; gbc.weighty = 0.3;
        txtAllergies.setLineWrap(true);
        txtAllergies.setWrapStyleWord(true);
        medicalPanel.add(new JScrollPane(txtAllergies), gbc);

        // Vacunas
        gbc.gridy = 2; gbc.weighty = 0.0;
        medicalPanel.add(new JLabel("Vacunas:"), gbc);
        gbc.gridy = 3; gbc.weighty = 0.3;
        txtVaccines.setLineWrap(true);
        txtVaccines.setWrapStyleWord(true);
        medicalPanel.add(new JScrollPane(txtVaccines), gbc);

        // Notas Médicas
        gbc.gridy = 4; gbc.weighty = 0.0;
        medicalPanel.add(new JLabel("Notas Médicas:"), gbc);
        gbc.gridy = 5; gbc.weighty = 0.4;
        txtMedicalNotes.setLineWrap(true);
        txtMedicalNotes.setWrapStyleWord(true);
        medicalPanel.add(new JScrollPane(txtMedicalNotes), gbc);

        // Botones de acción
        gbc.gridy = 6; gbc.weighty = 0.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnEdit);
        buttonsPanel.add(btnDelete);
        buttonsPanel.add(btnClear);
        medicalPanel.add(buttonsPanel, gbc);

        mainForm.add(medicalPanel, BorderLayout.CENTER);

        return mainForm;
    }

    public void load() {
        tableModel.setRowCount(0);
        List<Pet> pets = controller.listAllPets();
        for (Pet p : pets) {
            tableModel.addRow(new Object[]{
                    p.getId().toString(),
                    p.getName(),
                    p.getSpecies(),
                    p.getBreed(),
                    p.getSex(),
                    p.getAge(),
                    String.format("%.2f", p.getWeight()),
                    p.getOwner() != null ? p.getOwner().getName() : "Sin dueño"
            });
        }
    }

    public void reload() { load(); }

    private void initListeners() {
        // Selección en tabla
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        try {
                            String idStr = tableModel.getValueAt(row, 0).toString();
                            selectedPetId = UUID.fromString(idStr);
                            Optional<Pet> opt = controller.findPetById(selectedPetId);
                            if (opt.isPresent()) {
                                Pet p = opt.get();
                                txtName.setText(p.getName());
                                cmbSpecies.setSelectedItem(p.getSpecies());
                                txtBreed.setText(p.getBreed());
                                cmbSex.setSelectedItem(p.getSex());
                                spinAge.setValue(p.getAge());
                                spinWeight.setValue(p.getWeight());
                                selectedOwner = p.getOwner();
                                lblSelectedOwner.setText(p.getOwner() != null ? p.getOwner().getName() : "Sin dueño");
                                txtAllergies.setText(p.getAllergies() != null ? p.getAllergies() : "");
                                txtVaccines.setText(p.getVaccines() != null ? p.getVaccines() : "");
                                txtMedicalNotes.setText(p.getMedicalNotes() != null ? p.getMedicalNotes() : "");
                            } else {
                                selectedPetId = null;
                            }
                        } catch (Exception ex) {
                            selectedPetId = null;
                        }
                    }
                }
            }
        });

        // Seleccionar dueño: abrir cuadro de búsqueda para encontrar dueño por nombre/cédula/email
        btnSelectOwner.addActionListener(e -> {
            String query = JOptionPane.showInputDialog(this, "Buscar dueño por nombre / cédula / email:");
            if (query == null) return; // cancelado
            query = query.trim();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese texto para buscar", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Optional<Owner> opt = controller.findOwnerByQuery(query);
            if (opt.isPresent()) {
                selectedOwner = opt.get();
                lblSelectedOwner.setText(selectedOwner.getName() + " (" + selectedOwner.getId() + ")");
            } else {
                JOptionPane.showMessageDialog(this, "Dueño no encontrado. Intente con otro texto.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Agregar mascota
        btnAdd.addActionListener(e -> {
            String name = txtName.getText().trim();
            String species = (String) cmbSpecies.getSelectedItem();
            String breed = txtBreed.getText().trim();
            String sex = (String) cmbSex.getSelectedItem();
            int age = (Integer) spinAge.getValue();
            double weight = (Double) spinWeight.getValue();
            String allergies = txtAllergies.getText().trim();
            String vaccines = txtVaccines.getText().trim();
            String medicalNotes = txtMedicalNotes.getText().trim();

            if (name.isEmpty() || selectedOwner == null) {
                JOptionPane.showMessageDialog(this, "Nombre y dueño son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            controller.createPet(age, name, sex, species, breed, allergies, vaccines, medicalNotes, weight, selectedOwner);
            reload();
            clearFields();
        });

        // Editar mascota
        btnEdit.addActionListener(e -> {
            if (selectedPetId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una mascota para editar", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Optional<Pet> opt = controller.findPetById(selectedPetId);
            if (opt.isPresent()) {
                Pet p = opt.get();
                p.setName(txtName.getText().trim());
                p.setSpecies((String) cmbSpecies.getSelectedItem());
                p.setBreed(txtBreed.getText().trim());
                p.setSex((String) cmbSex.getSelectedItem());
                p.setAge((Integer) spinAge.getValue());
                p.setWeight((Double) spinWeight.getValue());
                if (selectedOwner != null) p.setOwner(selectedOwner);
                p.setAllergies(txtAllergies.getText().trim());
                p.setVaccines(txtVaccines.getText().trim());
                p.setMedicalNotes(txtMedicalNotes.getText().trim());
                controller.updatePet(p);
                reload();
                clearFields();
            }
        });

        // Eliminar mascota
        btnDelete.addActionListener(e -> {
            if (selectedPetId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una mascota para eliminar", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar mascota seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deletePet(selectedPetId);
                reload();
                clearFields();
            }
        });

        // Buscar
        btnSearch.addActionListener(e -> {
            String q = txtSearch.getText().trim();
            if (q.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese texto para buscar", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Optional<Pet> opt = controller.findPetByQuery(q);
            if (opt.isPresent()) {
                Pet p = opt.get();
                for (int r = 0; r < tableModel.getRowCount(); r++) {
                    if (tableModel.getValueAt(r, 0).toString().equals(p.getId().toString())) {
                        table.setRowSelectionInterval(r, r);
                        Rectangle rect = table.getCellRect(r, 0, true);
                        table.scrollRectToVisible(rect);
                        return;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró mascota", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnRefresh.addActionListener(e -> { reload(); clearFields(); });
        btnClear.addActionListener(e -> clearFields());
    }

    private void clearFields() {
        txtName.setText("");
        cmbSpecies.setSelectedIndex(0);
        txtBreed.setText("");
        cmbSex.setSelectedIndex(0);
        spinAge.setValue(1);
        spinWeight.setValue(5.0);
        selectedOwner = null;
        lblSelectedOwner.setText("Seleccionar dueño...");
        txtAllergies.setText("");
        txtVaccines.setText("");
        txtMedicalNotes.setText("");
        txtSearch.setText("");
        table.clearSelection();
        selectedPetId = null;
    }
}