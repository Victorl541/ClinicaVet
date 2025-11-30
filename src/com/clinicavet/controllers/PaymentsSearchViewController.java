package com.clinicavet.controllers;

import com.clinicavet.model.entities.Invoice;
import com.clinicavet.model.services.IInvoiceService;
import com.clinicavet.model.services.IPaymentService;
import com.clinicavet.views.PaymentRegistrationDialog;
import com.clinicavet.views.PaymentsSearchDialog;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.*;

public class PaymentsSearchViewController {
    
    private PaymentsSearchDialog view;
    private List<Invoice> allInvoices;
    private PaymentsViewController paymentsViewController;
    private IInvoiceService invoiceService;
    private IPaymentService paymentService;
    
    public PaymentsSearchViewController(PaymentsSearchDialog view, List<Invoice> allInvoices,
                                      PaymentsViewController paymentsViewController,
                                      IInvoiceService invoiceService,
                                      IPaymentService paymentService) {
        this.view = view;
        this.allInvoices = allInvoices;
        this.paymentsViewController = paymentsViewController;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
        
        System.out.println("🔍 [PaymentsSearchViewController] Inicializando...");
        
        setupListeners();
        addPaymentButton();
        // NO llamar performSearch() al inicio - tabla empieza vacía
        
        System.out.println("[PaymentsSearchViewController] Inicializado");
    }
    
    private void setupListeners() {
        System.out.println("Configurando listeners...");
        
        // Búsqueda en tiempo real - SOLO cuando hay texto
        view.txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                if (!view.getSearchText().isEmpty()) {
                    performSearch();
                } else {
                    view.setTableData(java.util.Collections.emptyList());
                }
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                if (!view.getSearchText().isEmpty()) {
                    performSearch();
                } else {
                    view.setTableData(java.util.Collections.emptyList());
                }
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                if (!view.getSearchText().isEmpty()) {
                    performSearch();
                } else {
                    view.setTableData(java.util.Collections.emptyList());
                }
            }
        });
        
        // Cambio de estado
        view.cbStatus.addActionListener(e -> {
            if (!view.getSearchText().isEmpty()) {
                performSearch();
            }
        });
        
        // Cambio de fechas
        view.spinDateFrom.addChangeListener(e -> {
            if (!view.getSearchText().isEmpty()) {
                performSearch();
            }
        });
        view.spinDateTo.addChangeListener(e -> {
            if (!view.getSearchText().isEmpty()) {
                performSearch();
            }
        });
        
        // Botón búsqueda manual
        view.btnSearch.addActionListener(e -> {
            System.out.println("🔘 Botón Buscar presionado");
            if (!view.getSearchText().isEmpty()) {
                performSearch();
            } else {
                view.showMessage("Ingresa el nombre del cliente");
            }
        });
        System.out.println("   ✓ Listeners configurados");
    }
    
    /**
     * Agregar botón de registrar pago en el diálogo
     */
    private void addPaymentButton() {
        JButton btnPayment = new JButton("Registrar Pago");
        btnPayment.setPreferredSize(new java.awt.Dimension(140, 35));
        btnPayment.setBackground(new java.awt.Color(46, 204, 113));
        btnPayment.setForeground(java.awt.Color.WHITE);
        btnPayment.setFocusPainted(false);
        
        btnPayment.addActionListener(e -> registerPayment());
        
        // Obtener panel de botones y agregar botón
        java.awt.Component[] components = view.getContentPane().getComponents();
        for (java.awt.Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                java.awt.LayoutManager layout = panel.getLayout();
                if (layout instanceof java.awt.BorderLayout) {
                    java.awt.Component south = ((java.awt.BorderLayout) layout).getLayoutComponent(java.awt.BorderLayout.SOUTH);
                    if (south instanceof JPanel) {
                        ((JPanel) south).add(btnPayment, 0);
                        ((JPanel) south).revalidate();
                        ((JPanel) south).repaint();
                        break;
                    }
                }
            }
        }
        
        System.out.println("   ✓ Botón de pago agregado");
    }
    
    /**
     * Registrar pago desde búsqueda
     */
    private void registerPayment() {
        System.out.println("Registrando pago desde búsqueda...");
        
        int selectedRow = view.getSelectedRow();
        
        if (selectedRow < 0) {
            view.showMessage("Selecciona una factura");
            return;
        }
        
        try {
            String invoiceNumber = (String) view.getTableModel().getValueAt(selectedRow, 0);
            
            Optional<Invoice> invoiceOpt = allInvoices.stream()
                    .filter(inv -> inv.getInvoiceNumber().equals(invoiceNumber))
                    .findFirst();
            
            if (invoiceOpt.isEmpty()) {
                view.showMessage("Factura no encontrada");
                return;
            }
            
            Invoice invoice = invoiceOpt.get();
            double remaining = paymentService.getRemainingAmount(invoice.getId());
            
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
            PaymentRegistrationDialog dialog = new PaymentRegistrationDialog(
                    parentFrame,
                    invoice.getInvoiceNumber(),
                    invoice.getClient() != null ? invoice.getClient().getName() : "N/A",
                    remaining
            );
            
            // Crear controlador para manejar el pago
            PaymentRegistrationDialogController dialogController = new PaymentRegistrationDialogController(
                    dialog,
                    invoice,
                    paymentService,
                    invoiceService,
                    paymentsViewController
            );
            
            dialog.setVisible(true);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            view.showMessage("Error: " + e.getMessage());
        }
    }
    
    /**
     * Realizar búsqueda con filtros - SOLO PENDIENTE
     */
    private void performSearch() {
        System.out.println("\n🔍 Filtrando facturas PENDIENTE...");
        
        String clientName = view.getSearchText();
        String statusFilter = view.getStatusFilter();
        LocalDate dateFrom = view.getDateFrom();
        LocalDate dateTo = view.getDateTo();

        System.out.println("   Cliente: '" + clientName + "'");
        System.out.println("   Estado: " + statusFilter);
        System.out.println("   Rango: " + dateFrom + " a " + dateTo);

        List<Invoice> results = allInvoices.stream()
                .filter(inv -> {
                    // FILTRO PRINCIPAL: SOLO FACTURAS PENDIENTE
                    if (inv.getStatus() != Invoice.InvoiceStatus.PENDIENTE) {
                        return false;
                    }
                    
                    // Filtrar por cliente (OBLIGATORIO si hay búsqueda)
                    if (inv.getClient() == null || 
                        !inv.getClient().getName().toLowerCase().contains(clientName.toLowerCase())) {
                        return false;
                    }
                    
                    // Filtrar por estado (si selecciona algo diferente a TODOS)
                    if (!statusFilter.equals("TODOS") && 
                        inv.getStatus() != Invoice.InvoiceStatus.valueOf(statusFilter)) {
                        return false;
                    }
                    
                    // Filtrar por rango de fechas
                    LocalDate invoiceDate = inv.getInvoiceDate();
                    if (invoiceDate.isBefore(dateFrom) || invoiceDate.isAfter(dateTo.plusDays(1))) {
                        return false;
                    }
                    
                    return true;
                })
                .collect(Collectors.toList());

        System.out.println("   ✓ Resultados encontrados: " + results.size());
        view.setTableData(results);
        System.out.println("✓ Tabla actualizada\n");
    }
}