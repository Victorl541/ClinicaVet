package com.clinicavet.views;

import com.clinicavet.model.entities.Invoice;
import com.clinicavet.model.entities.InvoiceItem;
import com.clinicavet.model.entities.Owner;
import com.clinicavet.model.services.IInvoiceService;
import com.clinicavet.model.services.IOwnerService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class InvoiceFormDialog extends JDialog {

    public JTextField txtSearchClient;
    public JLabel lblSelectedClient;
    public JLabel lblInvoiceId;
    public JSpinner spinDate;
    public JTextField txtDescription;
    public JSpinner spinQuantity;
    public JTextField txtUnitPrice;
    public JComboBox<String> cbCategory;
    public JTable tableItems;
    public JButton btnAddItem;
    public JButton btnRemoveItem;
    public JTextField txtNotes;
    public JButton btnSave;
    public JButton btnCancel;
    public JLabel lblSubtotal;
    public JLabel lblTax;
    public JLabel lblTotal;

    private IInvoiceService invoiceService;
    private IOwnerService ownerService;
    private Invoice invoice;
    private List<InvoiceItem> items;
    private Owner selectedOwner;
    private List<Owner> allOwners;
    private JPopupMenu suggestionsPopup;
    private UUID invoiceId;

    public InvoiceFormDialog(Invoice invoice, IInvoiceService invoiceService, IOwnerService ownerService) {
        this.invoice = invoice;
        this.invoiceService = invoiceService;
        this.ownerService = ownerService;
        this.items = invoice != null ? invoice.getItems() : new java.util.ArrayList<>();
        this.allOwners = ownerService.findAll();
        this.invoiceId = invoice != null ? invoice.getId() : UUID.randomUUID();

        setTitle(invoice == null ? "Nueva Factura" : "Editar Factura");
        setModal(true);
        setSize(950, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        loadInvoiceData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // === SECCIÓN SUPERIOR: BÚSQUEDA DE CLIENTE ===
        JPanel clientPanel = new JPanel(new GridBagLayout());
        clientPanel.setBorder(BorderFactory.createTitledBorder("Información de la Factura"));
        GridBagConstraints gbcClient = new GridBagConstraints();
        gbcClient.insets = new Insets(5, 5, 5, 5);
        gbcClient.anchor = GridBagConstraints.WEST;

        // Búsqueda cliente con dropdown
        JLabel lblSearchClient = new JLabel("Buscar Cliente:");
        txtSearchClient = new JTextField(20);
        JLabel lblResult = new JLabel("Cliente:");
        lblSelectedClient = new JLabel("Sin seleccionar");
        lblSelectedClient.setFont(lblSelectedClient.getFont().deriveFont(Font.BOLD));
        lblSelectedClient.setForeground(new Color(0, 100, 0));

        gbcClient.gridx = 0;
        gbcClient.gridy = 0;
        clientPanel.add(lblSearchClient, gbcClient);
        gbcClient.gridx = 1;
        gbcClient.fill = GridBagConstraints.HORIZONTAL;
        gbcClient.weightx = 1;
        clientPanel.add(txtSearchClient, gbcClient);

        gbcClient.gridx = 2;
        gbcClient.gridy = 0;
        gbcClient.weightx = 0;
        gbcClient.fill = GridBagConstraints.NONE;
        clientPanel.add(lblResult, gbcClient);
        gbcClient.gridx = 3;
        gbcClient.fill = GridBagConstraints.HORIZONTAL;
        gbcClient.weightx = 1;
        clientPanel.add(lblSelectedClient, gbcClient);

        // UUID de factura (auto-generado, solo lectura)
        JLabel lblIdLabel = new JLabel("ID Factura:");
        lblInvoiceId = new JLabel(invoiceId.toString());
        lblInvoiceId.setFont(lblInvoiceId.getFont().deriveFont(Font.PLAIN, 10f));
        lblInvoiceId.setForeground(new Color(100, 100, 100));

        gbcClient.gridx = 0;
        gbcClient.gridy = 1;
        gbcClient.weightx = 0;
        clientPanel.add(lblIdLabel, gbcClient);
        gbcClient.gridx = 1;
        gbcClient.fill = GridBagConstraints.HORIZONTAL;
        gbcClient.weightx = 1;
        clientPanel.add(lblInvoiceId, gbcClient);

        // Fecha
        JLabel lblDate = new JLabel("Fecha (YYYY-MM-DD):");
        spinDate = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinDate, "yyyy-MM-dd");
        spinDate.setEditor(editor);
        spinDate.setPreferredSize(new Dimension(150, 25));

        gbcClient.gridx = 2;
        gbcClient.gridy = 1;
        gbcClient.weightx = 0;
        gbcClient.fill = GridBagConstraints.NONE;
        clientPanel.add(lblDate, gbcClient);
        gbcClient.gridx = 3;
        gbcClient.fill = GridBagConstraints.HORIZONTAL;
        gbcClient.weightx = 1;
        clientPanel.add(spinDate, gbcClient);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        mainPanel.add(clientPanel, gbc);

        // === SECCIÓN ITEMS ===
        JLabel lblItems = new JLabel("Ítems de la Factura:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 1;
        mainPanel.add(lblItems, gbc);

        // Panel para agregar items
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        itemPanel.add(new JLabel("Descripción:"));
        txtDescription = new JTextField(12);
        itemPanel.add(txtDescription);

        itemPanel.add(new JLabel("Cantidad:"));
        spinQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinQuantity.setPreferredSize(new Dimension(60, 25));
        itemPanel.add(spinQuantity);

        itemPanel.add(new JLabel("Precio Unit:"));
        txtUnitPrice = new JTextField(10);
        itemPanel.add(txtUnitPrice);

        itemPanel.add(new JLabel("Categoría:"));
        cbCategory = new JComboBox<>(new String[]{"SERVICIO", "MEDICAMENTO", "PRODUCTO"});
        itemPanel.add(cbCategory);

        btnAddItem = new JButton("✚ Agregar Item");
        itemPanel.add(btnAddItem);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        mainPanel.add(itemPanel, gbc);

        // Tabla de items
        tableItems = new JTable();
        tableItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableItems.setRowHeight(24);
        JScrollPane scrollItems = new JScrollPane(tableItems);
        scrollItems.setPreferredSize(new Dimension(900, 220));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        mainPanel.add(scrollItems, gbc);

        // Botón eliminar item
        btnRemoveItem = new JButton("Eliminar Item Seleccionado");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0;
        mainPanel.add(btnRemoveItem, gbc);

        // === NOTAS ===
        JLabel lblNotes = new JLabel("Notas:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        mainPanel.add(lblNotes, gbc);

        txtNotes = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(txtNotes, gbc);

        // === TOTALES ===
        JPanel totalsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        totalsPanel.setBackground(new Color(240, 240, 240));
        totalsPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        lblSubtotal = new JLabel("Subtotal: $0.00");
        lblTax = new JLabel("IVA (19%): $0.00");
        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 14f));
        lblTotal.setForeground(new Color(0, 100, 0));

        totalsPanel.add(lblSubtotal);
        totalsPanel.add(new JSeparator(SwingConstants.VERTICAL));
        totalsPanel.add(lblTax);
        totalsPanel.add(new JSeparator(SwingConstants.VERTICAL));
        totalsPanel.add(lblTotal);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        mainPanel.add(totalsPanel, gbc);

        // Panel botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnSave = new JButton("Guardar");
        btnCancel = new JButton("✕ Cancelar");
        btnSave.setPreferredSize(new Dimension(110, 35));
        btnCancel.setPreferredSize(new Dimension(110, 35));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnCancel.addActionListener(e -> dispose());

        // Listener para búsqueda con dropdown
        setupClientSearchListener();
    }

    private void setupClientSearchListener() {
        // Crear popup para sugerencias
        suggestionsPopup = new JPopupMenu();
        suggestionsPopup.setFocusable(false);

        // Listener para cambios en el texto
        txtSearchClient.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                showSuggestions();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                showSuggestions();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                showSuggestions();
            }
        });

        // Listener para teclas (Enter para seleccionar)
        txtSearchClient.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    suggestionsPopup.setVisible(false);
                }
            }
        });
    }

    private void showSuggestions() {
        String searchText = txtSearchClient.getText().trim().toLowerCase();

        suggestionsPopup.removeAll();

        if (searchText.isEmpty()) {
            suggestionsPopup.setVisible(false);
            lblSelectedClient.setText("Sin seleccionar");
            lblSelectedClient.setForeground(Color.RED);
            selectedOwner = null;
            return;
        }

        // Filtrar clientes que coincidan
        List<Owner> matches = allOwners.stream()
                .filter(owner -> owner.getName().toLowerCase().contains(searchText) ||
                               owner.getId().contains(searchText) ||
                               owner.getPhone().contains(searchText))
                .collect(Collectors.toList());

        if (matches.isEmpty()) {
            lblSelectedClient.setText("Cliente no encontrado");
            lblSelectedClient.setForeground(Color.RED);
            selectedOwner = null;
            suggestionsPopup.setVisible(false);
            return;
        }

        if (matches.size() == 1 && matches.get(0).getName().toLowerCase().equals(searchText)) {
            // Selección exacta
            selectedOwner = matches.get(0);
            lblSelectedClient.setText(selectedOwner.getName() + " (" + selectedOwner.getId() + ") - " + selectedOwner.getPhone());
            lblSelectedClient.setForeground(new Color(0, 100, 0));
            suggestionsPopup.setVisible(false);
            System.out.println("✓ Cliente seleccionado: " + selectedOwner.getName());
            return;
        }

        // Mostrar dropdown con sugerencias
        for (Owner owner : matches) {
            JMenuItem item = new JMenuItem(owner.getName() + " (" + owner.getId() + ") - " + owner.getPhone());
            item.addActionListener(e -> selectClient(owner));
            suggestionsPopup.add(item);
        }

        // Mostrar popup debajo del campo de búsqueda
        suggestionsPopup.show(txtSearchClient, 0, txtSearchClient.getHeight());
    }

    private void selectClient(Owner owner) {
        selectedOwner = owner;
        txtSearchClient.setText(owner.getName());
        lblSelectedClient.setText(owner.getName() + " (" + owner.getId() + ") - " + owner.getPhone());
        lblSelectedClient.setForeground(new Color(0, 100, 0));
        suggestionsPopup.setVisible(false);
        System.out.println("✓ Cliente seleccionado: " + owner.getName());
    }

    private void loadInvoiceData() {
        if (invoice != null) {
            // Editar factura existente
            lblInvoiceId.setText(invoice.getId().toString());
            spinDate.setValue(java.sql.Date.valueOf(invoice.getInvoiceDate()));
            selectedOwner = invoice.getClient();
            txtSearchClient.setText(selectedOwner.getName());
            lblSelectedClient.setText(selectedOwner.getName() + " (" + selectedOwner.getId() + ") - " + selectedOwner.getPhone());
            lblSelectedClient.setForeground(new Color(0, 100, 0));
            txtNotes.setText(invoice.getNotes() != null ? invoice.getNotes() : "");
            updateItemsTable();
        } else {
            // Nueva factura
            spinDate.setValue(new java.util.Date());
        }
    }

    public void updateItemsTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Descripción", "Cantidad", "Precio Unit", "Categoría", "Subtotal"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (InvoiceItem item : items) {
            model.addRow(new Object[]{
                    item.getDescription(),
                    item.getQuantity(),
                    String.format("$%,.2f", item.getUnitPrice()),
                    item.getCategory(),
                    String.format("$%,.2f", item.getQuantity() * item.getUnitPrice())
            });
        }

        tableItems.setModel(model);
        updateTotals();
    }

    public void updateTotals() {
        double subtotal = 0;
        for (InvoiceItem item : items) {
            subtotal += item.getQuantity() * item.getUnitPrice();
        }

        double tax = subtotal * 0.19;
        double total = subtotal + tax;

        lblSubtotal.setText(String.format("Subtotal: $%,.2f", subtotal));
        lblTax.setText(String.format("IVA (19%%): $%,.2f", tax));
        lblTotal.setText(String.format("Total: $%,.2f", total));
    }

    // === GETTERS PARA ACCEDER DESDE CONTROLLER ===

    public Owner getSelectedOwner() {
        return selectedOwner;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public String getInvoiceIdString() {
        return invoiceId.toString();
    }

    public LocalDate getInvoiceDate() {
        java.util.Date date = (java.util.Date) spinDate.getValue();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public String getNotes() {
        return txtNotes.getText().trim();
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void addItem(InvoiceItem item) {
        items.add(item);
        updateItemsTable();
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            updateItemsTable();
        }
    }

    public Invoice getInvoice() {
        return invoice;
    }
}