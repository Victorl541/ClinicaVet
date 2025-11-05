package com.clinicavet.views.panels;

import com.clinicavet.controllers.MainController;
import com.clinicavet.model.entities.User;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class DisableUserPanelAdapter extends JPanel {
    private final MainController controller;
    private JTextField txtSearch;
    private JButton btnSearch, btnDisable;
    private JLabel lblResult;
    private User found;

    public DisableUserPanelAdapter(MainController controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // ---------- Encabezado ----------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblTitle = new JLabel("Desactivar Usuario", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(44, 62, 80));
        header.add(lblTitle, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // ---------- Contenido principal ----------
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30, 40, 30, 40),
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Campo de búsqueda
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Email o nombre:"), gbc);

        gbc.gridx = 1;
        txtSearch = new JTextField(20);
        form.add(txtSearch, gbc);

        gbc.gridx = 2;
        btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(52, 152, 219));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btnSearch.addActionListener(e -> search());
        form.add(btnSearch, gbc);

        // Resultado
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        lblResult = new JLabel(" ", SwingConstants.CENTER);
        lblResult.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblResult.setForeground(new Color(52, 73, 94));
        form.add(lblResult, gbc);

        // Botón de desactivar
        gbc.gridy = 2;
        btnDisable = new JButton("Desactivar");
        btnDisable.setEnabled(false);
        btnDisable.setBackground(new Color(231, 76, 60));
        btnDisable.setForeground(Color.WHITE);
        btnDisable.setFocusPainted(false);
        btnDisable.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDisable.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnDisable.addActionListener(e -> disableUser());
        form.add(btnDisable, gbc);

        // Centrar todo en el panel
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(245, 247, 250));
        center.add(form);

        add(center, BorderLayout.CENTER);
    }

    private void search() {
        String q = txtSearch.getText().trim();
        if (q.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese nombre o correo del usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<User> u = controller.findUserByEmailOrName(q);

        if (u.isPresent()) {
            found = u.get();
            String estado = found.isActivo() ? "Activo" : "Inactivo";
            lblResult.setText("<html><b>Encontrado:</b> " + found.getName() + " (" + found.geteMail() + ")<br>Rol: "
                    + (found.getRol() != null ? found.getRol().getName() : "-") + " - Estado: " + estado + "</html>");

            if (found.getRol() != null && found.getRol().getName().equalsIgnoreCase("admin")) {
                btnDisable.setEnabled(false);
                JOptionPane.showMessageDialog(this, "No se puede desactivar al usuario ADMIN", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                btnDisable.setEnabled(found.isActivo());
            }

        } else {
            lblResult.setText("No se encontró ningún usuario con ese nombre o correo");
            btnDisable.setEnabled(false);
        }
    }

    private void disableUser() {
        if (found == null) {
            JOptionPane.showMessageDialog(this, "Debe buscar un usuario primero", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (found.getRol() != null && found.getRol().getName().equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this, "No se puede desactivar al usuario ADMIN", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!found.isActivo()) {
            JOptionPane.showMessageDialog(this, "El usuario ya está inactivo", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        controller.deactivateUserById(found.getId());
        lblResult.setText("Usuario " + found.getName() + " desactivado correctamente");
        btnDisable.setEnabled(false);
        found = null;

        JOptionPane.showMessageDialog(this, "Usuario desactivado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
