package com.clinicavet.views.panels;

import com.clinicavet.controllers.MainController;
import com.clinicavet.model.entities.User;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class EditUserPanelAdapter extends JPanel {
    private final MainController controller;
    private JTextField txtSearch;
    private JTextField txtName, txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRoles;
    private JButton btnSearch, btnSave;
    private User current;

    public EditUserPanelAdapter(MainController controller) {
        this.controller = controller;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Buscar por email o nombre:"), gbc);

        gbc.gridx = 1;
        txtSearch = new JTextField(20);
        form.add(txtSearch, gbc);

        gbc.gridx = 2;
        btnSearch = new JButton("Buscar");
        form.add(btnSearch, gbc);
        btnSearch.addActionListener(e -> buscar());

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtName = new JTextField(20);
        form.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtEmail = new JTextField(20);
        form.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        form.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtPassword = new JPasswordField(20);
        form.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        form.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        cmbRoles = new JComboBox<>();

        // Cargar roles, excluyendo Admin
        controller.rolService.listRoles()
                .stream()
                .filter(r -> !r.getName().equalsIgnoreCase("Admin"))
                .forEach(r -> cmbRoles.addItem(r.getName()));

        form.add(cmbRoles, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        btnSave = new JButton("Guardar cambios");
        btnSave.addActionListener(e -> guardar());
        form.add(btnSave, gbc);

        add(form, BorderLayout.NORTH);
        bloquearCampos(false);
    }

    private void buscar() {
        String q = txtSearch.getText().trim();
        if (q.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese nombre o correo del usuario");
            return;
        }

        // Buscar tanto por email como por nombre
        Optional<User> u = controller.findUserByEmailOrName(q);
        if (u.isPresent()) {
            current = u.get();
            txtName.setText(current.getName());
            txtEmail.setText(current.geteMail());
            txtPassword.setText(current.getPassword());

            if (current.getRol() != null) {
                if (current.getRol().getName().equalsIgnoreCase("Admin")) {
                    cmbRoles.setEnabled(false);
                } else {
                    cmbRoles.setEnabled(true);
                    cmbRoles.setSelectedItem(current.getRol().getName());
                }
            }

            bloquearCampos(true);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún usuario con ese nombre o correo");
            limpiar();
            bloquearCampos(false);
        }
    }

    private void guardar() {
        if (current == null) {
            JOptionPane.showMessageDialog(this, "Busque un usuario primero");
            return;
        }

        current.setName(txtName.getText().trim());
        current.seteMail(txtEmail.getText().trim());
        current.setPassword(new String(txtPassword.getPassword()));

        if (current.getRol() == null || !current.getRol().getName().equalsIgnoreCase("Admin")) {
            String selectedRole = (String) cmbRoles.getSelectedItem();
            controller.rolService.getByName(selectedRole)
                    .ifPresent(current::setRol);
        }

        controller.updateUser(current);
        JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente");

        limpiar();
        bloquearCampos(false);
    }

    private void limpiar() {
        txtSearch.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        if (cmbRoles.getItemCount() > 0) cmbRoles.setSelectedIndex(0);
        current = null;
    }

    private void bloquearCampos(boolean enable) {
        txtName.setEnabled(enable);
        txtEmail.setEnabled(enable);
        txtPassword.setEnabled(enable);
        btnSave.setEnabled(enable);
    }
}
