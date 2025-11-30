package com.clinicavet.views;

import java.awt.*;
import javax.swing.*;

public class PaymentRegistrationDialog extends JDialog {
    
    private JLabel lblInvoiceNumber;
    private JLabel lblClientName;
    private JLabel lblRemaining;
    
    private JTextField txtAmount;
    private JComboBox<String> cmbPaymentMethod;
    private JTextField txtReference;
    
    private JButton btnProcess;
    private JButton btnCancel;
    
    public PaymentRegistrationDialog(JFrame parent, String invoiceNumber, String clientName, double remaining) {
        super(parent, "Registrar Pago", true);
        
        setSize(500, 420);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        
        initComponents();
        setInvoiceInfo(invoiceNumber, clientName, remaining);
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 247, 250));
        
        // === PANEL INFORMACIÓN (NORTE) ===
        mainPanel.add(createInfoPanel(), BorderLayout.NORTH);
        
        // === PANEL FORMULARIO (CENTRO) ===
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        
        // === PANEL BOTONES (SUR) ===
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2), 
                        "Información de la Factura", 
                        javax.swing.border.TitledBorder.LEFT, 
                        javax.swing.border.TitledBorder.TOP));
        panel.setBackground(Color.WHITE);
        panel.setOpaque(true);
        
        // Fila 1: Factura
        JPanel pnlInvoice = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlInvoice.setBackground(Color.WHITE);
        pnlInvoice.add(new JLabel("Factura:"));
        lblInvoiceNumber = new JLabel();
        lblInvoiceNumber.setFont(new Font("Arial", Font.BOLD, 13));
        lblInvoiceNumber.setForeground(new Color(52, 152, 219));
        pnlInvoice.add(lblInvoiceNumber);
        panel.add(pnlInvoice);
        
        // Fila 2: Cliente
        JPanel pnlClient = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlClient.setBackground(Color.WHITE);
        pnlClient.add(new JLabel("Cliente:"));
        lblClientName = new JLabel();
        lblClientName.setFont(new Font("Arial", Font.BOLD, 13));
        lblClientName.setForeground(new Color(44, 62, 80));
        pnlClient.add(lblClientName);
        panel.add(pnlClient);
        
        // Fila 3: Saldo Pendiente
        JPanel pnlRemaining = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlRemaining.setBackground(Color.WHITE);
        pnlRemaining.add(new JLabel("Saldo Pendiente:"));
        lblRemaining = new JLabel();
        lblRemaining.setFont(new Font("Arial", Font.BOLD, 14));
        lblRemaining.setForeground(new Color(231, 76, 60));
        pnlRemaining.add(lblRemaining);
        panel.add(pnlRemaining);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 15));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                        "Detalles del Pago",
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP));
        panel.setBackground(Color.WHITE);
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // === MONTO ===
        JPanel pnlAmount = new JPanel(new BorderLayout(10, 0));
        pnlAmount.setBackground(Color.WHITE);
        JLabel lblAmount = new JLabel("Monto a Pagar:");
        lblAmount.setFont(new Font("Arial", Font.BOLD, 12));
        lblAmount.setPreferredSize(new Dimension(120, 30));
        pnlAmount.add(lblAmount, BorderLayout.WEST);
        
        txtAmount = new JTextField();
        txtAmount.setFont(new Font("Arial", Font.PLAIN, 13));
        txtAmount.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        txtAmount.setPreferredSize(new Dimension(200, 35));
        pnlAmount.add(txtAmount, BorderLayout.CENTER);
        
        JLabel lblCurrency = new JLabel("$");
        lblCurrency.setFont(new Font("Arial", Font.BOLD, 14));
        lblCurrency.setPreferredSize(new Dimension(30, 35));
        pnlAmount.add(lblCurrency, BorderLayout.EAST);
        panel.add(pnlAmount);
        
        // === MÉTODO DE PAGO ===
        JPanel pnlMethod = new JPanel(new BorderLayout(10, 0));
        pnlMethod.setBackground(Color.WHITE);
        JLabel lblMethod = new JLabel("Método de Pago:");
        lblMethod.setFont(new Font("Arial", Font.BOLD, 12));
        lblMethod.setPreferredSize(new Dimension(120, 30));
        pnlMethod.add(lblMethod, BorderLayout.WEST);
        
        cmbPaymentMethod = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA", "TRANSFERENCIA"});
        cmbPaymentMethod.setFont(new Font("Arial", Font.PLAIN, 12));
        cmbPaymentMethod.setPreferredSize(new Dimension(200, 35));
        cmbPaymentMethod.setBackground(Color.WHITE);
        pnlMethod.add(cmbPaymentMethod, BorderLayout.CENTER);
        panel.add(pnlMethod);
        
        // === REFERENCIA ===
        JPanel pnlRef = new JPanel(new BorderLayout(10, 0));
        pnlRef.setBackground(Color.WHITE);
        JLabel lblRef = new JLabel("Referencia:");
        lblRef.setFont(new Font("Arial", Font.BOLD, 12));
        lblRef.setPreferredSize(new Dimension(120, 30));
        pnlRef.add(lblRef, BorderLayout.WEST);
        
        txtReference = new JTextField();
        txtReference.setFont(new Font("Arial", Font.PLAIN, 12));
        txtReference.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        txtReference.setPreferredSize(new Dimension(200, 35));
        txtReference.setToolTipText("Ej: Comprobante, número de transferencia, etc.");
        pnlRef.add(txtReference, BorderLayout.CENTER);
        panel.add(pnlRef);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(245, 247, 250));
        
        btnProcess = new JButton("✓ Procesar Pago");
        btnProcess.setPreferredSize(new Dimension(150, 40));
        btnProcess.setFont(new Font("Arial", Font.BOLD, 12));
        btnProcess.setBackground(new Color(46, 204, 113));
        btnProcess.setForeground(Color.WHITE);
        btnProcess.setFocusPainted(false);
        btnProcess.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 2));
        btnProcess.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panel.add(btnProcess);
        
        btnCancel = new JButton("✕ Cancelar");
        btnCancel.setPreferredSize(new Dimension(150, 40));
        btnCancel.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancel.setBackground(new Color(231, 76, 60));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(BorderFactory.createLineBorder(new Color(192, 57, 43), 2));
        btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());
        panel.add(btnCancel);
        
        return panel;
    }
    
    private void setInvoiceInfo(String invoiceNumber, String clientName, double remaining) {
        lblInvoiceNumber.setText(invoiceNumber);
        lblClientName.setText(clientName);
        lblRemaining.setText(String.format("$%,.2f", remaining));
    }
    
    // === GETTERS ===
    
    public String getAmount() {
        return txtAmount.getText().trim();
    }
    
    public String getPaymentMethod() {
        return (String) cmbPaymentMethod.getSelectedItem();
    }
    
    public String getReference() {
        return txtReference.getText().trim();
    }
    
    public JButton getBtnProcess() {
        return btnProcess;
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}