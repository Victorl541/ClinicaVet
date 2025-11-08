package com.clinicavet.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// Utilidad para leer/escribir JSON sin librerías externas
public class JsonHelper {
    
    private static final String DATA_DIR = "data";
    
    // Lee archivo JSON desde data/
    public static String readJsonFile(String filename) throws IOException {
        Path path = Paths.get(DATA_DIR, filename);
        if (!Files.exists(path)) {
            return null;
        }
        return Files.readString(path, StandardCharsets.UTF_8);
    }
    
    // Escribe contenido JSON a data/
    public static void writeJsonFile(String filename, String jsonContent) throws IOException {
        Path dirPath = Paths.get(DATA_DIR);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        Path filePath = Paths.get(DATA_DIR, filename);
        Files.writeString(filePath, jsonContent, StandardCharsets.UTF_8);
    }
    
    // Escapa string para JSON
    public static String escapeJson(String str) {
        if (str == null) return "null";
        return "\"" + str
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t") + "\"";
    }
    
    // Desescapa string de JSON
    public static String unescapeJson(String str) {
        if (str == null || str.equals("null")) return null;
        if (str.startsWith("\"") && str.endsWith("\"")) {
            str = str.substring(1, str.length() - 1);
        }
        return str
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t");
    }
    
    
    public static String toJson(boolean value) {
        return String.valueOf(value);
    }
    
    public static String toJson(int value) {
        return String.valueOf(value);
    }
    
    public static String toJson(double value) {
        return String.valueOf(value);
    }
}
