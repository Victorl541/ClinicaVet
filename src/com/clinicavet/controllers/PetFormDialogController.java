package com.clinicavet.controllers;

import com.clinicavet.model.entities.Owner;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.services.IOwnerService;
import com.clinicavet.model.services.IPetService;
import com.clinicavet.views.PetFormDialog;

import javax.swing.*;
import java.util.List;

public class PetFormDialogController {

    private final PetFormDialog view;
    private final Pet petToEdit;
    private final IPetService petService;
    private final IOwnerService ownerService;

    public PetFormDialogController(PetFormDialog view, Pet pet, IPetService petService, IOwnerService ownerService) {
        this.view = view;
        this.petToEdit = pet;
        this.petService = petService;
        this.ownerService = ownerService;

        loadOwners();
        loadData();
        setupListeners();
    }

    private void loadOwners() {
        view.getCbDueno().removeAllItems();
        List<Owner> owners = ownerService.findAll().stream()
                .filter(Owner::isActivo)
                .toList();

        for (Owner o : owners) {
            view.getCbDueno().addItem(o.getName());
        }
    }

    private void loadData() {
        if (petToEdit != null) {
            view.getTxtNombre().setText(petToEdit.getName());
            view.getTxtEspecie().setText(petToEdit.getSpecies());
            view.getTxtRaza().setText(petToEdit.getBreed());
            view.getCbSexo().setSelectedItem(petToEdit.getSex());
            view.getSpnAge().setValue(petToEdit.getAge());
            view.getSpnWeight().setValue(petToEdit.getWeight());
            view.getCbDueno().setSelectedItem(petToEdit.getOwner().getName());
            view.getTxtAlergias().setText(petToEdit.getAllergies());
            view.getTxtVacunas().setText(petToEdit.getVaccines());
            view.getTxtNotasMedicas().setText(petToEdit.getMedicalNotes());
        }
    }

    private void setupListeners() {
        view.getBtnSave().addActionListener(e -> savePet());
    }

    private void savePet() {
        String nombre = view.getTxtNombre().getText().trim();
        String especie = view.getTxtEspecie().getText().trim();
        String raza = view.getTxtRaza().getText().trim();
        String sexo = (String) view.getCbSexo().getSelectedItem();
        int edad = (Integer) view.getSpnAge().getValue();
        double peso = (Double) view.getSpnWeight().getValue();
        String duenioNombre = (String) view.getCbDueno().getSelectedItem();
        String alergias = view.getTxtAlergias().getText().trim();
        String vacunas = view.getTxtVacunas().getText().trim();
        String notasMedicas = view.getTxtNotasMedicas().getText().trim();

        // Validaciones
        if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty() || sexo == null || duenioNombre == null) {
            JOptionPane.showMessageDialog(view, "Campos obligatorios vacíos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (edad < 0 || peso <= 0) {
            JOptionPane.showMessageDialog(view, "Edad y peso deben ser válidos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Owner dueno = ownerService.findAll().stream()
                    .filter(o -> o.getName().equals(duenioNombre))
                    .findFirst()
                    .orElse(null);

            if (dueno == null) {
                JOptionPane.showMessageDialog(view, "Dueño no válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (petToEdit == null) {
                // Crear nueva mascota
                Pet newPet = new Pet(edad, nombre, sexo, especie, raza, alergias, vacunas, notasMedicas, peso, dueno);
                petService.createPet(newPet);
                JOptionPane.showMessageDialog(view, "Mascota creada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Editar mascota existente
                petToEdit.setName(nombre);
                petToEdit.setSpecies(especie);
                petToEdit.setBreed(raza);
                petToEdit.setSex(sexo);
                petToEdit.setAge(edad);
                petToEdit.setWeight(peso);
                petToEdit.setOwner(dueno);
                petToEdit.setAllergies(alergias);
                petToEdit.setVaccines(vacunas);
                petToEdit.setMedicalNotes(notasMedicas);
                petService.updatePet(petToEdit);
                JOptionPane.showMessageDialog(view, "Mascota actualizada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            view.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}