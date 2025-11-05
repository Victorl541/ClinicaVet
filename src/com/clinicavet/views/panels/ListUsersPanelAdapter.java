package com.clinicavet.views.panels;

import com.clinicavet.controllers.MainController;
import com.clinicavet.model.entities.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class ListUsersPanelAdapter extends JPanel {
    private final MainController controller;
    private JTable table;
    private DefaultTableModel model;

    public ListUsersPanelAdapter(MainController controller) {
        this.controller = controller;
        init();
        load();
        addAutoRefresh();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // ---------- ENCABEZADO ----------
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lbl = new JLabel("Listado de Usuarios", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(new Color(60, 63, 65));

        headerPanel.add(lbl, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // ---------- TABLA DE USUARIOS ----------
        model = new DefaultTableModel(new Object[]{"ID", "Nombre", "Email", "Rol", "Activo"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setFillsViewportHeight(true);

        // 🔹 Centrar todas las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < model.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        scroll.getViewport().setBackground(Color.WHITE);

        add(scroll, BorderLayout.CENTER);
    }

    private void load() {
        model.setRowCount(0);
        List<User> users = controller.listUsers();

        for (User u : users) {
            model.addRow(new Object[]{
                u.getId(),
                u.getName(),
                u.geteMail(),
                (u.getRol() != null ? u.getRol().getName() : "-"),
                (u.isActivo() ? "Sí" : "No")
            });
        }
    }

    public void addAutoRefresh() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                load();
            }
        });
    }
}
