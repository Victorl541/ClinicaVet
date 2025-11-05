package com.clinicavet.views.panels;

import com.clinicavet.controllers.MainController;
import com.clinicavet.model.entities.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class ListUsersPanelAdapter extends JPanel {

    private final MainController controller;

    private final JTextField txtName = new JTextField(15);
    private final JTextField txtEmail = new JTextField(18);
    private final JPasswordField txtPassword = new JPasswordField(12);
    private final JComboBox<String> cmbRole = new JComboBox<>(new String[]{"MEDICO", "AUXILIAR"});
    private final JTextField txtSearch = new JTextField(18);

    public final JButton btnAdd = new JButton("Agregar");
    public final JButton btnEdit = new JButton("Editar");
    public final JButton btnDeactivate = new JButton("Desactivar");
    public final JButton btnResetPassword = new JButton("Restablecer contraseña");
    public final JButton btnRefresh = new JButton("Refrescar");
    public final JButton btnClear = new JButton("Limpiar");
    public final JButton btnSearch = new JButton("Buscar");

    public JTable table;
    public DefaultTableModel tableModel;

    private Integer selectedId = null;
    private boolean selectedIsAdmin = false;

    public ListUsersPanelAdapter(MainController controller) {
        this.controller = controller;
        initUI();
        load();
        initListeners();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // formulario superior
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        form.setBackground(Color.WHITE);
        form.add(new JLabel("Nombre:"));
        form.add(txtName);
        form.add(new JLabel("Email:"));
        form.add(txtEmail);
        form.add(new JLabel("Password:"));
        form.add(txtPassword);
        form.add(new JLabel("Rol:"));
        form.add(cmbRole);

        form.add(btnAdd);
        form.add(btnEdit);
        form.add(btnDeactivate);
        form.add(btnResetPassword);
        form.add(btnClear);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(Color.WHITE);
        topContainer.add(form, BorderLayout.CENTER);
        topContainer.setPreferredSize(new Dimension(0, 140));
        add(topContainer, BorderLayout.NORTH);

        // tabla
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Email", "Rol", "Activo"}, 0) {
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
        scroll.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(scroll, BorderLayout.CENTER);

        // búsqueda abajo
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("Buscar (email / nombre / id):"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        add(searchPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    public void load() {
        tableModel.setRowCount(0);
        List<User> users = controller.listUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{
                    u.getId(),
                    u.getName(),
                    (u.geteMail() != null ? u.geteMail() : ""),
                    (u.getRol() != null ? u.getRol().getName() : ""),
                    u.isActivo() ? "Sí" : "No"
            });
        }
    }

    public void reload() { load(); }

    private void setControlsForSelection(boolean isAdmin) {
        // si es admin no permitir editar, desactivar ni cambiar rol
        btnEdit.setEnabled(!isAdmin);
        btnDeactivate.setEnabled(!isAdmin);
        cmbRole.setEnabled(!isAdmin);
        selectedIsAdmin = isAdmin;
    }

    private void initListeners() {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        try {
                            selectedId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                        } catch (Exception ex) {
                            selectedId = null;
                        }
                        if (selectedId != null) {
                            Optional<User> opt = controller.listUsers().stream()
                                    .filter(u -> u.getId() == selectedId).findFirst();
                            if (opt.isPresent()) {
                                User u = opt.get();
                                txtName.setText(u.getName() != null ? u.getName() : "");
                                txtEmail.setText(u.geteMail() != null ? u.geteMail() : "");
                                txtPassword.setText("");
                                String roleName = u.getRol() != null && u.getRol().getName() != null ? u.getRol().getName() : "MEDICO";
                                cmbRole.setSelectedItem(roleName);
                                boolean isAdmin = "ADMIN".equalsIgnoreCase(roleName);
                                setControlsForSelection(isAdmin);
                            } else {
                                selectedId = null;
                                setControlsForSelection(false);
                            }
                        } else {
                            setControlsForSelection(false);
                        }
                    } else {
                        selectedId = null;
                        setControlsForSelection(false);
                    }
                }
            }
        });

        btnAdd.addActionListener(e -> {
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String role = (String) cmbRole.getSelectedItem();
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre, email y password son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if ("ADMIN".equalsIgnoreCase(role)) {
                JOptionPane.showMessageDialog(this, "No se puede crear usuario con rol ADMIN desde aquí", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            controller.createUser(name, email, password, role);
            reload();
            clearFields();
        });

        btnEdit.addActionListener(e -> {
            if (selectedId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario para editar", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (selectedIsAdmin) {
                JOptionPane.showMessageDialog(this, "No se puede editar al usuario ADMIN", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Optional<User> opt = controller.listUsers().stream().filter(u -> u.getId() == selectedId).findFirst();
            if (opt.isPresent()) {
                User u = opt.get();
                u.setName(txtName.getText().trim());
                u.seteMail(txtEmail.getText().trim());
                String pass = new String(txtPassword.getPassword()).trim();
                if (!pass.isEmpty()) u.setPassword(pass);
                String roleName = (String) cmbRole.getSelectedItem();
                try { if (u.getRol() != null) u.getRol().setName(roleName); } catch (Exception ignored) {}
                controller.updateUser(u);
                reload();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDeactivate.addActionListener(e -> {
            if (selectedId == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un usuario para desactivar", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (selectedIsAdmin) {
                JOptionPane.showMessageDialog(this, "No se puede desactivar al usuario ADMIN", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "¿Desactivar usuario seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deactivateUserById(selectedId);
                reload();
                clearFields();
            }
        });

        btnResetPassword.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Seleccione/Ingrese email del usuario", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String newPass = JOptionPane.showInputDialog(this, "Nueva contraseña para " + email + ":");
            if (newPass != null && !newPass.trim().isEmpty()) {
                controller.resetPasswordByEmail(email, newPass.trim());
                JOptionPane.showMessageDialog(this, "Contraseña restablecida", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }
        });

        btnSearch.addActionListener(e -> {
            String q = txtSearch.getText().trim();
            if (q.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese texto para buscar", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Optional<User> opt = controller.findUserByEmailOrName(q);
            if (opt.isPresent()) {
                User u = opt.get();
                for (int r = 0; r < tableModel.getRowCount(); r++) {
                    if (tableModel.getValueAt(r, 0).toString().equals(String.valueOf(u.getId()))) {
                        table.setRowSelectionInterval(r, r);
                        Rectangle rect = table.getCellRect(r, 0, true);
                        table.scrollRectToVisible(rect);
                        return;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnRefresh.addActionListener(e -> { reload(); clearFields(); });
        btnClear.addActionListener(e -> clearFields());
    }

    private void clearFields() {
        txtName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
        txtSearch.setText("");
        table.clearSelection();
        selectedId = null;
        selectedIsAdmin = false;
        setControlsForSelection(false);
    }
}