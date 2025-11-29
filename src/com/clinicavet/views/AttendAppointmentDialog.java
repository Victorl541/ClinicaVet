package com.clinicavet.views;

import com.clinicavet.model.entities.Appointment;

import javax.swing.*;
import java.awt.*;

/**
 * Diálogo para atender una cita y registrar la atención médica
 */
public class AttendAppointmentDialog extends JDialog {
    
    private JTextField txtDate;
    private JTextField txtTime;
    private JTextField txtPet;
    private JTextField txtOwner;
    private JTextArea txtSintomas;
    private JTextArea txtDiagnostico;
    private JTextArea txtProcedimientos;
    private JTextArea txtTratamiento;
    private JTextArea txtOrdenes;
    private JButton btnSave;
    private JButton btnCancel;
    
    private Appointment appointment;
    
    public AttendAppointmentDialog(Frame parent, Appointment appointment) {
        super(parent, "Atender Cita - " + appointment.getMascota().getName(), true);
        this.appointment = appointment;
        initComponents();
        loadAppointmentData();
        setSize(750, 700);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Título
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JLabel lblTitle = new JLabel("📋 REGISTRO DE ATENCIÓN MÉDICA", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        mainPanel.add(lblTitle, gbc);
        row++;
        
        // Separador
        gbc.gridy = row;
        mainPanel.add(new JSeparator(), gbc);
        row++;
        gbc.gridwidth = 1;
        
        // Fecha
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        txtDate = new JTextField(20);
        txtDate.setEditable(false);
        txtDate.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(txtDate, gbc);
        row++;
        
        // Hora
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Hora:"), gbc);
        gbc.gridx = 1;
        txtTime = new JTextField(20);
        txtTime.setEditable(false);
        txtTime.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(txtTime, gbc);
        row++;
        
        // Mascota
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Mascota:"), gbc);
        gbc.gridx = 1;
        txtPet = new JTextField(20);
        txtPet.setEditable(false);
        txtPet.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(txtPet, gbc);
        row++;
        
        // Dueño
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Dueño:"), gbc);
        gbc.gridx = 1;
        txtOwner = new JTextField(20);
        txtOwner.setEditable(false);
        txtOwner.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(txtOwner, gbc);
        row++;
        
        // Separador
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        mainPanel.add(new JSeparator(), gbc);
        row++;
        gbc.gridwidth = 1;
        
        // Síntomas
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("1️⃣ Síntomas:"), gbc);
        gbc.gridx = 1; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        txtSintomas = new JTextArea(4, 20);
        txtSintomas.setLineWrap(true);
        txtSintomas.setWrapStyleWord(true);
        txtSintomas.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(new JScrollPane(txtSintomas), gbc);
        row++;
        
        // Diagnóstico
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblDiag = new JLabel("2️⃣ Diagnóstico:*");
        lblDiag.setForeground(Color.RED);
        mainPanel.add(lblDiag, gbc);
        gbc.gridx = 1; gbc.weighty = 1.0;
        txtDiagnostico = new JTextArea(4, 20);
        txtDiagnostico.setLineWrap(true);
        txtDiagnostico.setWrapStyleWord(true);
        txtDiagnostico.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        mainPanel.add(new JScrollPane(txtDiagnostico), gbc);
        row++;
        
        // Procedimientos
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("3️⃣ Procedimientos:"), gbc);
        gbc.gridx = 1; gbc.weighty = 1.0;
        txtProcedimientos = new JTextArea(4, 20);
        txtProcedimientos.setLineWrap(true);
        txtProcedimientos.setWrapStyleWord(true);
        txtProcedimientos.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(new JScrollPane(txtProcedimientos), gbc);
        row++;
        
        // Tratamiento
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("4️⃣ Tratamiento (medicación):"), gbc);
        gbc.gridx = 1; gbc.weighty = 1.0;
        txtTratamiento = new JTextArea(4, 20);
        txtTratamiento.setLineWrap(true);
        txtTratamiento.setWrapStyleWord(true);
        txtTratamiento.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(new JScrollPane(txtTratamiento), gbc);
        row++;
        
        // Órdenes
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("5️⃣ Órdenes (exámenes/controles):"), gbc);
        gbc.gridx = 1; gbc.weighty = 1.0;
        txtOrdenes = new JTextArea(4, 20);
        txtOrdenes.setLineWrap(true);
        txtOrdenes.setWrapStyleWord(true);
        txtOrdenes.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(new JScrollPane(txtOrdenes), gbc);
        row++;
        
        // Botones
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.weighty = 0; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new JButton("💾 Guardar y Cerrar Cita");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnCancel = new JButton("❌ Cancelar");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        mainPanel.add(buttonPanel, gbc);
        
        btnCancel.addActionListener(e -> dispose());
        
        add(new JScrollPane(mainPanel));
    }
    
    private void loadAppointmentData() {
        txtDate.setText(appointment.getFecha().toString());
        txtTime.setText(appointment.getHora().toString());
        txtPet.setText(appointment.getMascota().getName() + " (" + 
                      appointment.getMascota().getSpecies() + " - " + 
                      appointment.getMascota().getBreed() + ")");
        txtOwner.setText(appointment.getMascota().getOwner().getName());
    }
    
    // Getters
    public Appointment getAppointment() {
        return appointment;
    }
    
    public JTextArea getTxtSintomas() {
        return txtSintomas;
    }
    
    public JTextArea getTxtDiagnostico() {
        return txtDiagnostico;
    }
    
    public JTextArea getTxtProcedimientos() {
        return txtProcedimientos;
    }
    
    public JTextArea getTxtTratamiento() {
        return txtTratamiento;
    }
    
    public JTextArea getTxtOrdenes() {
        return txtOrdenes;
    }
    
    public JButton getBtnSave() {
        return btnSave;
    }
}
