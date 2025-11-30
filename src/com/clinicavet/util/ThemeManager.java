package com.clinicavet.util;

import java.awt.*;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;

/**
 * Gestor de temas responsivo y multiplataforma
 * Detecta automáticamente el sistema operativo y aplica el tema óptimo
 */
public class ThemeManager {
    
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SECONDARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color LIGHT_BG = new Color(250, 250, 250);
    private static final Color DARK_TEXT = new Color(44, 62, 80);
    
    private static String detectedOS;
    
    /**
     * Detecta el sistema operativo
     */
    private static String detectOS() {
        if (detectedOS == null) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                detectedOS = "windows";
            } else if (os.contains("mac")) {
                detectedOS = "macos";
            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                detectedOS = "linux";
            } else {
                detectedOS = "other";
            }
        }
        return detectedOS;
    }
    
    /**
     * Aplica el tema óptimo según el sistema operativo
     */
    public static void applyTheme() {
        String os = detectOS();
        System.out.println("Sistema operativo detectado: " + os);
        
        // Intentar FlatLaf primero (moderno y multiplataforma)
        if (tryApplyFlatLaf()) {
            System.out.println("FlatLaf aplicado exitosamente");
            applyCommonSettings();
            return;
        }
        
        // Si FlatLaf no está disponible, usar tema nativo
        System.out.println("FlatLaf no disponible, usando tema nativo...");
        try {
            switch (os) {
                case "windows":
                    applyWindowsTheme();
                    break;
                case "macos":
                    applyMacOSTheme();
                    break;
                case "linux":
                    applyLinuxTheme();
                    break;
                default:
                    applyDefaultTheme();
                    break;
            }
            
            // Aplicar configuraciones comunes multiplataforma
            applyCommonSettings();
            
            System.out.println("Tema aplicado exitosamente para " + os);
            
        } catch (Exception e) {
            System.err.println("Error al aplicar tema: " + e.getMessage());
            applyFallbackTheme();
        }
    }
    
    /**
     * Intenta aplicar FlatLaf (tema moderno multiplataforma)
     * @return true si se aplicó correctamente, false si no está disponible
     */
    private static boolean tryApplyFlatLaf() {
        try {
            // Intentar cargar FlatLaf Light
            Class.forName("com.formdev.flatlaf.FlatLightLaf");
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
            
            // Configuraciones específicas de FlatLaf
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("CheckBox.arc", 5);
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("Component.innerFocusWidth", 1);
            UIManager.put("ScrollBar.width", 12);
            UIManager.put("ScrollBar.thumbArc", 6);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
            
            // Colores personalizados para FlatLaf
            UIManager.put("Button.default.background", PRIMARY_COLOR);
            UIManager.put("Button.default.foreground", Color.WHITE);
            UIManager.put("Component.accentColor", PRIMARY_COLOR);
            
            // CRÍTICO: Configurar botones de diálogos para que sean visibles desde el inicio
            UIManager.put("Button.foreground", PRIMARY_COLOR);
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("Button.startBackground", Color.WHITE);
            UIManager.put("Button.endBackground", Color.WHITE);
            UIManager.put("Button.focusedBackground", new Color(230, 240, 250));
            UIManager.put("Button.hoverBackground", new Color(230, 240, 250));
            UIManager.put("Button.pressedBackground", new Color(200, 220, 240));
            UIManager.put("Button.selectedBackground", PRIMARY_COLOR);
            UIManager.put("Button.selectedForeground", Color.WHITE);
            UIManager.put("Button.disabledBackground", new Color(245, 245, 245));
            UIManager.put("Button.disabledText", new Color(150, 150, 150));
            UIManager.put("Button.default.borderColor", PRIMARY_COLOR);
            UIManager.put("Button.default.focusedBorderColor", PRIMARY_COLOR);
            UIManager.put("Button.borderColor", PRIMARY_COLOR);
            UIManager.put("Button.focusedBorderColor", PRIMARY_COLOR);
            UIManager.put("Button.hoverBorderColor", SECONDARY_COLOR);
            
            // Configuración de OptionPane
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("OptionPane.messageForeground", DARK_TEXT);
            UIManager.put("OptionPane.buttonMinimumWidth", 80);
            UIManager.put("OptionPane.buttonPadding", new Insets(5, 15, 5, 15));
            
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("FlatLaf no encontrado en el classpath");
            System.out.println("Para usar FlatLaf, ejecuta con: java -cp \"bin;lib/*\" com.clinicavet.App");
            return false;
        } catch (Exception e) {
            System.err.println("Error al cargar FlatLaf: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Tema optimizado para Windows
     */
    private static void applyWindowsTheme() throws Exception {
        // Intentar usar el tema nativo de Windows
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        // Ajustes específicos para Windows
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(2, 2, 2, 2));
    }
    
    /**
     * Tema optimizado para macOS
     */
    private static void applyMacOSTheme() throws Exception {
        // Usar el tema Aqua nativo de macOS
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        // Configuraciones específicas para macOS
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.name", "Clínica Veterinaria");
    }
    
    /**
     * Tema optimizado para Linux
     */
    private static void applyLinuxTheme() throws Exception {
        // Para Linux, usar Metal con personalizaciones
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        
        // Colores claros y visibles
        UIManager.put("control", new Color(240, 240, 240));
        UIManager.put("info", new Color(240, 240, 240));
        UIManager.put("nimbusBase", new Color(240, 240, 240));
        UIManager.put("nimbusAlertYellow", WARNING_COLOR);
        UIManager.put("nimbusBorder", new Color(150, 150, 150));
        UIManager.put("nimbusDisabledText", new Color(100, 100, 100));
        UIManager.put("nimbusFocus", PRIMARY_COLOR);
        UIManager.put("nimbusGreen", SUCCESS_COLOR);
        UIManager.put("nimbusInfoBlue", PRIMARY_COLOR);
        UIManager.put("nimbusLightBackground", LIGHT_BG);
        UIManager.put("nimbusPrimary", PRIMARY_COLOR);
        UIManager.put("nimbusRed", DANGER_COLOR);
        UIManager.put("nimbusSelectedText", Color.WHITE);
        UIManager.put("nimbusSelectionBackground", PRIMARY_COLOR);
        
        // Fuentes específicas para Linux
        Font defaultFont = new Font("DejaVu Sans", Font.PLAIN, 12);
        Font boldFont = new Font("DejaVu Sans", Font.BOLD, 12);
        Font menuFont = new Font("DejaVu Sans", Font.PLAIN, 11);
        
        UIManager.put("Label.font", defaultFont);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("TextArea.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("Menu.font", boldFont);
        UIManager.put("MenuItem.font", menuFont);
    }
    
    /**
     * Tema por defecto para sistemas no reconocidos
     */
    private static void applyDefaultTheme() throws Exception {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }
    
    /**
     * Tema de respaldo en caso de error
     */
    private static void applyFallbackTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            System.out.println("Tema de respaldo aplicado");
        } catch (Exception ex) {
            System.err.println("No se pudo aplicar ningún tema: " + ex.getMessage());
        }
    }
    
    /**
     * Configuraciones comunes para todos los sistemas operativos
     * Hace la interfaz responsiva y consistente
     */
    private static void applyCommonSettings() {
        // Colores de texto
        UIManager.put("text", DARK_TEXT);
        UIManager.put("textBackground", Color.WHITE);
        UIManager.put("textForeground", DARK_TEXT);
        UIManager.put("textHighlight", PRIMARY_COLOR);
        UIManager.put("textHighlightText", Color.WHITE);
        UIManager.put("textInactiveText", new Color(100, 100, 100));
        
        // Menús con diseño consistente
        UIManager.put("Menu.background", PRIMARY_COLOR);
        UIManager.put("Menu.foreground", Color.WHITE);
        UIManager.put("MenuItem.background", Color.WHITE);
        UIManager.put("MenuItem.foreground", DARK_TEXT);
        UIManager.put("MenuItem.selectionBackground", PRIMARY_COLOR);
        UIManager.put("MenuItem.selectionForeground", Color.WHITE);
        UIManager.put("MenuBar.background", PRIMARY_COLOR);
        UIManager.put("MenuBar.foreground", Color.WHITE);
        UIManager.put("PopupMenu.background", Color.WHITE);
        UIManager.put("PopupMenu.foreground", DARK_TEXT);
        UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
        
        // Botones responsivos
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", PRIMARY_COLOR);
        UIManager.put("Button.select", SECONDARY_COLOR);
        UIManager.put("Button.focus", new Color(0, 0, 0, 50));
        UIManager.put("Button.border", BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
        
        // Configuración específica para botones de JOptionPane (Yes, No, OK, Cancel)
        UIManager.put("OptionPane.buttonMinimumWidth", 80);
        UIManager.put("OptionPane.buttonPadding", new Insets(5, 15, 5, 15));
        UIManager.put("OptionPane.messageForeground", DARK_TEXT);
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 12));
        
        // Asegurar que los textos de los botones sean visibles en todos los estados
        UIManager.put("Button.disabledText", new Color(150, 150, 150));
        
        // Tablas
        UIManager.put("Table.selectionBackground", PRIMARY_COLOR);
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("Table.gridColor", new Color(200, 200, 200));
        
        // Scrollbars más modernos
        UIManager.put("ScrollBar.width", 12);
        UIManager.put("ScrollBar.thumb", new Color(180, 180, 180));
        UIManager.put("ScrollBar.thumbHighlight", PRIMARY_COLOR);
        
        // ComboBox
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.selectionBackground", PRIMARY_COLOR);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        
        // TextField y TextArea
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", DARK_TEXT);
        UIManager.put("TextField.selectionBackground", PRIMARY_COLOR);
        UIManager.put("TextField.selectionForeground", Color.WHITE);
        
        // Bordes más suaves
        UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        
        // Tooltips
        UIManager.put("ToolTip.background", new Color(255, 255, 220));
        UIManager.put("ToolTip.foreground", DARK_TEXT);
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(new Color(150, 150, 150), 1));
    }
    
    /**
     * Establece una escala de fuente global para responsividad
     * @param scale Factor de escala (1.0 = normal, 1.2 = 20% más grande, etc.)
     */
    public static void setFontScale(float scale) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            
            if (value instanceof FontUIResource) {
                FontUIResource originalFont = (FontUIResource) value;
                float newSize = originalFont.getSize() * scale;
                FontUIResource scaledFont = new FontUIResource(
                    originalFont.getName(),
                    originalFont.getStyle(),
                    Math.round(newSize)
                );
                UIManager.put(key, scaledFont);
            }
        }
        System.out.println("Escala de fuente aplicada: " + scale);
    }
    
    /**
     * Detecta la resolución de pantalla y ajusta la escala automáticamente
     */
    public static void applyResponsiveScale() {
        try {
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            int screenWidth = gd.getDisplayMode().getWidth();
            int screenHeight = gd.getDisplayMode().getHeight();
            
            System.out.println("Resolución de pantalla: " + screenWidth + "x" + screenHeight);
            
            // Ajustar escala según resolución
            float scale = 1.0f;
            if (screenWidth >= 3840) { // 4K
                scale = 1.3f;
            } else if (screenWidth >= 2560) { // QHD
                scale = 1.15f;
            } else if (screenWidth <= 1366) { // HD
                scale = 0.95f;
            }
            
            if (scale != 1.0f) {
                setFontScale(scale);
                System.out.println("Escala responsiva aplicada: " + scale);
            }
        } catch (Exception e) {
            System.err.println("Error al aplicar escala responsiva: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene el color primario del tema
     */
    public static Color getPrimaryColor() {
        return PRIMARY_COLOR;
    }
    
    /**
     * Obtiene el color secundario del tema
     */
    public static Color getSecondaryColor() {
        return SECONDARY_COLOR;
    }
    
    /**
     * Obtiene el color de éxito
     */
    public static Color getSuccessColor() {
        return SUCCESS_COLOR;
    }
    
    /**
     * Obtiene el color de advertencia
     */
    public static Color getWarningColor() {
        return WARNING_COLOR;
    }
    
    /**
     * Obtiene el color de peligro/error
     */
    public static Color getDangerColor() {
        return DANGER_COLOR;
    }
}
