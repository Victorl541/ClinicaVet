package com.clinicavet.controllers;

import com.clinicavet.model.entities.Invoice;
import com.clinicavet.model.services.IInvoiceService;
import com.clinicavet.model.services.IPaymentService;
import com.clinicavet.views.InvoiceSearchDialog;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InvoiceSearchController {

    private final InvoiceSearchDialog view;
    private final IInvoiceService invoiceService;
    private final IPaymentService paymentService;
    private List<Invoice> allInvoices;

    public InvoiceSearchController(InvoiceSearchDialog view, 
                                 IInvoiceService invoiceService,
                                 IPaymentService paymentService) {
        this.view = view;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
        this.allInvoices = invoiceService.listInvoices();

        System.out.println("🔍 [InvoiceSearchController] Inicializando...");
        System.out.println("   Total facturas disponibles: " + allInvoices.size());
        
        setupListeners();
        
        System.out.println("[InvoiceSearchController] Inicializado correctamente");
    }

    private void setupListeners() {
        System.out.println("Configurando listeners...");
        
        // Búsqueda en tiempo real por cliente
        view.txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }
        });
        System.out.println("   ✓ Listener txtSearch configurado");

        // Cambio de estado de filtro
        view.cbStatus.addActionListener(e -> performSearch());
        System.out.println("   ✓ Listener cbStatus configurado");

        // Cambio de rango de fechas
        view.spinDateFrom.addChangeListener(e -> performSearch());
        view.spinDateTo.addChangeListener(e -> performSearch());
        System.out.println("   ✓ Listeners de fechas configurados");

        // Botón búsqueda manual
        view.btnSearch.addActionListener(e -> {
            System.out.println("🔘 Botón Buscar presionado");
            performSearch();
        });
        System.out.println("   ✓ Listener btnSearch configurado");

        // Botón cerrar
        view.btnClose.addActionListener(e -> {
            System.out.println("🔒 Cerrando diálogo de búsqueda");
            view.dispose();
        });
        System.out.println("   ✓ Listener btnClose configurado");
        
        System.out.println("✓ Todos los listeners configurados");
    }

    /**
     * Realizar búsqueda con filtros
     */
    private void performSearch() {
        System.out.println("\n🔍 [InvoiceSearchController] Filtrando facturas...");
        
        String clientName = view.getSearchText();
        String status = view.getStatusFilter();
        LocalDate dateFrom = view.getDateFrom();
        LocalDate dateTo = view.getDateTo();

        System.out.println("   Cliente: " + (clientName.isEmpty() ? "TODOS" : "'" + clientName + "'"));
        System.out.println("   Estado: " + status);
        System.out.println("   Rango: " + dateFrom + " a " + dateTo);
        System.out.println("   Total facturas a filtrar: " + allInvoices.size());

        List<Invoice> results = allInvoices.stream().collect(Collectors.toList());

        // Filtrar por cliente
        if (!clientName.isEmpty()) {
            System.out.println("  → Filtrando por cliente: '" + clientName + "'");
            int before = results.size();
            results = results.stream()
                    .filter(inv -> inv.getClient() != null &&
                            inv.getClient().getName().toLowerCase().contains(clientName.toLowerCase()))
                    .collect(Collectors.toList());
            System.out.println("     Antes: " + before + " | Después: " + results.size());
        }

        // Filtrar por estado
        if (!status.equals("TODOS")) {
            System.out.println("  → Filtrando por estado: " + status);
            int before = results.size();
            try {
                Invoice.InvoiceStatus statusEnum = Invoice.InvoiceStatus.valueOf(status);
                results = results.stream()
                        .filter(inv -> inv.getStatus().equals(statusEnum))
                        .collect(Collectors.toList());
                System.out.println("     Antes: " + before + " | Después: " + results.size());
            } catch (IllegalArgumentException ex) {
                System.err.println("Estado inválido: " + status);
            }
        }

        // Filtrar por rango de fechas
        System.out.println("  → Filtrando por rango de fechas: " + dateFrom + " a " + dateTo);
        int before = results.size();
        final LocalDate finalDateTo = dateTo.plusDays(1); // Incluir el día final
        results = results.stream()
                .filter(inv -> !inv.getInvoiceDate().isBefore(dateFrom) &&
                              inv.getInvoiceDate().isBefore(finalDateTo))
                .collect(Collectors.toList());
        System.out.println("     Antes: " + before + " | Después: " + results.size());

        System.out.println("✓ Búsqueda completada. Total resultados: " + results.size());
        
        // Mostrar resultados en tabla
        view.setTableData(results);
        System.out.println("✓ Tabla actualizada con resultados\n");
    }


    // === MÉTODOS AUXILIARES ===

    /**
     * Buscar factura por número
     */
    private Optional<Invoice> findInvoiceByNumber(String invoiceNumber) {
        return allInvoices.stream()
                .filter(inv -> inv.getInvoiceNumber().equals(invoiceNumber))
                .findFirst();
    }

    /**
     * Recargar lista de facturas desde el servicio
     */
    public void refreshInvoices() {
        System.out.println("🔄 Recargando facturas...");
        allInvoices = invoiceService.listInvoices();
        performSearch();
    }
}