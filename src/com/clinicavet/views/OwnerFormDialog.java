package com.clinicavet.views;

import com.clinicavet.controllers.OwnerFormDialogController;
import com.clinicavet.model.entities.Owner;
import com.clinicavet.model.services.IOwnerService;

import javax.swing.*;
import java.awt.*;

public class OwnerFormDialog extends JDialog {

    private JTextField txtCedula;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtDireccion;
    private JCheckBox chkActivo;
    private JButton btnSave;
    private JButton btnCancel;

    private OwnerFormDialogController controller;

    public OwnerFormDialog(Owner owner, IOwnerService ownerService) {
        setTitle(owner == null ? "Nuevo Dueño" : "Editar Dueño");
        setModal(true);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();

        // Crear controlador y pasar la vista
        this.controller = new OwnerFormDialogController(this, owner, ownerService);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Fila 1: Cédula
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Cédula:"), gbc);
        gbc.gridx = 1;
        txtCedula = new JTextField(20);
        mainPanel.add(txtCedula, gbc);

        // Fila 2: Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        mainPanel.add(txtNombre, gbc);

        // Fila 3: Teléfono
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(20);
        mainPanel.add(txtTelefono, gbc);

        // Fila 4: Email
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        mainPanel.add(txtEmail, gbc);

        // Fila 5: Dirección
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Dirección:"), gbc);
        gbc.gridx = 1;
        txtDireccion = new JTextField(20);
        mainPanel.add(txtDireccion, gbc);

        // Fila 6: Estado
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        chkActivo = new JCheckBox("Activo");
        mainPanel.add(chkActivo, gbc);

        // Fila 7: Botones
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);

        btnSave = new JButton("Guardar");
        btnCancel = new JButton("Cancelar");
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        mainPanel.add(btnPanel, gbc);

        add(mainPanel);
    }

    // Getters para el controlador
    public JTextField getTxtCedula() {
        return txtCedula;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtTelefono() {
        return txtTelefono;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JTextField getTxtDireccion() {
        return txtDireccion;
    }

    public JCheckBox getChkActivo() {
        return chkActivo;
    }

    public JButton getBtnSave() {
        return btnSave;
    }
}