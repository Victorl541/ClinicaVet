package com.clinicavet.controllers;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.views.UserFormDialog;
import com.clinicavet.views.UsersListView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.util.stream.Collectors;

public class UsersListController {

    private final UsersListView view;
    private final IUserService userService;
    private final RolService rolService;

    public UsersListController(UsersListView view,
                               IUserService userService,
                               RolService rolService) {

        this.view = view;
        this.userService = userService;
        this.rolService = rolService;

        // Cargar tabla inicial
        loadUsers("");

        // Listener de búsqueda EN TIEMPO REAL
        view.txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchUsers();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchUsers();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchUsers();
            }
        });

        // Listeners en la vista
        view.btnSearch.addActionListener(e -> searchUsers());
        view.btnNew.addActionListener(e -> openNewUserForm());
        view.btnEdit.addActionListener(e -> openEditUserForm());
    }

    public void loadUsers(String filter) {
        List<User> users;

        if (filter == null || filter.trim().isEmpty()) {
            // Cargar todos los usuarios excepto ADMIN
            users = userService.listUsers().stream()
                    .filter(u -> u.getRol() != null && !u.getRol().getName().equals("ADMIN"))
                    .collect(Collectors.toList());
        } else {
            String q = filter.trim().toLowerCase();
            users = userService.listUsers().stream()
                    .filter(u -> u.getRol() != null && !u.getRol().getName().equals("ADMIN"))
                    .filter(u -> u.getName().toLowerCase().contains(q)
                            || u.getEmail().toLowerCase().contains(q)
                            || u.getRol().getName().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }

        view.setTableData(users);
    }

    private void searchUsers() {
        String filter = view.getSearchText();
        loadUsers(filter);
    }

    private void openNewUserForm() {
        UserFormDialog dialog = new UserFormDialog(null, userService, rolService);
        dialog.setVisible(true);

        // Recargar tabla después de cerrar el diálogo
        loadUsers("");
        view.clearSearch();
    }

    private void openEditUserForm() {
        int selectedRow = view.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Seleccione un usuario para editar", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        long userId = view.getUserIdAt(selectedRow);
        User user = userService.getById((int) userId).orElse(null);

        if (user == null) {
            JOptionPane.showMessageDialog(view, "No se encontró el usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserFormDialog dialog = new UserFormDialog(user, userService, rolService);
        dialog.setVisible(true);

        // Recargar tabla después de editar
        loadUsers("");
        view.clearSearch();
    }
}