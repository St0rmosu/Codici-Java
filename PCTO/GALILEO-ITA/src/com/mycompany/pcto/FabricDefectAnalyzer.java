package com.mycompany.pcto;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.util.Base64;
import com.fasterxml.jackson.databind.*;

/**
 * Classe core per l'analisi dei difetti nei tessuti
 * Progetto PCTO - NetBeans/Ant
 * 
 * @author PCTO Team
 * @version 2.0
 */
public class FabricDefectAnalyzer {
    
    // Configurazione Ollama
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String MODEL_GEMMA = "gemma3:4b";
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String[] SUPPORTED_FORMATS = {"jpg", "jpeg", "png", "bmp", "gif"};
    private static final int MAX_IMAGE_SIZE = 10 * 1024 * 1024;

    public static String analyzeImageForDefects(String imagePath, String fabricType) throws Exception {
        System.out.println("🔍 Caricamento immagine: " + imagePath);
        BufferedImage image = loadAndValidateImage(imagePath);
        System.out.println("🔄 Conversione immagine in formato base64...");
        String base64Image = imageToBase64(image);

        String prompt = createFabricAnalysisPrompt(fabricType, base64Image);

        System.out.println("🚀 Invio richiesta a Gemma3:4b");
        long gemmaStart = System.currentTimeMillis();
        String gemmaResult = sendRequestToModel(MODEL_GEMMA, prompt);
        long gemmaEnd = System.currentTimeMillis();
        System.out.printf("✅ Gemma3:4b completato in %.2f secondi%n", (gemmaEnd - gemmaStart) / 1000.0);

        return gemmaResult.trim();
    }

    private static BufferedImage loadAndValidateImage(String imagePath) throws IOException {
        imagePath = cleanPath(imagePath);
        File imageFile = new File(imagePath);

        if (!imageFile.exists()) {
            throw new FileNotFoundException("❌ Immagine non trovata: " + imagePath);
        }
        if (imageFile.length() > MAX_IMAGE_SIZE) {
            throw new IOException("❌ Immagine troppo grande. Massimo 10MB consentiti.");
        }
        String extension = getFileExtension(imagePath).toLowerCase();
        if (!Arrays.asList(SUPPORTED_FORMATS).contains(extension)) {
            throw new IOException("❌ Formato non supportato. Usa: " + Arrays.toString(SUPPORTED_FORMATS));
        }
        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("❌ Impossibile leggere l'immagine. File corrotto?");
        }
        System.out.printf("✅ Immagine caricata: %dx%d pixel, formato: %s%n", 
                image.getWidth(), image.getHeight(), extension.toUpperCase());
        return image;
    }

    private static String createFabricAnalysisPrompt(String fabricType, String base64Image) {
        return String.format(
            "Sei un esperto quality inspector specializzato nell'analisi di tessuti. " +
            "CERCA SPECIFICAMENTE:\n" +
            "• DIFETTI STRUTTURALI: strappi, buchi, tagli, usura eccessiva\n" +
            "• DIFETTI DI COLORE: macchie, scolorimenti, variazioni cromatiche anomale\n" +
            "• DEFORMAZIONI: pieghe permanenti, ondulazioni, distorsioni\n" +
            "• CONTAMINAZIONI: residui, pelucchi, corpi estranei\n\n" +
            "RISPONDI IN ITALIANO con un report dettagliato e professionale.\n\n" +
            "Immagine da analizzare: data:image/jpeg;base64,%s", 
            base64Image
        );
    }

    private static String sendRequestToModel(String model, String prompt) throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("model", model);
        request.put("prompt", prompt);
        request.put("stream", false);
        request.put("options", createMistralOptions());

        String jsonRequest = mapper.writeValueAsString(request);
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(30))
                .build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(OLLAMA_URL))
                .header("Content-Type", "application/json")
                .timeout(java.time.Duration.ofMinutes(5))
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Errore HTTP " + response.statusCode() + ": " + response.body());
        }
        return parseOllamaResponse(response.body());
    }

    private static Map<String, Object> createMistralOptions() {
        Map<String, Object> options = new HashMap<>();
        options.put("temperature", 0.3);
        options.put("top_p", 0.8);
        options.put("top_k", 40);
        return options;
    }

    private static String parseOllamaResponse(String jsonResponse) throws Exception {
        JsonNode root = mapper.readTree(jsonResponse);
        if (root.has("response")) {
            return root.get("response").asText();
        } else if (root.has("error")) {
            throw new RuntimeException("❌ Errore da Ollama: " + root.get("error").asText());
        } else {
            throw new RuntimeException("❌ Risposta inattesa da Ollama: " + jsonResponse);
        }
    }

    private static String imageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private static String cleanPath(String path) {
        if (path.startsWith("\"") && path.endsWith("\"")) {
            return path.substring(1, path.length() - 1);
        }
        return path.trim();
    }

    private static String getFileExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return (lastDot > 0) ? filename.substring(lastDot + 1) : "";
    }
}