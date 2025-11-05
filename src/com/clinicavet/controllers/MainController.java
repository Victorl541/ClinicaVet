package com.clinicavet.controllers;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.entities.Rol;
import com.clinicavet.model.services.UserService;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.views.MainWindow;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public class MainController {

    private final User loggedUser;
    public final IUserService userService;
    public final RolService rolService;
    private MainWindow mainWindow; 

    public MainController(User loggedUser, IUserService userService2, RolService rolService) {
        this.loggedUser = loggedUser;
        this.userService = userService2;
        this.rolService = rolService;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    
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
                mainWindow.getListUsersPanel().addAutoRefresh();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error creando usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Optional<User> findUserByEmailOrName(String query) {
        return userService.listUsers().stream()
                .filter(u -> u.geteMail().equalsIgnoreCase(query)
                        || u.getName().equalsIgnoreCase(query))
                .findFirst();
    }

    public void deactivateUserById(int id) {
        Optional<User> optUser = userService.listUsers().stream()
                .filter(u -> u.getId() == id)
                .findFirst();

        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setActivo(false);
            userService.update(user);
            JOptionPane.showMessageDialog(null, "Usuario desactivado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
        } else {
            JOptionPane.showMessageDialog(null, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    public void updateUser(User user) {
        userService.update(user);
        JOptionPane.showMessageDialog(null, "Usuario actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void resetPasswordByEmail(String email, String newPassword) {
        Optional<User> u = userService.getByEmail(email);
        if (u.isPresent()) {
            User user = u.get();
            user.setPassword(newPassword);
            userService.update(user);
            JOptionPane.showMessageDialog(null, "Contraseña restablecida", "Éxito", JOptionPane.INFORMATION_MESSAGE);
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

    public boolean hasRole(String roleName) {
        return loggedUser != null && loggedUser.getRol() != null &&
                loggedUser.getRol().getName().equalsIgnoreCase(roleName);
    }
}
