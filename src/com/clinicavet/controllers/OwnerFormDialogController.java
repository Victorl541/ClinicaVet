package com.clinicavet.controllers;

import com.clinicavet.model.entities.Owner;
import com.clinicavet.model.services.IOwnerService;
import com.clinicavet.views.OwnerFormDialog;

import javax.swing.*;

public class OwnerFormDialogController {

    private final OwnerFormDialog view;
    private final Owner ownerToEdit;
    private final IOwnerService ownerService;

    public OwnerFormDialogController(OwnerFormDialog view, Owner owner, IOwnerService ownerService) {
        this.view = view;
        this.ownerToEdit = owner;
        this.ownerService = ownerService;

        loadData();
        setupListeners();
    }

    private void loadData() {
        if (ownerToEdit != null) {
            view.getTxtCedula().setText(ownerToEdit.getId());
            view.getTxtCedula().setEnabled(false);  // No permitir cambiar cédula
            view.getTxtNombre().setText(ownerToEdit.getName());
            view.getTxtTelefono().setText(ownerToEdit.getPhone());
            view.getTxtEmail().setText(ownerToEdit.getEmail());
            view.getTxtDireccion().setText(ownerToEdit.getAddress());
            view.getChkActivo().setSelected(ownerToEdit.isActivo());
        } else {
            view.getChkActivo().setSelected(true);
        }
    }

    private void setupListeners() {
        view.getBtnSave().addActionListener(e -> saveOwner());
    }

    private void saveOwner() {
        String cedula = view.getTxtCedula().getText().trim();
        String nombre = view.getTxtNombre().getText().trim();
        String telefono = view.getTxtTelefono().getText().trim();
        String email = view.getTxtEmail().getText().trim();
        String direccion = view.getTxtDireccion().getText().trim();
        boolean activo = view.getChkActivo().isSelected();

        // Validaciones
        if (cedula.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || direccion.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(view, "Email inválido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (ownerToEdit == null) {
                // Crear nuevo dueño - pasar parámetros individuales
                ownerService.createOwner(cedula, nombre, telefono, direccion, email);
                JOptionPane.showMessageDialog(view, "Dueño creado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Editar dueño existente
                ownerToEdit.setName(nombre);
                ownerToEdit.setPhone(telefono);
                ownerToEdit.setEmail(email);
                ownerToEdit.setAddress(direccion);
                ownerToEdit.setActivo(activo);
                ownerService.updateOwner(ownerToEdit);
                JOptionPane.showMessageDialog(view, "Dueño actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            view.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}