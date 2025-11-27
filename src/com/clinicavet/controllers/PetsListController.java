package com.clinicavet.controllers;

import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.services.IOwnerService;
import com.clinicavet.model.services.IPetService;
import com.clinicavet.views.PetFormDialog;
import com.clinicavet.views.PetsListView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import java.util.stream.Collectors;

public class PetsListController {

    private final PetsListView view;
    private final IPetService petService;
    private final IOwnerService ownerService;

    public PetsListController(PetsListView view, IPetService petService, IOwnerService ownerService) {
        this.view = view;
        this.petService = petService;
        this.ownerService = ownerService;

        // Cargar tabla inicial
        loadPets("");

        // Listener de búsqueda EN TIEMPO REAL
        view.txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchPets();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchPets();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchPets();
            }
        });

        // Listeners en la vista
        view.btnSearch.addActionListener(e -> searchPets());
        view.btnNew.addActionListener(e -> openNewPetForm());
        view.btnEdit.addActionListener(e -> openEditPetForm());
    }

    public void loadPets(String filter) {
        List<Pet> pets;

        if (filter == null || filter.trim().isEmpty()) {
            pets = petService.listPets();
        } else {
            String q = filter.trim().toLowerCase();
            pets = petService.listPets().stream()
                    .filter(p -> p.getName().toLowerCase().contains(q)
                            || p.getSpecies().toLowerCase().contains(q)
                            || p.getBreed().toLowerCase().contains(q)
                            || p.getOwner().getName().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }

        view.setTableData(pets);
    }

    private void searchPets() {
        String filter = view.getSearchText();
        loadPets(filter);
    }

    private void openNewPetForm() {
        PetFormDialog dialog = new PetFormDialog(null, petService, ownerService);
        dialog.setVisible(true);

        // Recargar tabla después de cerrar el diálogo
        loadPets("");
        view.clearSearch();
    }

    private void openEditPetForm() {
        int selectedRow = view.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Seleccione una mascota para editar", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String petIdStr = view.getPetIdAt(selectedRow);
        // El ID mostrado es truncado, necesitamos encontrar la mascota por otros atributos
        // O podemos ajustar la tabla para almacenar el ID completo

        List<Pet> allPets = petService.listPets();
        if (selectedRow >= 0 && selectedRow < allPets.size()) {
            Pet pet = allPets.get(selectedRow);

            PetFormDialog dialog = new PetFormDialog(pet, petService, ownerService);
            dialog.setVisible(true);

            // Recargar tabla después de editar
            loadPets("");
            view.clearSearch();
        }
    }
}