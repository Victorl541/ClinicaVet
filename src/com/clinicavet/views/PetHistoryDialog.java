package com.clinicavet.views;

import com.clinicavet.model.entities.MedicalRecord;
import com.clinicavet.model.entities.Pet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Diálogo que muestra la historia clínica completa de una mascota
 */
public class PetHistoryDialog extends JDialog {
    
    private JTable table;
    private JTextArea txtDetails;
    
    public PetHistoryDialog(Frame parent, Pet pet, List<MedicalRecord> history) {
        super(parent, "Historia Clínica - " + pet.getName(), true);
        initComponents(pet, history);
        setSize(900, 600);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents(Pet pet, List<MedicalRecord> history) {
        setLayout(new BorderLayout(10, 10));
        
        // Panel superior con información de la mascota
        JPanel topPanel = new JPanel(new GridLayout(5, 2, 10, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("Información de la Mascota"));
        topPanel.add(new JLabel("Nombre:"));
        topPanel.add(new JLabel(pet.getName()));
        topPanel.add(new JLabel("Especie:"));
        topPanel.add(new JLabel(pet.getSpecies()));
        topPanel.add(new JLabel("Raza:"));
        topPanel.add(new JLabel(pet.getBreed()));
        topPanel.add(new JLabel("Dueño:"));
        topPanel.add(new JLabel(pet.getOwner().getName()));
        topPanel.add(new JLabel("Alergias:"));
        topPanel.add(new JLabel(pet.getAllergies() != null ? pet.getAllergies() : "Ninguna"));
        
        add(topPanel, BorderLayout.NORTH);
        
        // Panel central con tabla de atenciones
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Historial de Atenciones (" + history.size() + " registros)"));
        
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Fecha", "Hora", "Médico", "Diagnóstico"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (MedicalRecord mr : history) {
            model.addRow(new Object[]{
                    mr.getFecha(),
                    mr.getHora(),
                    mr.getMedico().getName(),
                    mr.getDiagnostico().length() > 40 ? 
                        mr.getDiagnostico().substring(0, 37) + "..." : mr.getDiagnostico()
            });
        }
        
        table.setModel(model);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Panel inferior con detalles de la atención seleccionada
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Detalle de Atención"));
        
        txtDetails = new JTextArea(10, 40);
        txtDetails.setEditable(false);
        txtDetails.setLineWrap(true);
        txtDetails.setWrapStyleWord(true);
        bottomPanel.add(new JScrollPane(txtDetails), BorderLayout.CENTER);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Listener para mostrar detalles al seleccionar una fila
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < history.size()) {
                    showRecordDetails(history.get(selectedRow));
                }
            }
        });
    }
    
    private void showRecordDetails(MedicalRecord record) {
        StringBuilder details = new StringBuilder();
        details.append("═══════════════════════════════════════════════════════\n");
        details.append("FECHA: ").append(record.getFecha()).append(" | HORA: ").append(record.getHora()).append("\n");
        details.append("MÉDICO: ").append(record.getMedico().getName()).append("\n");
        details.append("═══════════════════════════════════════════════════════\n\n");
        
        details.append("SÍNTOMAS:\n");
        details.append(record.getSintomas() != null ? record.getSintomas() : "No registrado").append("\n\n");
        
        details.append("DIAGNÓSTICO:\n");
        details.append(record.getDiagnostico()).append("\n\n");
        
        details.append("PROCEDIMIENTOS:\n");
        details.append(record.getProcedimientos() != null ? record.getProcedimientos() : "Ninguno").append("\n\n");
        
        details.append("TRATAMIENTO:\n");
        details.append(record.getTratamiento() != null ? record.getTratamiento() : "Ninguno").append("\n\n");
        
        details.append("ÓRDENES (Exámenes/Controles):\n");
        details.append(record.getOrdenes() != null ? record.getOrdenes() : "Ninguna").append("\n\n");
        
        details.append("OBSERVACIONES:\n");
        details.append(record.getObservaciones() != null ? record.getObservaciones() : "Ninguna").append("\n");
        
        txtDetails.setText(details.toString());
        txtDetails.setCaretPosition(0);
    }
}
