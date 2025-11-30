package com.clinicavet.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class PaymentsSearchDialog extends JDialog {
    
    public JTextField txtSearch;
    public JComboBox<String> cbStatus;
    public JSpinner spinDateFrom;
    public JSpinner spinDateTo;
    public JButton btnSearch;
    public JButton btnClose;
    public JTable tableResults;
    private DefaultTableModel tableModel;
    
    public PaymentsSearchDialog(JFrame parent) {
        super(parent, "Buscar Facturas Pendientes", true);
        
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(245, 247, 250));
        
        // === PANEL FILTROS ===
        mainPanel.add(createFilterPanel(), BorderLayout.NORTH);
        
        // === TABLA DE RESULTADOS ===
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        // === PANEL BOTONES ===
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Filtros de Búsqueda"));
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Fila 1: Cliente y Estado
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Cliente:"), gbc);
        
        txtSearch = new JTextField(20);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        panel.add(txtSearch, gbc);
        
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Estado:"), gbc);
        
        cbStatus = new JComboBox<>(new String[]{"TODOS", "PENDIENTE", "PAGADA"});
        cbStatus.setPreferredSize(new Dimension(120, 25));
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.2;
        panel.add(cbStatus, gbc);
        
        // Fila 2: Rango de fechas
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Desde:"), gbc);
        
        spinDateFrom = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorFrom = new JSpinner.DateEditor(spinDateFrom, "yyyy-MM-dd");
        spinDateFrom.setEditor(editorFrom);
        spinDateFrom.setPreferredSize(new Dimension(140, 25));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        panel.add(spinDateFrom, gbc);
        
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Hasta:"), gbc);
        
        spinDateTo = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorTo = new JSpinner.DateEditor(spinDateTo, "yyyy-MM-dd");
        spinDateTo.setEditor(editorTo);
        spinDateTo.setPreferredSize(new Dimension(140, 25));
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.2;
        panel.add(spinDateTo, gbc);
        
        // Botón búsqueda
        btnSearch = new JButton("🔍 Buscar");
        btnSearch.setPreferredSize(new Dimension(100, 28));
        btnSearch.setBackground(new Color(52, 152, 219));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(btnSearch, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columns = {"Factura", "Cliente", "Fecha", "Total", "Pagado", "Saldo", "Estado"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableResults = new JTable(tableModel);
        tableResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableResults.setRowHeight(26);
        tableResults.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableResults.setFont(new Font("Arial", Font.PLAIN, 11));
        tableResults.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableResults.setGridColor(new Color(230, 230, 230));
        tableResults.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tableResults);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        btnClose = new JButton("✕ Cerrar");
        btnClose.setPreferredSize(new Dimension(100, 35));
        btnClose.setBackground(new Color(189, 195, 199));
        btnClose.setForeground(Color.WHITE);
        btnClose.setFocusPainted(false);
        btnClose.addActionListener(e -> dispose());
        panel.add(btnClose);
        
        return panel;
    }
    
    public void setTableData(java.util.List<?> invoices) {
        tableModel.setRowCount(0);
        
        for (Object obj : invoices) {
            com.clinicavet.model.entities.Invoice invoice = (com.clinicavet.model.entities.Invoice) obj;
            tableModel.addRow(new Object[]{
                    invoice.getInvoiceNumber(),
                    invoice.getClient() != null ? invoice.getClient().getName() : "N/A",
                    invoice.getInvoiceDate(),
                    String.format("$%,.2f", invoice.getTotal()),
                    String.format("$%,.2f", invoice.getTotalPaid()),
                    String.format("$%,.2f", invoice.getRemaining()),
                    invoice.getStatus().toString()
            });
        }
    }
    
    public String getSearchText() {
        return txtSearch.getText().trim();
    }
    
    public String getStatusFilter() {
        Object selected = cbStatus.getSelectedItem();
        return selected != null ? selected.toString() : "TODOS";
    }
    
    public LocalDate getDateFrom() {
        java.util.Date date = (java.util.Date) spinDateFrom.getValue();
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }
    
    public LocalDate getDateTo() {
        java.util.Date date = (java.util.Date) spinDateTo.getValue();
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    }
    
    public void setDefaultDateRange(LocalDate from, LocalDate to) {
        spinDateFrom.setValue(java.sql.Date.valueOf(from));
        spinDateTo.setValue(java.sql.Date.valueOf(to));
    }
    
    public int getSelectedRow() {
        return tableResults.getSelectedRow();
    }
    
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}