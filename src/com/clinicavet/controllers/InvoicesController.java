package com.clinicavet.controllers;

import com.clinicavet.model.entities.Invoice;
import com.clinicavet.model.services.IInvoiceService;
import com.clinicavet.model.services.IOwnerService;
import com.clinicavet.model.services.IPaymentService;
import com.clinicavet.views.InvoiceFormDialog;
import com.clinicavet.views.InvoiceSearchDialog;
import com.clinicavet.views.InvoicesListView;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.*;

public class InvoicesController {

    private final InvoicesListView view;
    private final IInvoiceService invoiceService;
    private final IOwnerService ownerService;
    private final IPaymentService paymentService;
    private List<Invoice> allInvoices;

    public InvoicesController(InvoicesListView view,
                             IInvoiceService invoiceService,
                             IOwnerService ownerService,
                             IPaymentService paymentService) {

        this.view = view;
        this.invoiceService = invoiceService;
        this.ownerService = ownerService;
        this.paymentService = paymentService;

        System.out.println("[InvoicesController] Inicializando...");
        
        loadAllInvoices();
        setupListeners();

        System.out.println("[InvoicesController] Inicializado correctamente");
    }

    private void setupListeners() {
        // Botón búsqueda (abre diálogo)
        view.btnSearch.addActionListener(e -> openSearchDialog());

        // Botón nueva factura
        view.btnNew.addActionListener(e -> openNewInvoiceForm());

        // Botón anular (cambiar estado a CANCELADA)
        view.btnCancel.addActionListener(e -> cancelInvoice());
    }

    /**
     * Cargar todas las facturas desde el servicio
     */
    public void loadAllInvoices() {
        System.out.println("[InvoicesController] Cargando todas las facturas...");
        
        allInvoices = invoiceService.listInvoices();
        System.out.println("Total de facturas en memoria: " + allInvoices.size());
        
        view.setTableData(allInvoices);
        System.out.println("✓ Tabla actualizada con " + allInvoices.size() + " facturas");
    }

    /**
     * Abrir diálogo de búsqueda de facturas
     */
    private void openSearchDialog() {
        System.out.println("🔍 [InvoicesController] Abriendo diálogo de búsqueda de facturas");
        
        try {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(view);
            InvoiceSearchDialog searchDialog = new InvoiceSearchDialog(parentFrame, invoiceService);

            // Configurar rango de fechas por defecto (último mes)
            LocalDate today = LocalDate.now();
            searchDialog.setDefaultDateRange(today.minusMonths(1), today);

            System.out.println("   Dialog creado: ✓ OK");

            // Crear controller para el diálogo
            InvoiceSearchController searchController = new InvoiceSearchController(
                    searchDialog,
                    invoiceService,
                    paymentService
            );

            System.out.println("   Controller creado: ✓ OK");
            
            searchDialog.setVisible(true);
            System.out.println("   Dialog visible: ✓ OK");

        } catch (Exception ex) {
            System.err.println("ERROR al abrir búsqueda: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Abrir diálogo para crear nueva factura
     */
    private void openNewInvoiceForm() {
        System.out.println("➕ [InvoicesController] Abriendo diálogo de nueva factura");
        
        try {
            InvoiceFormDialog dialog = new InvoiceFormDialog(null, invoiceService, ownerService);
            System.out.println("   Dialog creado: ✓ OK");
            
            new InvoiceFormController(dialog, invoiceService);
            System.out.println("   Controller creado: ✓ OK");
            
            dialog.setVisible(true);
            System.out.println("   Dialog visible: ✓ OK");

            System.out.println("🔄 Recargando facturas después de crear");
            loadAllInvoices();

        } catch (Exception e) {
            System.err.println("ERROR al abrir nueva factura: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Anular factura (cambiar estado a CANCELADA)
     */
    private void cancelInvoice() {
        int selectedRow = view.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Selecciona una factura para anular", "Info", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("No hay factura seleccionada para anular");
            return;
        }

        String invoiceNumber = (String) view.tableInvoices.getValueAt(selectedRow, 0);
        Optional<Invoice> invoice = findInvoiceByNumber(invoiceNumber);

        if (invoice.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Factura no encontrada", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Factura no encontrada: " + invoiceNumber);
            return;
        }

        String currentStatus = invoice.get().getStatus().toString();
        if (currentStatus.equals("CANCELADA")) {
            JOptionPane.showMessageDialog(view, "Esta factura ya está anulada", "Info", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("ℹ️ Factura ya está anulada: " + invoiceNumber);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "¿Estás seguro de que deseas anular esta factura?\n" +
                "Factura: " + invoiceNumber + "\n" +
                "Estado actual: " + currentStatus,
                "Confirmar anulación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("⛔ [InvoicesController] Anulando factura: " + invoiceNumber);
            
            try {
                System.out.println("   Estado actual: " + invoice.get().getStatus());
                
                invoice.get().setStatus(Invoice.InvoiceStatus.ANULADA);
                System.out.println("   Nuevo estado: " + invoice.get().getStatus());
                
                invoiceService.updateInvoice(invoice.get());
                System.out.println("   ✓ Factura actualizada en memoria");
                
                invoiceService.saveInvoices();
                System.out.println("   ✓ Factura guardada en archivo JSON");
                
                JOptionPane.showMessageDialog(view, "Factura anulada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Factura anulada exitosamente: " + invoiceNumber);
                
                System.out.println("🔄 Recargando facturas después de anular");
                loadAllInvoices();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Error al anular la factura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("Error al anular factura: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            System.out.println("↩️ Anulación cancelada por el usuario");
        }
    }

    // === MÉTODOS AUXILIARES ===

    /**
     * Buscar factura por número de factura
     */
    private Optional<Invoice> findInvoiceByNumber(String invoiceNumber) {
        System.out.println("🔎 Buscando factura por número: " + invoiceNumber);
        Optional<Invoice> result = allInvoices.stream()
                .filter(inv -> inv.getInvoiceNumber().equals(invoiceNumber))
                .findFirst();
        
        if (result.isPresent()) {
            System.out.println("   ✓ Factura encontrada");
        } else {
            System.out.println("   ✗ Factura no encontrada");
        }
        
        return result;
    }

    /**
     * Recargar lista de facturas
     */
    public void refreshInvoices() {
        System.out.println("🔄 [InvoicesController] Recargando facturas...");
        loadAllInvoices();
    }

    /**
     * Obtener todas las facturas cargadas
     */
    public List<Invoice> getAllInvoices() {
        return allInvoices;
    }

    /**
     * Obtener facturas por estado
     */
    public List<Invoice> getInvoicesByStatus(Invoice.InvoiceStatus status) {
        System.out.println("🔍 Obteniendo facturas con estado: " + status);
        return allInvoices.stream()
                .filter(inv -> inv.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    /**
     * Obtener facturas por cliente
     */
    public List<Invoice> getInvoicesByClient(String clientName) {
        System.out.println("🔍 Obteniendo facturas del cliente: " + clientName);
        return allInvoices.stream()
                .filter(inv -> inv.getClient() != null &&
                        inv.getClient().getName().equalsIgnoreCase(clientName))
                .collect(Collectors.toList());
    }

    /**
     * Obtener facturas en rango de fechas
     */
    public List<Invoice> getInvoicesByDateRange(LocalDate from, LocalDate to) {
        System.out.println("🔍 Obteniendo facturas en rango: " + from + " a " + to);
        final LocalDate finalDateTo = to.plusDays(1);
        return allInvoices.stream()
                .filter(inv -> !inv.getInvoiceDate().isBefore(from) &&
                              inv.getInvoiceDate().isBefore(finalDateTo))
                .collect(Collectors.toList());
    }

    /**
     * Obtener total de facturas
     */
    public int getTotalInvoices() {
        return allInvoices.size();
    }

    /**
     * Obtener total de facturas pendientes
     */
    public double getTotalPendingAmount() {
        return allInvoices.stream()
                .filter(inv -> inv.getStatus().equals(Invoice.InvoiceStatus.PENDIENTE))
                .mapToDouble(Invoice::getTotal)
                .sum();
    }

    /**
     * Obtener total de facturas pagadas
     */
    public double getTotalPaidAmount() {
        return allInvoices.stream()
                .filter(inv -> inv.getStatus().equals(Invoice.InvoiceStatus.PAGADA))
                .mapToDouble(Invoice::getTotal)
                .sum();
    }

    /**
     * Obtener total de facturas canceladas/anuladas
     */
    public double getTotalCancelledAmount() {
        return allInvoices.stream()
                .filter(inv -> inv.getStatus().equals(Invoice.InvoiceStatus.ANULADA))
                .mapToDouble(Invoice::getTotal)
                .sum();
    }
}