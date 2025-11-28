package com.clinicavet.views;

import com.clinicavet.controllers.UserFormDialogController;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.model.services.RolService;

import javax.swing.*;
import java.awt.*;

public class UserFormDialog extends JDialog {

    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRol;
    private JCheckBox chkActivo;
    private JButton btnSave;
    private JButton btnCancel;

    private UserFormDialogController controller;
    private boolean isEditMode;

    public UserFormDialog(User user, IUserService userService, RolService rolService) {
        setTitle(user == null ? "Nuevo Usuario" : "Editar Usuario");
        setModal(true);
        setSize(450, isEditMode(user) ? 350 : 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.isEditMode = user != null;
        
        initComponents();
        
        // Crear controlador y pasar la vista
        this.controller = new UserFormDialogController(this, user, userService, rolService);
    }

    private boolean isEditMode(User user) {
        return user != null;
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

        // Fila 2: Email
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        mainPanel.add(txtEmail, gbc);

        // Fila 3: Contraseña
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        mainPanel.add(txtPassword, gbc);

        // Fila 4: Rol
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        cbRol = new JComboBox<>();
        mainPanel.add(cbRol, gbc);

        int nextRow = 4;

        // Fila 5: Estado (SOLO EN MODO EDICIÓN)
        if (isEditMode) {
            gbc.gridx = 0; gbc.gridy = nextRow;
            mainPanel.add(new JLabel("Estado:"), gbc);
            gbc.gridx = 1;
            chkActivo = new JCheckBox("Activo");
            mainPanel.add(chkActivo, gbc);
            nextRow++;
        } else {
            // En modo creación, crear el checkbox oculto pero activo
            chkActivo = new JCheckBox("Activo");
            chkActivo.setSelected(true);
            chkActivo.setVisible(false);
        }

        // Fila 6: Botones
        gbc.gridx = 0; gbc.gridy = nextRow; gbc.gridwidth = 2;
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

    // Getters para que el controlador acceda a los campos
    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JComboBox<String> getCbRol() {
        return cbRol;
    }

    public JCheckBox getChkActivo() {
        return chkActivo;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public boolean isEditMode() {
        return isEditMode;
    }
}