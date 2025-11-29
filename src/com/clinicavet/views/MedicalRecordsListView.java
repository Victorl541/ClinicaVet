package com.clinicavet.views;

import com.clinicavet.model.entities.Appointment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Vista de Atención Médica: muestra las citas del médico para atender
 */
public class MedicalRecordsListView extends JPanel {
    
    public JTable tableCitas;
    public JTextField txtSearch;
    public JButton btnSearch;
    public JButton btnAttend;
    public JButton btnViewHistory;
    public JButton btnNoAttended;
    public JButton btnReschedule;
    
    public MedicalRecordsListView() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);
        
        // --- TÍTULO ---
        JLabel lblTitle = new JLabel("🩺 Atención Médica - Mis Citas", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);
        
        // --- PANEL CENTRAL ---
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        
        // Búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(new JLabel("🔍 Buscar:"));
        txtSearch = new JTextField(25);
        searchPanel.add(txtSearch);
        btnSearch = new JButton("Buscar");
        searchPanel.add(btnSearch);
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Tabla de citas
        tableCitas = new JTable();
        tableCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCitas.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(tableCitas);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // --- PANEL INFERIOR: BOTONES ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(Color.WHITE);
        
        btnAttend = new JButton("✅ Atender Cita");
        btnAttend.setBackground(new Color(46, 204, 113));
        btnAttend.setForeground(Color.WHITE);
        btnAttend.setFocusPainted(false);
        
        btnNoAttended = new JButton("❌ No Asistió");
        btnNoAttended.setBackground(new Color(231, 76, 60));
        btnNoAttended.setForeground(Color.WHITE);
        btnNoAttended.setFocusPainted(false);
        
        btnReschedule = new JButton("🔄 Reprogramar");
        btnReschedule.setBackground(new Color(241, 196, 15));
        btnReschedule.setForeground(Color.WHITE);
        btnReschedule.setFocusPainted(false);
        
        btnViewHistory = new JButton("📋 Ver Historia Clínica");
        btnViewHistory.setBackground(new Color(52, 152, 219));
        btnViewHistory.setForeground(Color.WHITE);
        btnViewHistory.setFocusPainted(false);
        
        bottomPanel.add(btnAttend);
        bottomPanel.add(btnNoAttended);
        bottomPanel.add(btnReschedule);
        bottomPanel.add(btnViewHistory);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void setTableData(List<Appointment> appointments) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Fecha", "Hora", "Mascota", "Dueño", "Motivo", "Estado"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        for (Appointment a : appointments) {
            model.addRow(new Object[]{
                    a.getFecha(),
                    a.getHora(),
                    a.getMascota().getName(),
                    a.getMascota().getOwner().getName(),
                    a.getMotivo(),
                    a.getEstado()
            });
        }
        
        tableCitas.setModel(model);
    }
    
    public String getSearchText() {
        return txtSearch.getText().trim();
    }
    
    public int getSelectedRow() {
        return tableCitas.getSelectedRow();
    }
    
    public void clearSearch() {
        txtSearch.setText("");
    }
}
