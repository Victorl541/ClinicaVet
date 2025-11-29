package com.clinicavet.views;

import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.MedicalRecord;

import javax.swing.*;
import java.awt.*;

public class MedicalRecordFormDialog extends JDialog {
    
    private JComboBox<String> cbAppointment;
    private JTextField txtDate;
    private JTextField txtTime;
    private JTextField txtPet;
    private JTextField txtOwner;
    private JTextArea txtSintomas;
    private JTextArea txtDiagnostico;
    private JTextArea txtProcedimientos;
    private JTextArea txtTratamiento;
    private JTextArea txtOrdenes;
    private JTextArea txtObservaciones;
    private JButton btnSave;
    private JButton btnCancel;
    
    private boolean isEditMode;
    
    public MedicalRecordFormDialog(Frame parent, MedicalRecord recordToEdit) {
        super(parent, recordToEdit == null ? "Nueva Atención Médica" : "Editar Atención Médica", true);
        this.isEditMode = (recordToEdit != null);
        initComponents();
        setSize(700, 750);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Cita (si aplica)
        if (!isEditMode) {
            gbc.gridx = 0; gbc.gridy = row;
            mainPanel.add(new JLabel("Cita:"), gbc);
            gbc.gridx = 1;
            cbAppointment = new JComboBox<>();
            mainPanel.add(cbAppointment, gbc);
            row++;
        }
        
        // Fecha
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        txtDate = new JTextField(20);
        txtDate.setEditable(false);
        mainPanel.add(txtDate, gbc);
        row++;
        
        // Hora
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Hora:"), gbc);
        gbc.gridx = 1;
        txtTime = new JTextField(20);
        txtTime.setEditable(false);
        mainPanel.add(txtTime, gbc);
        row++;
        
        // Mascota
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Mascota:"), gbc);
        gbc.gridx = 1;
        txtPet = new JTextField(20);
        txtPet.setEditable(false);
        mainPanel.add(txtPet, gbc);
        row++;
        
        // Dueño
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Dueño:"), gbc);
        gbc.gridx = 1;
        txtOwner = new JTextField(20);
        txtOwner.setEditable(false);
        mainPanel.add(txtOwner, gbc);
        row++;
        
        // Síntomas
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Síntomas:"), gbc);
        gbc.gridx = 1; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        txtSintomas = new JTextArea(3, 20);
        txtSintomas.setLineWrap(true);
        txtSintomas.setWrapStyleWord(true);
        mainPanel.add(new JScrollPane(txtSintomas), gbc);
        row++;
        
        // Diagnóstico
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Diagnóstico:*"), gbc);
        gbc.gridx = 1; gbc.weighty = 1.0;
        txtDiagnostico = new JTextArea(3, 20);
        txtDiagnostico.setLineWrap(true);
        txtDiagnostico.setWrapStyleWord(true);
        mainPanel.add(new JScrollPane(txtDiagnostico), gbc);
        row++;
        
        // Procedimientos
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Procedimientos:"), gbc);
        gbc.gridx = 1; gbc.weighty = 1.0;
        txtProcedimientos = new JTextArea(3, 20);
        txtProcedimientos.setLineWrap(true);
        txtProcedimientos.setWrapStyleWord(true);
        mainPanel.add(new JScrollPane(txtProcedimientos), gbc);
        row++;
        
        // Tratamiento
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Tratamiento:"), gbc);
        gbc.gridx = 1; gbc.weighty = 1.0;
        txtTratamiento = new JTextArea(3, 20);
        txtTratamiento.setLineWrap(true);
        txtTratamiento.setWrapStyleWord(true);
        mainPanel.add(new JScrollPane(txtTratamiento), gbc);
        row++;
        
        // Órdenes
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Órdenes:"), gbc);
        gbc.gridx = 1; gbc.weighty = 1.0;
        txtOrdenes = new JTextArea(3, 20);
        txtOrdenes.setLineWrap(true);
        txtOrdenes.setWrapStyleWord(true);
        mainPanel.add(new JScrollPane(txtOrdenes), gbc);
        row++;
        
        // Observaciones
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Observaciones:"), gbc);
        gbc.gridx = 1; gbc.weighty = 1.0;
        txtObservaciones = new JTextArea(3, 20);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        mainPanel.add(new JScrollPane(txtObservaciones), gbc);
        row++;
        
        // Botones
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.weighty = 0; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new JButton("Guardar");
        btnCancel = new JButton("Cancelar");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        mainPanel.add(buttonPanel, gbc);
        
        btnCancel.addActionListener(e -> dispose());
        
        add(new JScrollPane(mainPanel));
    }
    
    // Getters
    public JComboBox<String> getCbAppointment() {
        return cbAppointment;
    }
    
    public JTextField getTxtDate() {
        return txtDate;
    }
    
    public JTextField getTxtTime() {
        return txtTime;
    }
    
    public JTextField getTxtPet() {
        return txtPet;
    }
    
    public JTextField getTxtOwner() {
        return txtOwner;
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
    
    public JTextArea getTxtObservaciones() {
        return txtObservaciones;
    }
    
    public JButton getBtnSave() {
        return btnSave;
    }
    
    public boolean isEditMode() {
        return isEditMode;
    }
}
