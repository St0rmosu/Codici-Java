package com.mycompany.pcto;

import java.util.Scanner;

/**
 * Launcher principale con menu di selezione interfaccia
 * Progetto PCTO - NetBeans/Ant
 * 
 * @author PCTO Team
 * @version 2.0
 */
public class MainLauncher {
    
    public static void main(String[] args) {
        showWelcome();
        showMenu();
    }
    
    private static void showWelcome() {
        System.out.println("╔" + "═".repeat(60) + "╗");
        System.out.println("║" + centerText("🧵 ANALIZZATORE DIFETTI TESSUTI", 60) + "  ║");
        System.out.println("║" + centerText("PCTO - Progetto Alternanza Scuola-Lavoro", 60) + "║");
        System.out.println("║" + centerText("", 60) + "║");
        System.out.println("║" + centerText("🤖 Powered by Gemma3:4b AI Model", 60) + "  ║");
        System.out.println("║" + centerText("⚡ Tecnologie: Java + Ollama + Swing", 60) + "║");
        System.out.println("║" + centerText("", 60) + "║");
        System.out.println("║" + centerText("Versione 2.0 - Team PCTO", 60) + "║");
        System.out.println("╚" + "═".repeat(60) + "╝");
        System.out.println();
    }
    
    private static void showMenu() {
        try (Scanner scanner = new Scanner(System.in) // Assicura che il Scanner venga sempre chiuso
        ) {
            while (true) {
                System.out.println("🚀 SELEZIONA MODALITÀ DI UTILIZZO:");
                System.out.println();
                System.out.println("┌─ 1️⃣  Interfaccia Grafica (GUI)");
                System.out.println("│   └─ 🖥️  Esperienza visuale completa con Swing");
                System.out.println("│   └─ 📂 File browser integrato");
                System.out.println("│   └─ 🎨 Design moderno e intuitivo");
                System.out.println("│");
                System.out.println("├─ 2️⃣  Interfaccia Testuale (Console)");
                System.out.println("│   └─ ⌨️  Controllo completo da terminale");
                System.out.println("│   └─ 🚀 Prestazioni ottimali");
                System.out.println("│   └─ 📝 Output dettagliato");
                System.out.println("│");
                System.out.println("└─ 3️⃣  Esci");
                System.out.println();
                System.out.print("👉 Inserisci la tua scelta (1-3): ");
                
                String choice = scanner.nextLine().trim();
                
                switch (choice) {
                    case "1" -> {
                        System.out.println();
                        System.out.println("🎨 Avvio interfaccia grafica...");
                        System.out.println("✨ Caricamento componenti Swing...");
                        try {
                            Thread.sleep(1000); // Simula caricamento
                            // Commenta temporaneamente finché non crei GuiInterface
                            // GuiInterface.run();
                            System.out.println("🚧 Interfaccia grafica in sviluppo...");
                            GuiInterface.run();
                            System.out.println("🔄 Tornando al menu principale...");
                            System.out.println();
                        } catch (InterruptedException e) {
                            System.out.println("❌ Errore durante l'avvio dell'interfaccia grafica");
                            Thread.currentThread().interrupt(); // Ripristina il flag di interruzione
                        }
                    }
                        
                    case "2" -> {
                        System.out.println();
                        System.out.println("⌨️  Avvio interfaccia console...");
                        System.out.println("🚀 Modalità testuale attivata!");
                        System.out.println();
                        try {
                            Thread.sleep(800); // Breve pausa
                            // Commenta temporaneamente finché non crei ConsoleInterface
                            // ConsoleInterface.run();
                            System.out.println("🚧 Interfaccia console in sviluppo...");
                            System.out.println("🔄 Tornando al menu principale...");
                            System.out.println();
                            ConsoleInterface.run();
                        } catch (InterruptedException e) {
                            System.out.println("❌ Errore durante l'avvio dell'interfaccia console");
                            Thread.currentThread().interrupt(); // Ripristina il flag di interruzione
                        }
                    }
                        
                    case "3" -> {
                        System.out.println();
                        System.out.println("👋 Grazie per aver utilizzato l'Analizzatore Difetti Tessuti!");
                        return; // Esce dal metodo e termina il programma
                    }
                    default -> {
                        System.out.println();
                        System.out.println("❌ Scelta non valida! Inserisci 1, 2 o 3.");
                        System.out.println();
                    }
                }
            }
        }
    }
    
    private static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        
        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        
        // Padding sinistro
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        
        sb.append(text);
        
        // Padding destro per raggiungere la larghezza esatta
        while (sb.length() < width) {
            sb.append(" ");
        }
        
        return sb.toString();
    }
}