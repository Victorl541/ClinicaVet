package com.clinicavet.views;

import com.clinicavet.model.entities.Invoice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InvoicesListView extends JPanel {

    public JTable tableInvoices;
    public JButton btnSearch;
    public JButton btnNew;
    public JButton btnCancel;

    public InvoicesListView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // === TABLA CENTRAL ===
        tableInvoices = new JTable();
        tableInvoices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableInvoices.setRowHeight(26);
        tableInvoices.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tableInvoices);
        add(scrollPane, BorderLayout.CENTER);

        // === PANEL INFERIOR: BOTONES ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(Color.WHITE);

        btnSearch = new JButton("Buscar");
        btnNew = new JButton("Nueva Factura");
        btnCancel = new JButton("Anular");

        btnSearch.setPreferredSize(new Dimension(120, 35));
        btnNew.setPreferredSize(new Dimension(130, 35));
        btnCancel.setPreferredSize(new Dimension(100, 35));

        bottomPanel.add(btnSearch);
        bottomPanel.add(btnNew);
        bottomPanel.add(btnCancel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

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

        tableInvoices.setModel(model);
        tableInvoices.revalidate();
        tableInvoices.repaint();
    }

    public int getSelectedRow() {
        return tableInvoices.getSelectedRow();
    }
}