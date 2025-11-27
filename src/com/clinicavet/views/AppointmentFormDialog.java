package com.clinicavet.views;

import com.clinicavet.controllers.AppointmentFormDialogController;
import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.services.IAppointmentService;
import com.clinicavet.model.services.IPetService;
import com.clinicavet.model.services.IUserService;

import javax.swing.*;
import java.awt.*;

public class AppointmentFormDialog extends JDialog {

    private JComboBox<String> cbPet;
    private JComboBox<String> cbVet;
    private JTextField txtDate;
    private JTextField txtTime;
    private JTextArea txtReason;
    private JComboBox<String> cbStatus;
    private JButton btnSave;
    private JButton btnCancel;

    private AppointmentFormDialogController controller;

    public AppointmentFormDialog(Appointment appointment, IAppointmentService appointmentService, 
                                  IPetService petService, IUserService userService) {
        setTitle(appointment == null ? "Nueva Cita" : "Editar Cita");
        setModal(true);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();

        this.controller = new AppointmentFormDialogController(this, appointment, appointmentService, petService, userService);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Fila 1: Mascota
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Mascota:"), gbc);
        gbc.gridx = 1;
        cbPet = new JComboBox<>();
        mainPanel.add(cbPet, gbc);

        // Fila 2: Médico Veterinario
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Médico Veterinario:"), gbc);
        gbc.gridx = 1;
        cbVet = new JComboBox<>();
        mainPanel.add(cbVet, gbc);

        // Fila 3: Fecha
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Fecha (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        txtDate = new JTextField(20);
        mainPanel.add(txtDate, gbc);

        // Fila 4: Hora
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Hora (HH:mm):"), gbc);
        gbc.gridx = 1;
        txtTime = new JTextField(20);
        mainPanel.add(txtTime, gbc);

        // Fila 5: Motivo
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Motivo:"), gbc);
        gbc.gridx = 1;
        gbc.weighty = 0.3;
        txtReason = new JTextArea(3, 20);
        txtReason.setLineWrap(true);
        txtReason.setWrapStyleWord(true);
        mainPanel.add(new JScrollPane(txtReason), gbc);
        gbc.weighty = 0;

        // Fila 6: Estado
        gbc.gridx = 0; gbc.gridy = 5;
        mainPanel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        cbStatus = new JComboBox<>(new String[]{"Pendiente", "Confirmada", "Cancelada", "Completada"});
        mainPanel.add(cbStatus, gbc);

        // Fila 7: Botones
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);

        btnSave = new JButton("Guardar");
        btnCancel = new JButton("Cancelar");
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        mainPanel.add(btnPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane);
    }

    // Getters para el controlador
    public JComboBox<String> getCbPet() {
        return cbPet;
    }

    public JComboBox<String> getCbVet() {
        return cbVet;
    }

    public JTextField getTxtDate() {
        return txtDate;
    }

    public JTextField getTxtTime() {
        return txtTime;
    }

    public JTextArea getTxtReason() {
        return txtReason;
    }

    public JComboBox<String> getCbStatus() {
        return cbStatus;
    }

    public JButton getBtnSave() {
        return btnSave;
    }
}