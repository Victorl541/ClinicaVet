package com.clinicavet.views.panels;

import com.clinicavet.controllers.MainController;
import com.clinicavet.model.entities.User;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class ResetPasswordPanelAdapter extends JPanel {
    private final MainController controller;
    private JTextField txtEmail;
    private JPasswordField txtNewPass;
    private JButton btnSearch, btnSave;
    private JLabel lblFound;
    private User found;

    public ResetPasswordPanelAdapter(MainController controller) {
        this.controller = controller;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        p.add(new JLabel("Buscar por email:"), gbc);

        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        p.add(txtEmail, gbc);

        gbc.gridx = 2;
        btnSearch = new JButton("Buscar");
        p.add(btnSearch, gbc);
        btnSearch.addActionListener(e -> search());

       
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        lblFound = new JLabel(" ");
        p.add(lblFound, gbc);

    
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        p.add(new JLabel("Nueva contraseña:"), gbc);

        gbc.gridx = 1;
        txtNewPass = new JPasswordField(20);
        p.add(txtNewPass, gbc);

        gbc.gridx = 2;
        btnSave = new JButton("Guardar");
        btnSave.addActionListener(e -> save());
        p.add(btnSave, gbc);

        add(p, BorderLayout.NORTH);
    }

    private void search() {
        String query = txtEmail.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese email o nombre");
            return;
        }

        
        Optional<User> u = controller.findUserByEmail(query);

        if (u.isPresent()) {
            found = u.get();

            
            if ("admin".equalsIgnoreCase(found.getRol().toString())) {
                lblFound.setText("No se puede modificar la contraseña del administrador.");
                found = null;
                return;
            }

            lblFound.setText("Encontrado: " + found.getName() + " (" + found.geteMail() + ")");
        } else {
            lblFound.setText("No encontrado");
            found = null;
        }
    }

    private void save() {
        if (found == null) {
            JOptionPane.showMessageDialog(this, "Busque un usuario primero");
            return;
        }

        String newPass = new String(txtNewPass.getPassword()).trim();
        if (newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una nueva contraseña");
            return;
        }

        controller.resetPasswordByEmail(found.geteMail(), newPass);
        JOptionPane.showMessageDialog(this, "Contraseña actualizada correctamente para: " + found.getName());

        txtEmail.setText("");
        txtNewPass.setText("");
        lblFound.setText(" ");
        found = null;
    }
}
