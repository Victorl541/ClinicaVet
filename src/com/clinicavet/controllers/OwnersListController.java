package com.clinicavet.controllers;

import com.clinicavet.model.entities.Owner;
import com.clinicavet.model.services.IOwnerService;
import com.clinicavet.views.OwnerFormDialog;
import com.clinicavet.views.OwnersListView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.util.stream.Collectors;

public class OwnersListController {

    private final OwnersListView view;
    private final IOwnerService ownerService;

    public OwnersListController(OwnersListView view, IOwnerService ownerService) {
        this.view = view;
        this.ownerService = ownerService;

        // Cargar tabla inicial
        loadOwners("");

        // Listener de búsqueda EN TIEMPO REAL
        view.txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchOwners();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchOwners();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchOwners();
            }
        });

        // Listeners en la vista
        view.btnSearch.addActionListener(e -> searchOwners());
        view.btnNew.addActionListener(e -> openNewOwnerForm());
        view.btnEdit.addActionListener(e -> openEditOwnerForm());
    }

    public void loadOwners(String filter) {
        List<Owner> owners;

        if (filter == null || filter.trim().isEmpty()) {
            owners = ownerService.findAll();
        } else {
            String q = filter.trim().toLowerCase();
            owners = ownerService.findAll().stream()
                    .filter(o -> o.getId().toLowerCase().contains(q)
                            || o.getName().toLowerCase().contains(q)
                            || o.getEmail().toLowerCase().contains(q)
                            || o.getPhone().toLowerCase().contains(q)
                            || o.getAddress().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }

        view.setTableData(owners);
    }

    private void searchOwners() {
        String filter = view.getSearchText();
        loadOwners(filter);
    }

    private void openNewOwnerForm() {
        OwnerFormDialog dialog = new OwnerFormDialog(null, ownerService);
        dialog.setVisible(true);

        // Recargar tabla después de cerrar el diálogo
        loadOwners("");
        view.clearSearch();
    }

    private void openEditOwnerForm() {
        int selectedRow = view.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Seleccione un dueño para editar", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String ownerId = view.getOwnerIdAt(selectedRow);
        Owner owner = ownerService.findById(ownerId).orElse(null);

        if (owner == null) {
            JOptionPane.showMessageDialog(view, "No se encontró el dueño", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        OwnerFormDialog dialog = new OwnerFormDialog(owner, ownerService);
        dialog.setVisible(true);

        // Recargar tabla después de editar
        loadOwners("");
        view.clearSearch();
    }

    private void deleteOwner() {
        int selectedRow = view.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Seleccione un dueño para eliminar", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String ownerId = view.getOwnerIdAt(selectedRow);
        Owner owner = ownerService.findById(ownerId).orElse(null);

        if (owner == null) {
            JOptionPane.showMessageDialog(view, "No se encontró el dueño", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view, 
                "¿Desactivar dueño " + owner.getName() + "?", 
                "Confirmar", 
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            owner.setActivo(false);
            ownerService.updateOwner(owner);
            loadOwners("");
            JOptionPane.showMessageDialog(view, "Dueño desactivado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}