package com.mycompany.pcto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Interfaccia testuale per l'analizzatore di difetti nei tessuti
 * Progetto PCTO - NetBeans/Ant
 * 
 * @author PCTO Team
 * @version 2.0
 */
public class ConsoleInterface {
    
    public static void run() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("🧵 === ANALIZZATORE DIFETTI TESSUTI - VERSIONE CONSOLE ===");
        System.out.println("🤖 Modello AI: Gemma3:4b via Ollama\n");
        
        while (true) {
            try {
                System.out.print("📂 Inserisci il percorso dell'immagine (o 'exit' per uscire): ");
                String imagePath = scanner.nextLine().trim();
                
                if (imagePath.equalsIgnoreCase("exit")) {
                    System.out.println("👋 Arrivederci!");
                    break;
                }
                
                if (imagePath.isEmpty()) {
                    System.out.println("⚠️ Percorso vuoto. Riprova.");
                    continue;
                }
                
                System.out.print("🧵 Tipo di tessuto (cotone, lino, seta, lana, sintetico, etc.): ");
                String fabricType = scanner.nextLine().trim();
                
                if (fabricType.isEmpty()) {
                    fabricType = "generico";
                }
                
                System.out.println("\n⏳ Analisi in corso...");
                long startTime = System.currentTimeMillis();
                
                String result = FabricDefectAnalyzer.analyzeImageForDefects(imagePath, fabricType);
                
                long endTime = System.currentTimeMillis();
                System.out.println("\n" + "=".repeat(80));
                System.out.println("📋 REPORT ANALISI DIFETTI");
                System.out.println("=".repeat(80));
                System.out.println(result);
                System.out.println("=".repeat(80));
                System.out.printf("⏱️ Tempo elaborazione: %.2f secondi%n", (endTime - startTime) / 1000.0);
                System.out.println("=".repeat(80) + "\n");
                
            } catch (FileNotFoundException e) {
                System.out.println("❌ " + e.getMessage());
            } catch (IOException e) {
                System.out.println("❌ Errore I/O: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Errore durante l'analisi: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        scanner.close();
    }
}