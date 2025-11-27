package com.clinicavet.views;

import com.clinicavet.controllers.PetFormDialogController;
import com.clinicavet.model.entities.Owner;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.services.IOwnerService;
import com.clinicavet.model.services.IPetService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PetFormDialog extends JDialog {

    private JTextField txtNombre;
    private JTextField txtEspecie;
    private JTextField txtRaza;
    private JComboBox<String> cbSexo;
    private JSpinner spnAge;
    private JSpinner spnWeight;
    private JComboBox<String> cbDueno;
    private JTextArea txtAlergias;
    private JTextArea txtVacunas;
    private JTextArea txtNotasMedicas;
    private JButton btnSave;
    private JButton btnCancel;

    private PetFormDialogController controller;

    public PetFormDialog(Pet pet, IPetService petService, IOwnerService ownerService) {
        setTitle(pet == null ? "Nueva Mascota" : "Editar Mascota");
        setModal(true);
        setSize(600, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();

        this.controller = new PetFormDialogController(this, pet, petService, ownerService);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Fila 1: Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        mainPanel.add(txtNombre, gbc);

        // Fila 2: Especie
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Especie:"), gbc);
        gbc.gridx = 1;
        txtEspecie = new JTextField(20);
        mainPanel.add(txtEspecie, gbc);

        // Fila 3: Raza
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Raza:"), gbc);
        gbc.gridx = 1;
        txtRaza = new JTextField(20);
        mainPanel.add(txtRaza, gbc);

        // Fila 4: Sexo
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1;
        cbSexo = new JComboBox<>(new String[]{"Macho", "Hembra"});
        mainPanel.add(cbSexo, gbc);

        // Fila 5: Edad
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Edad (años):"), gbc);
        gbc.gridx = 1;
        spnAge = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        mainPanel.add(spnAge, gbc);

        // Fila 6: Peso
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Peso (kg):"), gbc);
        gbc.gridx = 1;
        spnWeight = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 500.0, 0.5));
        mainPanel.add(spnWeight, gbc);

        // Fila 7: Dueño
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(new JLabel("Dueño:"), gbc);
        gbc.gridx = 1;
        cbDueno = new JComboBox<>();
        mainPanel.add(cbDueno, gbc);

        // Fila 8: Alergias
        gbc.gridx = 0; gbc.gridy = 7;
        mainPanel.add(new JLabel("Alergias:"), gbc);
        gbc.gridx = 1;
        gbc.weighty = 0.3;
        txtAlergias = new JTextArea(3, 20);
        txtAlergias.setLineWrap(true);
        txtAlergias.setWrapStyleWord(true);
        mainPanel.add(new JScrollPane(txtAlergias), gbc);
        gbc.weighty = 0;

        // Fila 9: Vacunas
        gbc.gridx = 0; gbc.gridy = 8;
        mainPanel.add(new JLabel("Vacunas:"), gbc);
        gbc.gridx = 1;
        gbc.weighty = 0.3;
        txtVacunas = new JTextArea(3, 20);
        txtVacunas.setLineWrap(true);
        txtVacunas.setWrapStyleWord(true);
        mainPanel.add(new JScrollPane(txtVacunas), gbc);
        gbc.weighty = 0;

        // Fila 10: Notas médicas
        gbc.gridx = 0; gbc.gridy = 9;
        mainPanel.add(new JLabel("Notas Médicas:"), gbc);
        gbc.gridx = 1;
        gbc.weighty = 0.3;
        txtNotasMedicas = new JTextArea(3, 20);
        txtNotasMedicas.setLineWrap(true);
        txtNotasMedicas.setWrapStyleWord(true);
        mainPanel.add(new JScrollPane(txtNotasMedicas), gbc);
        gbc.weighty = 0;

        // Fila 11: Botones
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);

        btnSave = new JButton("Guardar");
        btnCancel = new JButton("Cancelar");
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        mainPanel.add(btnPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane);
    }

    // Getters para el controlador
    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtEspecie() {
        return txtEspecie;
    }

    public JTextField getTxtRaza() {
        return txtRaza;
    }

    public JComboBox<String> getCbSexo() {
        return cbSexo;
    }

    public JSpinner getSpnAge() {
        return spnAge;
    }

    public JSpinner getSpnWeight() {
        return spnWeight;
    }

    public JComboBox<String> getCbDueno() {
        return cbDueno;
    }

    public JTextArea getTxtAlergias() {
        return txtAlergias;
    }

    public JTextArea getTxtVacunas() {
        return txtVacunas;
    }

    public JTextArea getTxtNotasMedicas() {
        return txtNotasMedicas;
    }

    public JButton getBtnSave() {
        return btnSave;
    }
}