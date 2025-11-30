package com.clinicavet.views;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PaymentsView extends JPanel {
    
    public JButton btnSearch;
    public JButton btnPayInvoice;
    public JTable tableInvoices;
    private DefaultTableModel tableModel;
    
    public PaymentsView() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
    }
    
    private void initComponents() {
        // === PANEL SUPERIOR: BOTONES ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        JLabel lblTitle = new JLabel("Registro de Pagos - Facturas Pendientes");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(lblTitle);
        
        topPanel.add(Box.createHorizontalStrut(30));
        
        btnSearch = new JButton("🔍 Buscar");
        btnSearch.setPreferredSize(new Dimension(120, 35));
        btnSearch.setBackground(new Color(52, 152, 219));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        topPanel.add(btnSearch);
        
        btnPayInvoice = new JButton("Registrar Pago");
        btnPayInvoice.setPreferredSize(new Dimension(150, 35));
        btnPayInvoice.setBackground(new Color(46, 204, 113));
        btnPayInvoice.setForeground(Color.WHITE);
        btnPayInvoice.setFocusPainted(false);
        topPanel.add(btnPayInvoice);
        
        add(topPanel, BorderLayout.NORTH);
        
        // === TABLA DE FACTURAS PENDIENTES ===
        String[] columns = {"Factura", "Cliente", "Fecha", "Total", "Pagado", "Saldo", "Estado"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableInvoices = new JTable(tableModel);
        tableInvoices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableInvoices.setRowHeight(26);
        tableInvoices.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableInvoices.setFont(new Font("Arial", Font.PLAIN, 11));
        tableInvoices.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableInvoices.setGridColor(new Color(230, 230, 230));
        tableInvoices.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(tableInvoices);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(scrollPane, BorderLayout.CENTER);
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
    
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    public int getSelectedRow() {
        return tableInvoices.getSelectedRow();
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}