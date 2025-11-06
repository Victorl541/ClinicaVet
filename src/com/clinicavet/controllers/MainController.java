package com.clinicavet.controllers;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.entities.Owner;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.Rol;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.model.services.IOwnerService;
import com.clinicavet.model.services.IPetService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.views.MainWindow;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MainController {

    private final User loggedUser;
    public final IUserService userService;
    public final IOwnerService ownerService;
    public final RolService rolService;
    public final IPetService petService;
    private MainWindow mainWindow;

    public MainController(User loggedUser, IUserService userService, IOwnerService ownerService, RolService rolService, IPetService petService) {
        this.loggedUser = loggedUser;
        this.userService = userService;
        this.ownerService = ownerService;
        this.rolService = rolService;
        this.petService = petService;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    // ==================== USUARIO ====================

    public void createUser(String name, String email, String password, String roleName) {
        if ("admin".equalsIgnoreCase(roleName)) {
            JOptionPane.showMessageDialog(null, "No se puede crear otro usuario con rol ADMIN", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Optional<Rol> rolOpt = rolService.getByName(roleName);
        if (rolOpt.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Rol no existe: " + roleName, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            userService.createUser(new User(name, email, password, rolOpt.get()));
            JOptionPane.showMessageDialog(null, "Usuario creado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            if (mainWindow != null && mainWindow.getListUsersPanel() != null) {
                try { mainWindow.getListUsersPanel().reload(); } catch (Exception ex) { /* no-op */ }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error creando usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Optional<User> findUserByEmailOrName(String query) {
        if (query == null) return Optional.empty();
        String q = query.trim().toLowerCase();
        return userService.listUsers().stream()
                .filter(u -> (u.geteMail() != null && u.geteMail().toLowerCase().equals(q))
                        || (u.getName() != null && u.getName().toLowerCase().equals(q)))
                .findFirst();
    }

    public void deactivateUserById(int id) {
        Optional<User> optUser = userService.listUsers().stream()
                .filter(u -> u.getId() == id)
                .findFirst();

        if (optUser.isPresent()) {
            User user = optUser.get();
            if (user.getRol() != null && "ADMIN".equalsIgnoreCase(user.getRol().getName())) {
                JOptionPane.showMessageDialog(null, "No se puede desactivar al usuario con rol ADMIN", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            user.setActivo(false);
            userService.update(user);
            JOptionPane.showMessageDialog(null, "Usuario desactivado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            if (mainWindow != null && mainWindow.getListUsersPanel() != null) {
                try { mainWindow.getListUsersPanel().reload(); } catch (Exception ex) { /* no-op */ }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activateUserById(int id) {
        Optional<User> optUser = userService.listUsers().stream()
                .filter(u -> u.getId() == id)
                .findFirst();

        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setActivo(true);
            userService.update(user);
            JOptionPane.showMessageDialog(null, "Usuario reactivado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            if (mainWindow != null && mainWindow.getListUsersPanel() != null) {
                try { mainWindow.getListUsersPanel().reload(); } catch (Exception ex) { /* no-op */ }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateUser(User user) {
        // proteger usuario ADMIN contra modificaciones (edición/rol/desactivar)
        Optional<User> origOpt = userService.listUsers().stream()
                .filter(u -> u.getId() == user.getId()).findFirst();
        if (origOpt.isPresent()) {
            User orig = origOpt.get();
            if (orig.getRol() != null && "ADMIN".equalsIgnoreCase(orig.getRol().getName())) {
                JOptionPane.showMessageDialog(null, "No se puede modificar al usuario con rol ADMIN", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        userService.update(user);
        JOptionPane.showMessageDialog(null, "Usuario actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        if (mainWindow != null && mainWindow.getListUsersPanel() != null) {
            try { mainWindow.getListUsersPanel().reload(); } catch (Exception ex) { /* no-op */ }
        }
    }

    public void resetPasswordByEmail(String email, String newPassword) {
        Optional<User> u = userService.getByEmail(email);
        if (u.isPresent()) {
            User user = u.get();
            user.setPassword(newPassword);
            userService.update(user);
            JOptionPane.showMessageDialog(null, "Contraseña restablecida", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            if (mainWindow != null && mainWindow.getListUsersPanel() != null) {
                try { mainWindow.getListUsersPanel().reload(); } catch (Exception ex) { /* no-op */ }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<User> listUsers() {
        return userService.listUsers();
    }

    public Optional<User> findUserByEmail(String email) {
        return userService.getByEmail(email);
    }

    // ==================== DUEÑO ====================

    public void createOwner(String id, String name, String phone, String address, String email) {
        try {
            if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("ID (cedula) obligatorio");
            ownerService.createOwner(id.trim(), name, phone, address, email);
            JOptionPane.showMessageDialog(null, "Dueño creado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            if (mainWindow != null && mainWindow.getListOwnersPanel() != null) {
                try { mainWindow.getListOwnersPanel().reload(); } catch (Exception ex) { /* no-op */ }
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Optional<Owner> findOwnerByQuery(String query) {
        if (query == null) return Optional.empty();
        String q = query.trim().toLowerCase();
        return ownerService.findAll().stream()
                .filter(owner -> {
                    String name = owner.getName() == null ? "" : owner.getName().toLowerCase();
                    String phone = owner.getPhone() == null ? "" : owner.getPhone().toLowerCase();
                    String email = owner.getEmail() == null ? "" : owner.getEmail().toLowerCase();
                    String id = owner.getId() == null ? "" : owner.getId().toLowerCase();
                    return name.contains(q) || phone.contains(q) || email.contains(q) || id.equalsIgnoreCase(q);
                })
                .findFirst();
    }

    public Optional<Owner> findOwnerById(String id) {
        return ownerService.findById(id);
    }

    public void updateOwner(Owner owner) {
        try {
            ownerService.updateOwner(owner);
            JOptionPane.showMessageDialog(null, "Dueño actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            if (mainWindow != null && mainWindow.getListOwnersPanel() != null) {
                try { mainWindow.getListOwnersPanel().reload(); } catch (Exception ex) { /* no-op */ }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error actualizando dueño: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deactivateOwnerById(String id) {
        try {
            ownerService.deactivateOwner(id);
            JOptionPane.showMessageDialog(null, "Dueño desactivado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            if (mainWindow != null && mainWindow.getListOwnersPanel() != null) {
                try { mainWindow.getListOwnersPanel().reload(); } catch (Exception ex) { /* no-op */ }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error desactivando dueño: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activateOwnerById(String id) {
        try {
            ownerService.activateOwner(id);
            JOptionPane.showMessageDialog(null, "Dueño reactivado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            if (mainWindow != null && mainWindow.getListOwnersPanel() != null) {
                try { mainWindow.getListOwnersPanel().reload(); } catch (Exception ex) { /* no-op */ }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error reactivando dueño: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteOwnerById(String id) {
        deactivateOwnerById(id);
    }

    public List<Owner> listAllOwners() {
        return ownerService.findAll();
    }

    // ==================== MASCOTA ====================

    public void createPet(int age, String name, String sex, String species, String breed, String allergies, String vaccines, String medicalNotes, double weight, Owner owner) {
        try {
            if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Nombre obligatorio");
            petService.createPet(new Pet(age, name, sex, species, breed, allergies, vaccines, medicalNotes, weight, owner));
            JOptionPane.showMessageDialog(null, "Mascota registrada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            if (mainWindow != null && mainWindow.getListPetsPanel() != null) {
                try { mainWindow.getListPetsPanel().reload(); } catch (Exception ex) { /* no-op */ }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Optional<Pet> findPetById(UUID id) {
        return petService.findById(id);
    }

    public Optional<Pet> findPetByQuery(String query) {
        if (query == null) return Optional.empty();
        String q = query.trim().toLowerCase();
        return petService.listPets().stream()
                .filter(p -> (p.getName() != null && p.getName().toLowerCase().contains(q))
                        || (p.getSpecies() != null && p.getSpecies().toLowerCase().contains(q))
                        || (p.getBreed() != null && p.getBreed().toLowerCase().contains(q)))
                .findFirst();
    }

    public void updatePet(Pet pet) {
        try {
            petService.updatePet(pet);
            JOptionPane.showMessageDialog(null, "Mascota actualizada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            if (mainWindow != null && mainWindow.getListPetsPanel() != null) {
                try { mainWindow.getListPetsPanel().reload(); } catch (Exception ex) { /* no-op */ }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deletePet(UUID id) {
        try {
            petService.deletePet(id);
            JOptionPane.showMessageDialog(null, "Mascota eliminada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            if (mainWindow != null && mainWindow.getListPetsPanel() != null) {
                try { mainWindow.getListPetsPanel().reload(); } catch (Exception ex) { /* no-op */ }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Pet> listAllPets() {
        return petService.listPets();
    }


    // ==================== UTILIDADES ====================

    public boolean hasRole(String roleName) {
        return loggedUser != null && loggedUser.getRol() != null &&
                loggedUser.getRol().getName().equalsIgnoreCase(roleName);
    }
}