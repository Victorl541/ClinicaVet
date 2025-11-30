package com.clinicavet.controllers;

import com.clinicavet.model.entities.Invoice;
import com.clinicavet.views.PaymentRegistrationDialog;

public class PaymentRegistrationDialogController {
    
    private PaymentRegistrationDialog view;
    private Invoice invoice;
    private PaymentsViewController paymentsViewController;
    
    public PaymentRegistrationDialogController(PaymentRegistrationDialog view, 
                                              Invoice invoice,
                                              com.clinicavet.model.services.IPaymentService paymentService,
                                              com.clinicavet.model.services.IInvoiceService invoiceService,
                                              PaymentsViewController paymentsViewController) {
        this.view = view;
        this.invoice = invoice;
        this.paymentsViewController = paymentsViewController;
        
        System.out.println("[PaymentRegistrationDialogController] Inicializando...");
        
        setupListeners();
        
        System.out.println("[PaymentRegistrationDialogController] Inicializado");
    }
    
    private void setupListeners() {
        System.out.println("Configurando listeners...");
        
        // Botón procesar pago
        view.getBtnProcess().addActionListener(e -> processPayment());
        System.out.println("   ✓ Listener btnProcess configurado");
    }
    
    /**
     * Procesar el pago
     */
    private void processPayment() {
        System.out.println("Procesando pago...");
        
        try {
            String amountStr = view.getAmount();
            if (amountStr.isEmpty()) {
                view.showMessage("Ingresa el monto");
                return;
            }
            
            double amount = Double.parseDouble(amountStr);
            String paymentMethod = view.getPaymentMethod();
            String reference = view.getReference();
            
            // Validaciones
            if (amount <= 0) {
                view.showError("El monto debe ser mayor a 0");
                return;
            }
            
            // Llamar al controlador principal para procesar el pago
            paymentsViewController.processPayment(invoice, amount, paymentMethod, reference);
            
            // Cerrar diálogo
            view.dispose();
            
        } catch (NumberFormatException e) {
            view.showError("Monto inválido");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            view.showError("Error: " + e.getMessage());
        }
    }
}