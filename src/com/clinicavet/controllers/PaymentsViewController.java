package com.clinicavet.controllers;

import com.clinicavet.model.entities.Invoice;
import com.clinicavet.model.entities.Payment;
import com.clinicavet.model.services.IInvoiceService;
import com.clinicavet.model.services.IPaymentService;
import com.clinicavet.views.PaymentRegistrationDialog;
import com.clinicavet.views.PaymentsSearchDialog;
import com.clinicavet.views.PaymentsView;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.swing.*;

public class PaymentsViewController {
    
    private PaymentsView view;
    private IPaymentService paymentService;
    private IInvoiceService invoiceService;
    private List<Invoice> allInvoices;
    
    public PaymentsViewController(PaymentsView view, IPaymentService paymentService, 
                                 IInvoiceService invoiceService) {
        this.view = view;
        this.paymentService = paymentService;
        this.invoiceService = invoiceService;
        this.allInvoices = invoiceService.listInvoices();
        
        System.out.println("[PaymentsViewController] Inicializando...");
        
        setupListeners();
        loadDefaultData();
        
        System.out.println("[PaymentsViewController] Inicializado");
    }
    
    private void setupListeners() {
        System.out.println("Configurando listeners...");
        
        // Botón búsqueda
        view.btnSearch.addActionListener(e -> openSearchDialog());
        System.out.println("   ✓ Listener btnSearch configurado");
        
        // Botón registrar pago
        view.btnPayInvoice.addActionListener(e -> openPaymentDialog());
        System.out.println("   ✓ Listener btnPayInvoice configurado");
    }
    
    /**
     * Cargar datos por defecto (SOLO PENDIENTE)
     */
    private void loadDefaultData() {
        System.out.println("Cargando datos por defecto...");
        
        List<Invoice> pendingInvoices = allInvoices.stream()
                .filter(inv -> inv.getStatus() == Invoice.InvoiceStatus.PENDIENTE)
                .collect(Collectors.toList());
        
        System.out.println("   ✓ Facturas PENDIENTE: " + pendingInvoices.size());
        view.setTableData(pendingInvoices);
    }
    
    /**
     * Abrir diálogo de búsqueda
     */
    private void openSearchDialog() {
        System.out.println("🔍 Abriendo diálogo de búsqueda...");
        
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
        PaymentsSearchDialog searchDialog = new PaymentsSearchDialog(parentFrame);
        
        // Establecer rango de fechas por defecto
        LocalDate today = LocalDate.now();
        searchDialog.setDefaultDateRange(today.minusMonths(3), today);
        
        // Crear controlador
        PaymentsSearchViewController searchController = new PaymentsSearchViewController(
                searchDialog,
                allInvoices,
                this,
                invoiceService,
                paymentService
        );
        
        searchDialog.setVisible(true);
    }
    
    /**
     * Abrir diálogo de pago
     */
    private void openPaymentDialog() {
        System.out.println("Abriendo diálogo de pago...");
        
        int selectedRow = view.getSelectedRow();
        
        if (selectedRow < 0) {
            view.showMessage("Selecciona una factura");
            return;
        }
        
        try {
            // Obtener datos de la tabla
            String invoiceNumber = (String) view.getTableModel().getValueAt(selectedRow, 0);
            
            // Buscar Invoice completo
            Optional<Invoice> invoiceOpt = allInvoices.stream()
                    .filter(inv -> inv.getInvoiceNumber().equals(invoiceNumber))
                    .findFirst();
            
            if (invoiceOpt.isEmpty()) {
                view.showError("Factura no encontrada");
                return;
            }
            
            Invoice invoice = invoiceOpt.get();
            
            // Crear PaymentRegistrationDialog
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
            PaymentRegistrationDialog dialog = new PaymentRegistrationDialog(
                    parentFrame,
                    invoice.getInvoiceNumber(),
                    invoice.getClient() != null ? invoice.getClient().getName() : "N/A",
                    invoice.getRemaining()
            );
            
            // Crear controlador para manejar el pago
            PaymentRegistrationDialogController dialogController = new PaymentRegistrationDialogController(
                    dialog,
                    invoice,
                    paymentService,
                    invoiceService,
                    this
            );
            
            dialog.setVisible(true);
            
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
            view.showError("Error: " + ex.getMessage());
        }
    }
    
    /**
     * Procesar el pago (llamado desde PaymentRegistrationDialogController)
     */
    public void processPayment(Invoice invoice, double amount, String paymentMethod, String reference) {
        System.out.println("Procesando pago...");
        
        try {
            if (amount <= 0) {
                JOptionPane.showMessageDialog(view, "El monto debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double remaining = paymentService.getRemainingAmount(invoice.getId());
            if (amount > remaining) {
                JOptionPane.showMessageDialog(view, 
                    String.format("El monto no puede exceder: $%.2f", remaining),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Simular procesamiento
            simulatePaymentProcessing(paymentMethod);
            
            // Crear Payment
            Payment payment = new Payment();
            payment.setId(UUID.randomUUID());
            payment.setInvoiceId(invoice.getId());
            payment.setAmount(amount);
            payment.setPaymentMethod(Payment.PaymentMethod.valueOf(paymentMethod));
            payment.setPaymentDate(LocalDateTime.now());
            payment.setReference(reference);
            
            // Guardar pago
            if (paymentService.createPayment(payment)) {
                paymentService.savePayments();
                System.out.println("   ✓ Pago guardado");
                
                // ACTUALIZAR ESTADO DE FACTURA AUTOMÁTICAMENTE
                double newTotalPaid = paymentService.getTotalPaidByInvoice(invoice.getId());
                invoice.setTotalPaid(newTotalPaid);
                
                // Si está completamente pagada
                if (invoice.getRemaining() <= 0) {
                    invoice.setStatus(Invoice.InvoiceStatus.PAGADA);
                    System.out.println("   ✓ Factura marcada como PAGADA");
                }
                
                invoiceService.updateInvoice(invoice);
                invoiceService.saveInvoices();
                
                JOptionPane.showMessageDialog(view,
                    "✓ Pago registrado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Recargar tabla
                allInvoices = invoiceService.listInvoices();
                loadDefaultData();
                
            } else {
                JOptionPane.showMessageDialog(view, "Error al registrar pago", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            JOptionPane.showMessageDialog(view, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Simular procesamiento de pago
     */
    private void simulatePaymentProcessing(String method) {
        System.out.println("   📡 Simulando: " + method);
        
        switch (method) {
            case "EFECTIVO":
                System.out.println("   ✓ Efectivo recibido");
                break;
            case "TARJETA":
                System.out.println("   ✓ Transacción de tarjeta aprobada");
                break;
            case "TRANSFERENCIA":
                System.out.println("   ✓ Transferencia bancaria confirmada");
                break;
        }
    }
}