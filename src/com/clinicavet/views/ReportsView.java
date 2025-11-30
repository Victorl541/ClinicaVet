package com.clinicavet.views;

import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Vista principal de reportes con pestañas para cada tipo de reporte.
 */
public class ReportsView extends JPanel {
    
    private JTabbedPane tabbedPane;
    
    // Reporte 1: Citas por médico y estado
    private JTable tableDoctorStatus;
    private DefaultTableModel modelDoctorStatus;
    
    // Reporte 2: Top motivos de consulta
    private JTable tableTopReasons;
    private DefaultTableModel modelTopReasons;
    private JSpinner spinnerTopLimit;
    
    // Reporte 3: Ingresos por período
    private JTable tableIncome;
    private DefaultTableModel modelIncome;
    private JTextField dateFrom;
    private JTextField dateTo;
    private JLabel lblTotalIncome;
    private JLabel lblTotalAppointments;
    private JButton btnFilterByPeriod;
    
    public ReportsView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Título
        JLabel lblTitle = new JLabel("Reportes Estadísticos");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);
        
        // Pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        tabbedPane.addTab("Citas por Médico", createDoctorStatusPanel());
        tabbedPane.addTab("Top Motivos", createTopReasonsPanel());
        tabbedPane.addTab("Ingresos", createIncomePanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createDoctorStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Título
        JLabel lblTitle = new JLabel("Citas Agrupadas por Médico y Estado");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(lblTitle, BorderLayout.NORTH);
        
        // Tabla
        String[] columns = {"Médico", "PENDIENTE", "CONFIRMADA", "CANCELADA", "ATENDIDA", "NO_ASISTIO", "REPROGRAMAR", "TOTAL"};
        modelDoctorStatus = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDoctorStatus = new JTable(modelDoctorStatus);
        tableDoctorStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableDoctorStatus.setRowHeight(25);
        tableDoctorStatus.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JScrollPane scrollPane = new JScrollPane(tableDoctorStatus);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTopReasonsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel superior con filtros
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Motivos de Consulta Más Frecuentes");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.add(new JLabel("Mostrar top:"));
        spinnerTopLimit = new JSpinner(new SpinnerNumberModel(10, 5, 50, 5));
        spinnerTopLimit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        ((JSpinner.DefaultEditor) spinnerTopLimit.getEditor()).getTextField().setEditable(false);
        filterPanel.add(spinnerTopLimit);
        
        headerPanel.add(filterPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Tabla
        String[] columns = {"#", "Motivo de Consulta", "Especie", "Cantidad"};
        modelTopReasons = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableTopReasons = new JTable(modelTopReasons);
        tableTopReasons.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableTopReasons.setRowHeight(25);
        tableTopReasons.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // Ancho de columnas
        tableTopReasons.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableTopReasons.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableTopReasons.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableTopReasons.getColumnModel().getColumn(3).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(tableTopReasons);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createIncomePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel superior con filtros
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Ingresos - Todos los Períodos");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.add(new JLabel("Desde:"));
        dateFrom = new JTextField(LocalDate.now().minusMonths(1).toString());
        dateFrom.setPreferredSize(new Dimension(130, 25));
        filterPanel.add(dateFrom);
        
        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(new JLabel("Hasta:"));
        dateTo = new JTextField(LocalDate.now().toString());
        dateTo.setPreferredSize(new Dimension(130, 25));
        filterPanel.add(dateTo);
        
        filterPanel.add(Box.createHorizontalStrut(15));
        btnFilterByPeriod = new JButton("Filtrar por Período");
        btnFilterByPeriod.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFilterByPeriod.setBackground(new Color(52, 152, 219));
        btnFilterByPeriod.setForeground(Color.WHITE);
        btnFilterByPeriod.setFocusPainted(false);
        btnFilterByPeriod.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterPanel.add(btnFilterByPeriod);
        
        headerPanel.add(filterPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel de resumen
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summaryPanel.setBackground(new Color(240, 248, 255));
        summaryPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        
        lblTotalAppointments = new JLabel("Total Pagos: 0");
        lblTotalAppointments.setFont(new Font("Segoe UI", Font.BOLD, 14));
        summaryPanel.add(lblTotalAppointments);
        
        summaryPanel.add(new JSeparator(SwingConstants.VERTICAL));
        
        lblTotalIncome = new JLabel("Ingresos Totales: $0.00");
        lblTotalIncome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalIncome.setForeground(new Color(0, 128, 0));
        summaryPanel.add(lblTotalIncome);
        
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        // Tabla
        String[] columns = {"Cliente", "Factura", "Método Pago", "Fecha", "Monto"};
        modelIncome = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableIncome = new JTable(modelIncome);
        tableIncome.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableIncome.setRowHeight(25);
        tableIncome.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JScrollPane scrollPane = new JScrollPane(tableIncome);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Getters para el controlador
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
    
    public JSpinner getSpinnerTopLimit() {
        return spinnerTopLimit;
    }
    
    public JTextField getDateFrom() {
        return dateFrom;
    }
    
    public JTextField getDateTo() {
        return dateTo;
    }
    
    public DefaultTableModel getModelDoctorStatus() {
        return modelDoctorStatus;
    }
    
    public DefaultTableModel getModelTopReasons() {
        return modelTopReasons;
    }
    
    public DefaultTableModel getModelIncome() {
        return modelIncome;
    }
    
    public JLabel getLblTotalIncome() {
        return lblTotalIncome;
    }
    
    public JLabel getLblTotalAppointments() {
        return lblTotalAppointments;
    }
    
    public JButton getBtnFilterByPeriod() {
        return btnFilterByPeriod;
    }
}
