package com.clinicavet.controllers;

import com.clinicavet.model.entities.Invoice;
import com.clinicavet.model.entities.InvoiceItem;
import com.clinicavet.model.entities.Owner;
import com.clinicavet.model.services.IInvoiceService;
import com.clinicavet.views.InvoiceFormDialog;
import java.time.LocalDate;
import java.util.UUID;
import javax.swing.*;

public class InvoiceFormController {

    private final InvoiceFormDialog view;
    private final IInvoiceService invoiceService;
    private Invoice invoice;

    public InvoiceFormController(InvoiceFormDialog view, IInvoiceService invoiceService) {
        this.view = view;
        this.invoiceService = invoiceService;
        this.invoice = view.getInvoice();

        System.out.println("📝 [InvoiceFormController] Inicializando...");
        
        if (invoice != null) {
            System.out.println("   Modo: EDITAR factura #" + invoice.getInvoiceNumber());
        } else {
            System.out.println("   Modo: CREAR nueva factura");
        }
        
        setupListeners();
        System.out.println("[InvoiceFormController] Listeners configurados");
    }

    private void setupListeners() {
        System.out.println("Configurando listeners...");
        
        // Agregar item
        view.btnAddItem.addActionListener(e -> addItem());
        System.out.println("   ✓ Listener btnAddItem configurado");

        // Eliminar item
        view.btnRemoveItem.addActionListener(e -> removeItem());
        System.out.println("   ✓ Listener btnRemoveItem configurado");

        // Guardar factura
        view.btnSave.addActionListener(e -> saveInvoice());
        System.out.println("   ✓ Listener btnSave configurado");
        
        System.out.println("✓ Todos los listeners configurados");
    }

    /**
     * Agregar nuevo item a la factura
     */
    private void addItem() {
        System.out.println("➕ [InvoiceFormController] Agregando item...");
        
        try {
            String description = view.txtDescription.getText().trim();
            String quantityStr = view.spinQuantity.getValue().toString();
            String priceStr = view.txtUnitPrice.getText().trim();
            String category = (String) view.cbCategory.getSelectedItem();

            System.out.println("   Descripción: " + (description.isEmpty() ? "VACÍA" : description));
            System.out.println("   Cantidad: " + quantityStr);
            System.out.println("   Precio: " + (priceStr.isEmpty() ? "VACÍO" : priceStr));
            System.out.println("   Categoría: " + category);

            // Validaciones
            if (description.isEmpty()) {
                showError("Ingresa la descripción del item");
                System.out.println("Descripción vacía");
                return;
            }

            if (priceStr.isEmpty()) {
                showError("Ingresa el precio unitario");
                System.out.println("Precio vacío");
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            double unitPrice = Double.parseDouble(priceStr);

            System.out.println("   Cantidad parseada: " + quantity);
            System.out.println("   Precio parseado: " + unitPrice);

            if (quantity <= 0) {
                showError("La cantidad debe ser mayor a 0");
                System.out.println("Cantidad inválida: " + quantity);
                return;
            }

            if (unitPrice <= 0) {
                showError("El precio debe ser mayor a 0");
                System.out.println("Precio inválido: " + unitPrice);
                return;
            }

            // Crear item
            InvoiceItem item = new InvoiceItem();
            item.setId(UUID.randomUUID());
            item.setDescription(description);
            item.setQuantity(quantity);
            item.setUnitPrice(unitPrice);
            item.setCategory(category);

            System.out.println("   Item creado con ID: " + item.getId());

            // Agregar a la vista
            view.addItem(item);
            System.out.println("   ✓ Item agregado a la vista");

            // Limpiar campos
            view.txtDescription.setText("");
            view.spinQuantity.setValue(1);
            view.txtUnitPrice.setText("");
            view.cbCategory.setSelectedIndex(0);
            view.txtDescription.requestFocus();

            System.out.println("   ✓ Campos limpios");
            System.out.println("Item agregado exitosamente: " + description + " x" + quantity + " @ $" + unitPrice);
            System.out.println("   Total items en factura: " + view.getItems().size());

        } catch (NumberFormatException ex) {
            showError("Valores numéricos inválidos: " + ex.getMessage());
            System.err.println("Error al parsear números: " + ex.getMessage());
        } catch (Exception ex) {
            showError("Error al agregar item: " + ex.getMessage());
            System.err.println("Error al agregar item: ");
            ex.printStackTrace();
        }
    }

    /**
     * Eliminar item seleccionado de la factura
     */
    private void removeItem() {
        System.out.println("🗑️ [InvoiceFormController] Eliminando item...");
        
        int selectedRow = view.tableItems.getSelectedRow();
        
        if (selectedRow < 0) {
            showInfo("Selecciona un item para eliminar");
            System.out.println("No hay item seleccionado");
            return;
        }

        System.out.println("   Item seleccionado en fila: " + selectedRow);

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "¿Estás seguro de que deseas eliminar este item?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("   ✓ Usuario confirmó eliminación");
            view.removeItem(selectedRow);
            System.out.println("Item eliminado");
            System.out.println("   Total items en factura: " + view.getItems().size());
        } else {
            System.out.println("↩️ Eliminación cancelada por el usuario");
        }
    }

    /**
     * Guardar factura (crear o actualizar)
     */
    private void saveInvoice() {
        System.out.println("\n[InvoiceFormController] Guardando factura...");
        
        try {
            // Validar cliente
            Owner owner = view.getSelectedOwner();
            if (owner == null) {
                showError("Selecciona un cliente válido");
                System.out.println("Cliente no seleccionado");
                return;
            }

            System.out.println("   Cliente: " + owner.getName());

            // Validar items
            if (view.getItems().isEmpty()) {
                showError("Agrega al menos un item a la factura");
                System.out.println("Sin items en la factura");
                return;
            }

            System.out.println("   Items: " + view.getItems().size());

            // Obtener datos
            LocalDate invoiceDate = view.getInvoiceDate();
            String notes = view.getNotes();

            System.out.println("   Fecha: " + invoiceDate);
            System.out.println("   Notas: " + (notes.isEmpty() ? "SIN NOTAS" : notes));

            // Calcular totales
            double subtotal = calculateSubtotal();
            double tax = subtotal * 0.19;
            double total = subtotal + tax;

            System.out.println("   Subtotal: $" + String.format("%.2f", subtotal));
            System.out.println("   IVA (19%): $" + String.format("%.2f", tax));
            System.out.println("   Total: $" + String.format("%.2f", total));

            if (invoice == null) {
                // === CREAR NUEVA FACTURA ===
                createNewInvoice(owner, invoiceDate, notes, subtotal, tax, total);
            } else {
                // === ACTUALIZAR FACTURA EXISTENTE ===
                updateExistingInvoice(owner, invoiceDate, notes, subtotal, tax, total);
            }

        } catch (Exception ex) {
            showError("Error al guardar factura: " + ex.getMessage());
            System.err.println("Error grave al guardar: ");
            ex.printStackTrace();
        }
    }

    /**
     * Crear nueva factura
     */
    private void createNewInvoice(Owner owner, LocalDate invoiceDate, String notes, 
                                 double subtotal, double tax, double total) {
        System.out.println("\n➕ [InvoiceFormController] Creando nueva factura...");

        try {
            invoice = new Invoice();
            invoice.setId(view.getInvoiceId());
            invoice.setInvoiceNumber(generateInvoiceNumber());
            invoice.setInvoiceDate(invoiceDate);
            invoice.setClient(owner);
            invoice.setItems(view.getItems());
            invoice.setNotes(notes);
            invoice.setStatus(Invoice.InvoiceStatus.PENDIENTE);
            invoice.setSubtotal(subtotal);
            invoice.setTax(tax);
            invoice.setTotal(total);

            System.out.println("   ID: " + invoice.getId());
            System.out.println("   Número: " + invoice.getInvoiceNumber());
            System.out.println("   Cliente: " + owner.getName());
            System.out.println("   Estado: " + invoice.getStatus());

            // Crear factura en el repositorio
            invoiceService.createInvoice(invoice);
            System.out.println("   ✓ Factura creada en memoria");

            // Guardar en JSON
            invoiceService.saveInvoices();
            System.out.println("   ✓ Factura guardada en archivo JSON");

            showSuccess("Factura creada exitosamente\nNúmero: " + invoice.getInvoiceNumber() + 
                       "\nTotal: $" + String.format("%.2f", total));
            System.out.println("Factura creada exitosamente: " + invoice.getInvoiceNumber());
            view.dispose();

        } catch (Exception ex) {
            showError("Error al crear la factura: " + ex.getMessage());
            System.err.println("Error en createNewInvoice: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Actualizar factura existente
     */
    private void updateExistingInvoice(Owner owner, LocalDate invoiceDate, String notes,
                                       double subtotal, double tax, double total) {
        System.out.println("\n✏️ [InvoiceFormController] Actualizando factura...");

        try {
            invoice.setClient(owner);
            invoice.setInvoiceDate(invoiceDate);
            invoice.setItems(view.getItems());
            invoice.setNotes(notes);
            invoice.setSubtotal(subtotal);
            invoice.setTax(tax);
            invoice.setTotal(total);

            System.out.println("   Número: " + invoice.getInvoiceNumber());
            System.out.println("   Cliente: " + owner.getName());
            System.out.println("   Total actualizado: $" + String.format("%.2f", total));

            // Actualizar factura en el repositorio
            invoiceService.updateInvoice(invoice);
            System.out.println("   ✓ Factura actualizada en memoria");

            // Guardar en JSON
            invoiceService.saveInvoices();
            System.out.println("   ✓ Factura guardada en archivo JSON");

            showSuccess("Factura actualizada exitosamente\nNúmero: " + invoice.getInvoiceNumber() + 
                       "\nTotal: $" + String.format("%.2f", total));
            System.out.println("Factura actualizada: " + invoice.getInvoiceNumber());
            view.dispose();

        } catch (Exception ex) {
            showError("Error al actualizar la factura: " + ex.getMessage());
            System.err.println("Error en updateExistingInvoice: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Calcular subtotal de todos los items
     */
    private double calculateSubtotal() {
        double subtotal = 0;
        for (InvoiceItem item : view.getItems()) {
            double itemSubtotal = item.getQuantity() * item.getUnitPrice();
            subtotal += itemSubtotal;
            System.out.println("      - " + item.getDescription() + ": " + item.getQuantity() + 
                             " x $" + String.format("%.2f", item.getUnitPrice()) + 
                             " = $" + String.format("%.2f", itemSubtotal));
        }
        return subtotal;
    }

    /**
     * Generar número de factura único y legible
     */
    private String generateInvoiceNumber() {
        LocalDate today = LocalDate.now();
        int invoiceCount = invoiceService.listInvoices().size() + 1;
        
        String invoiceNumber = String.format("FAC-%04d%02d%02d-%05d",
                today.getYear(),
                today.getMonthValue(),
                today.getDayOfMonth(),
                invoiceCount);

        System.out.println("   ✓ Número de factura generado: " + invoiceNumber);
        return invoiceNumber;
    }

    // === MÉTODOS AUXILIARES PARA MENSAJES ===

    /**
     * Mostrar mensaje de error
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
        System.err.println(message);
    }

    /**
     * Mostrar mensaje de éxito
     */
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(view, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Mostrar mensaje de información
     */
    private void showInfo(String message) {
        JOptionPane.showMessageDialog(view, message, "ℹ️ Información", JOptionPane.INFORMATION_MESSAGE);
    }
}