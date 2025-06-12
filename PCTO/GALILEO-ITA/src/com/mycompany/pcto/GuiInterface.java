/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.pcto;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.util.Base64;
import com.fasterxml.jackson.databind.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author lollo
 */
public class GuiInterface extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GuiInterface.class.getName());

    static void run() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    // Componenti UI
    private JTextField imagePathField;
    private JComboBox<String> fabricTypeCombo;
    private JTextArea resultArea;
    private JButton analyzeButton;
    private JButton browseButton;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    private JLabel imagePreviewLabel;
    
    /**
     * Creates new form GuiInterface
     */
    public GuiInterface() {
        initComponents();
        initCustomComponents();
        setupLayout();
        setupEventHandlers();
    }
    
        private void initCustomComponents() {
        // Inizializzazione componenti personalizzati
        imagePathField = new JTextField(30);
        browseButton = new JButton("Sfoglia");
        
        String[] fabricTypes = {"generico", "cotone", "lino", "seta", "lana", "sintetico", "jeans", "poliestere"};
        fabricTypeCombo = new JComboBox<>(fabricTypes);
        
        analyzeButton = new JButton("Analizza Tessuto");
        analyzeButton.setEnabled(false);
        
        resultArea = new JTextArea(15, 50);
        resultArea.setEditable(false);
        
        statusLabel = new JLabel("Pronto per l'analisi");
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setPreferredSize(new Dimension(200, 200));
        imagePreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private void setupLayout() {
        // Rimuovi il layout generato automaticamente
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout(10, 10));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel titleLabel = new JLabel("ANALIZZATORE DIFETTI TESSUTI", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Pannello input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Percorso immagine:"), gbc);
        
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        inputPanel.add(imagePathField, gbc);
        
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        inputPanel.add(browseButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Tipo tessuto:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        inputPanel.add(fabricTypeCombo, gbc);
        
        // Pannello anteprima immagine
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.add(new JLabel("Anteprima immagine:"), BorderLayout.NORTH);
        previewPanel.add(imagePreviewLabel, BorderLayout.CENTER);
        
        // Pannello inferiore
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(analyzeButton, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        bottomPanel.add(progressBar, BorderLayout.NORTH);
        
        // Aggiungi tutto al pannello principale
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Aggiungi i pannelli al frame
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(previewPanel, BorderLayout.EAST);
        
        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void setupEventHandlers() {
        browseButton.addActionListener(e -> browseImage());
        analyzeButton.addActionListener(e -> analyzeImage());
    }

    private void browseImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Immagini", "jpg", "jpeg", "png", "gif", "bmp"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            imagePathField.setText(selectedFile.getAbsolutePath());
            updateImagePreview(selectedFile);
            analyzeButton.setEnabled(true);
        }
    }

    private void updateImagePreview(File imageFile) {
        try {
            BufferedImage img = ImageIO.read(imageFile);
            if (img != null) {
                ImageIcon icon = new ImageIcon(img.getScaledInstance(
                    imagePreviewLabel.getWidth(), imagePreviewLabel.getHeight(), Image.SCALE_SMOOTH));
                imagePreviewLabel.setIcon(icon);
            }
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Errore caricamento immagine", ex);
            JOptionPane.showMessageDialog(this, "Errore nel caricamento dell'immagine", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void analyzeImage() {
        String imagePath = imagePathField.getText();
        String fabricType = (String) fabricTypeCombo.getSelectedItem();
        
        // Disabilita i controlli durante l'analisi
        analyzeButton.setEnabled(false);
        browseButton.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        statusLabel.setText("Analisi in corso...");
        
        // Esegui l'analisi in un thread separato
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                try {
                    // 1. Converti l'immagine in base64
                    String imageBase64 = encodeImageToBase64(imagePath);
                    
                    // 2. Prepara la richiesta all'API AI
                    Map<String, Object> requestData = new HashMap<>();
                    requestData.put("image", imageBase64);
                    requestData.put("fabric_type", fabricType);
                    
                    // 3. Invia la richiesta (esempio con HttpClient)
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.gemma-ai.com/analyze"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(requestData)))
                        .build();
                    
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    
                    // 4. Elabora la risposta
                    if (response.statusCode() == 200) {
                        return parseAnalysisResult(response.body());
                    } else {
                        throw new IOException("Errore API: " + response.statusCode());
                    }
                } catch (IOException | InterruptedException ex) {
                    logger.log(java.util.logging.Level.SEVERE, "Errore analisi", ex);
                    throw ex;
                }
            }
            
            @Override
            protected void done() {
                try {
                    String result = get();
                    resultArea.setText(result);
                    statusLabel.setText("Analisi completata");
                } catch (InterruptedException | ExecutionException ex) {
                    resultArea.setText("Errore: " + ex.getMessage());
                    statusLabel.setText("Errore nell'analisi");
                } finally {
                    analyzeButton.setEnabled(true);
                    browseButton.setEnabled(true);
                    progressBar.setVisible(false);
                }
            }
        }.execute();
    }
    
    private String encodeImageToBase64(String imagePath) throws IOException {
        byte[] fileContent = Files.readAllBytes(Paths.get(imagePath));
        return Base64.getEncoder().encodeToString(fileContent);
    }
    
    private String parseAnalysisResult(String jsonResponse) throws IOException {
        // Parsing della risposta JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonResponse);
        
        // Costruisci il report (adatta alla risposta effettiva dell'API)
        StringBuilder report = new StringBuilder();
        report.append("RISULTATO ANALISI\n");
        report.append("================\n\n");
        report.append("Tipo tessuto: ").append(root.path("fabric_type").asText()).append("\n");
        report.append("Difetti rilevati: ").append(root.path("defects_count").asInt()).append("\n\n");
        
        JsonNode defects = root.path("defects");
        for (JsonNode defect : defects) {
            report.append("- ").append(defect.path("type").asText())
                 .append(": ").append(defect.path("description").asText())
                 .append(" (gravità: ").append(defect.path("severity").asText()).append(")\n");
        }
        
        return report.toString();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        EventQueue.invokeLater(() -> {
    GuiInterface frame = new GuiInterface();
    frame.setTitle("Analizzatore Difetti Tessuti - PCTO v2.0");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
}); // Aggiungi questa parentesi e punto e virgola
    }

    // Variables declaration - do not modify                     
    // End of variables declaration                   
}
