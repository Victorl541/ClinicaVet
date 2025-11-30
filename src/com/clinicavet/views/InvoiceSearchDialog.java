package com.clinicavet.views;

import com.clinicavet.model.entities.Invoice;
import com.clinicavet.model.services.IInvoiceService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class InvoiceSearchDialog extends JDialog {

    public JTextField txtSearch;
    public JComboBox<String> cbStatus;
    public JSpinner spinDateFrom;
    public JSpinner spinDateTo;
    public JButton btnSearch;
    public JTable tableResults;
    public JButton btnClose;

    private List<Invoice> allInvoices;
    private IInvoiceService invoiceService;

    public InvoiceSearchDialog(JFrame parent, IInvoiceService invoiceService) {
        super(parent, "Búsqueda de Facturas", true);
        this.invoiceService = invoiceService;
        this.allInvoices = invoiceService.listInvoices();

        setSize(1000, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        initializeEmptyTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === PANEL SUPERIOR: FILTROS ===
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filtros de Búsqueda"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 1: Cliente y Estado
        gbc.gridx = 0;
        gbc.gridy = 0;
        filterPanel.add(new JLabel("Buscar cliente:"), gbc);

        txtSearch = new JTextField(20);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.4;
        filterPanel.add(txtSearch, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        filterPanel.add(new JLabel("Estado:"), gbc);

        cbStatus = new JComboBox<>(new String[]{"TODOS", "PENDIENTE", "PAGADA", "CANCELADA"});
        cbStatus.setPreferredSize(new Dimension(120, 25));
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.2;
        filterPanel.add(cbStatus, gbc);

        // Fila 2: Rango de fechas
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        filterPanel.add(new JLabel("Desde:"), gbc);

        spinDateFrom = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorFrom = new JSpinner.DateEditor(spinDateFrom, "yyyy-MM-dd");
        spinDateFrom.setEditor(editorFrom);
        spinDateFrom.setPreferredSize(new Dimension(140, 25));
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.4;
        filterPanel.add(spinDateFrom, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        filterPanel.add(new JLabel("Hasta:"), gbc);

        spinDateTo = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorTo = new JSpinner.DateEditor(spinDateTo, "yyyy-MM-dd");
        spinDateTo.setEditor(editorTo);
        spinDateTo.setPreferredSize(new Dimension(140, 25));
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.2;
        filterPanel.add(spinDateTo, gbc);

        // Botón búsqueda
        btnSearch = new JButton("Buscar");
        btnSearch.setPreferredSize(new Dimension(120, 28));
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        filterPanel.add(btnSearch, gbc);

        mainPanel.add(filterPanel, BorderLayout.NORTH);

        // === TABLA CENTRAL (VACÍA INICIALMENTE) ===
        tableResults = new JTable();
        tableResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableResults.setRowHeight(26);
        tableResults.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tableResults);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // === PANEL INFERIOR: BOTONES ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnClose = new JButton("✕ Cerrar");

        btnClose.setPreferredSize(new Dimension(100, 35));

        bottomPanel.add(btnClose);

        btnClose.addActionListener(e -> dispose());

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Inicializar tabla vacía sin datos
     */
    private void initializeEmptyTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Factura", "Cliente", "Fecha", "Total", "Estado"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableResults.setModel(model);
        System.out.println("Tabla inicializada vacía - esperando búsqueda");
    }

    /**
     * Mostrar resultados en la tabla
     */
    public void setTableData(List<Invoice> invoices) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Factura", "Cliente", "Fecha", "Total", "Estado"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Invoice inv : invoices) {
            model.addRow(new Object[]{
                    inv.getInvoiceNumber(),
                    inv.getClient() != null ? inv.getClient().getName() : "Sin cliente",
                    inv.getInvoiceDate(),
                    String.format("$%,.2f", inv.getTotal()),
                    inv.getStatus().toString()
            });
        }

        tableResults.setModel(model);
        tableResults.revalidate();
        tableResults.repaint();
    }

    // === GETTERS ===

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

    public int getSelectedRow() {
        return tableResults.getSelectedRow();
    }

    public void setDefaultDateRange(LocalDate from, LocalDate to) {
        spinDateFrom.setValue(java.sql.Date.valueOf(from));
        spinDateTo.setValue(java.sql.Date.valueOf(to));
    }
}