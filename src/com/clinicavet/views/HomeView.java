package com.clinicavet.views;

import java.awt.*;
import javax.swing.*;

public class HomeView extends JPanel {

    private JLabel lblMedicos;
    private JLabel lblAuxiliares;
    private JLabel lblMascotas;
    private JLabel lblDuenos;

    public HomeView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(240, 245, 250));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- HEADER CON LOGO ---
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // --- PANEL DE STATS ---
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(240, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Logo y Título
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        logoPanel.setBackground(new Color(240, 245, 250));

        JLabel lblLogo = new JLabel("");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 48));
        logoPanel.add(lblLogo);

        JLabel lblTitle = new JLabel("CLÍNICA VETERINARIA");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(new Color(41, 128, 185));
        logoPanel.add(lblTitle);

        panel.add(logoPanel, BorderLayout.CENTER);

        // Subtítulo
        JLabel lblSubtitle = new JLabel("Sistema de Gestión Moderna");
        lblSubtitle.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblSubtitle.setForeground(new Color(127, 140, 141));
        lblSubtitle.setHorizontalAlignment(JLabel.CENTER);
        panel.add(lblSubtitle, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 25, 25));
        panel.setBackground(new Color(240, 245, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // CARD 1: Médicos Activos
        lblMedicos = createStatCardAndAdd(panel,
                "Médicos Activos",
                "0",
                new Color(52, 152, 219),
                new Color(236, 240, 241)
        );

        // CARD 2: Auxiliares Activos
        lblAuxiliares = createStatCardAndAdd(panel,
                "Auxiliares Activos",
                "0",
                new Color(46, 204, 113),
                new Color(236, 240, 241)
        );

        // CARD 3: Mascotas Creadas
        lblMascotas = createStatCardAndAdd(panel,
                "Mascotas Creadas",
                "0",
                new Color(241, 196, 15),
                new Color(236, 240, 241)
        );

        // CARD 4: Dueños Registrados
        lblDuenos = createStatCardAndAdd(panel,
                "Dueños Registrados",
                "0",
                new Color(155, 89, 182),
                new Color(236, 240, 241)
        );

        return panel;
    }

    private JLabel createStatCardAndAdd(JPanel parentPanel, String title, String value, Color accentColor, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout(15, 15));

        // Fondo
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 0, 0, accentColor),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        // Título
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setForeground(new Color(52, 73, 94));

        // Valor
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 40));
        lblValue.setForeground(accentColor);
        lblValue.setHorizontalAlignment(JLabel.CENTER);

        // Panel de contenido
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(bgColor);
        contentPanel.add(lblTitle, BorderLayout.NORTH);
        contentPanel.add(lblValue, BorderLayout.CENTER);

        card.add(contentPanel, BorderLayout.CENTER);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        parentPanel.add(card);

        return lblValue;  // Retornar el label del valor para poder actualizarlo
    }

    // Setters para actualizar valores
    public void setMedicosActivos(int count) {
        lblMedicos.setText(String.valueOf(count));
    }

    public void setAuxiliaresActivos(int count) {
        lblAuxiliares.setText(String.valueOf(count));
    }

    public void setMascotasCreadas(int count) {
        lblMascotas.setText(String.valueOf(count));
    }

    public void setDuenosRegistrados(int count) {
        lblDuenos.setText(String.valueOf(count));
    }
}