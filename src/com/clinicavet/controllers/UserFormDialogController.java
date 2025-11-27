package com.clinicavet.controllers;

import com.clinicavet.model.entities.Rol;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.views.UserFormDialog;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public class UserFormDialogController {

    private final UserFormDialog view;
    private final User userToEdit;
    private final IUserService userService;
    private final RolService rolService;

    public UserFormDialogController(UserFormDialog view, User user, IUserService userService, RolService rolService) {
        this.view = view;
        this.userToEdit = user;
        this.userService = userService;
        this.rolService = rolService;

        loadRoles();
        loadData();
        setupListeners();
    }

    private void loadRoles() {
        view.getCbRol().removeAllItems();
        List<Rol> roles = rolService.listRoles();
        
        // Si estamos editando, mostrar solo el rol alternativo
        if (userToEdit != null && userToEdit.getRol() != null) {
            String currentRolName = userToEdit.getRol().getName();
            
            // Mostrar los roles que NO sean el actual ni ADMIN
            for (Rol r : roles) {
                if (!r.getName().equals("ADMIN") && !r.getName().equals(currentRolName)) {
                    view.getCbRol().addItem(r.getName());
                }
            }
            
            // Añadir el rol actual al final para mantenerlo seleccionado
            if (!currentRolName.equals("ADMIN")) {
                view.getCbRol().addItem(currentRolName);
            }
        } else {
            // Si es nuevo usuario, mostrar MEDICO y AUXILIAR
            for (Rol r : roles) {
                if (!r.getName().equals("ADMIN")) {
                    view.getCbRol().addItem(r.getName());
                }
            }
        }
    }

    private void loadData() {
        if (userToEdit != null) {
            view.getTxtNombre().setText(userToEdit.getName());
            view.getTxtEmail().setText(userToEdit.getEmail());
            view.getTxtPassword().setText(userToEdit.getPassword());
            
            if (userToEdit.getRol() != null && !userToEdit.getRol().getName().equals("ADMIN")) {
                view.getCbRol().setSelectedItem(userToEdit.getRol().getName());
            }
            
            view.getChkActivo().setSelected(userToEdit.isActivo());
        } else {
            view.getChkActivo().setSelected(true);
        }
    }

    private void setupListeners() {
        view.getBtnSave().addActionListener(e -> saveUser());
    }

    private void saveUser() {
        String nombre = view.getTxtNombre().getText().trim();
        String email = view.getTxtEmail().getText().trim();
        String password = new String(view.getTxtPassword().getPassword()).trim();
        String rolName = (String) view.getCbRol().getSelectedItem();
        boolean activo = view.getChkActivo().isSelected();

        // Validaciones
        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty() || rolName == null) {
            JOptionPane.showMessageDialog(view, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(view, "Email inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Obtener el Rol (filtrado: solo MEDICO o AUXILIAR)
            Rol rol = rolService.listRoles().stream()
                    .filter(r -> r.getName().equals(rolName) && !r.getName().equals("ADMIN"))
                    .findFirst()
                    .orElse(null);

            if (rol == null) {
                JOptionPane.showMessageDialog(view, "Rol no válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userToEdit == null) {
                // Crear nuevo usuario
                User newUser = new User(nombre, email, password, rol);
                newUser.setActivo(activo);
                userService.createUser(newUser);
                JOptionPane.showMessageDialog(view, "Usuario creado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Editar usuario existente
                userToEdit.setName(nombre);
                userToEdit.setEmail(email);
                userToEdit.setPassword(password);
                userToEdit.setRol(Optional.of(rol));
                userToEdit.setActivo(activo);
                userService.update(userToEdit);
                JOptionPane.showMessageDialog(view, "Usuario actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            view.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}