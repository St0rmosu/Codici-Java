package com.mycompany.pcto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Interfaccia grafica per l'analisi dei difetti nei tessuti
 * Progetto PCTO - NetBeans/Ant
 * 
 * @author PCTO Team
 * @version 2.0
 */
public class GuiInterface {
    
    private static JFrame frame;
    private static JLabel imageLabel;
    private static JTextArea resultArea;
    private static JButton analyzeButton;
    private static JComboBox<String> fabricTypeCombo;
    private static JLabel statusLabel;
    private static String selectedImagePath;
    
    public static void run() {
        SwingUtilities.invokeLater(() -> {
            try {
            } catch (Exception e) {
                // Usa il look and feel predefinito
            }
            createAndShowGUI();
        });
    }
    
    private static void createAndShowGUI() {
        frame = new JFrame("🧵 Analizzatore Difetti Tessuti - PCTO v2.0");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        
        // Listener per quando la finestra viene chiusa
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                System.out.println("\n🔄 Interfaccia grafica chiusa. Tornando al menu principale...\n");
            }
        });
        
        // Panel principale
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel centrale
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Panel sinistro - Controlli
        JPanel leftPanel = createControlPanel();
        centerPanel.add(leftPanel, BorderLayout.WEST);
        
        // Panel destro - Risultati
        JPanel rightPanel = createResultPanel();
        centerPanel.add(rightPanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private static JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(70, 130, 180));
        
        JLabel titleLabel = new JLabel("🧵 ANALIZZATORE DIFETTI TESSUTI");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("PCTO - Powered by Gemma3:4b AI");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.LIGHT_GRAY);
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(70, 130, 180));
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel);
        return headerPanel;
    }
    
    private static JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createTitledBorder("🔧 Controlli"));
        controlPanel.setPreferredSize(new Dimension(350, 0));
        
        // Selezione tipo tessuto
        JPanel fabricPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fabricPanel.add(new JLabel("Tipo Tessuto:"));
        fabricTypeCombo = new JComboBox<>(new String[]{
            "Cotone", "Lino", "Seta", "Lana", "Poliestere", 
            "Nylon", "Viscosa", "Misto", "Altro"
        });
        fabricPanel.add(fabricTypeCombo);
        controlPanel.add(fabricPanel);
        
        controlPanel.add(Box.createVerticalStrut(20));
        
        // Selezione immagine
        JButton selectImageButton = new JButton("📂 Seleziona Immagine");
        selectImageButton.setPreferredSize(new Dimension(200, 40));
        selectImageButton.addActionListener(new SelectImageListener());
        controlPanel.add(selectImageButton);
        
        controlPanel.add(Box.createVerticalStrut(10));
        
        // Anteprima immagine
        imageLabel = new JLabel("Nessuna immagine selezionata");
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        imageLabel.setPreferredSize(new Dimension(300, 200));
        imageLabel.setBackground(Color.LIGHT_GRAY);
        imageLabel.setOpaque(true);
        controlPanel.add(imageLabel);
        
        controlPanel.add(Box.createVerticalStrut(20));
        
        // Pulsante analisi
        analyzeButton = new JButton("🔍 Analizza Difetti");
        analyzeButton.setPreferredSize(new Dimension(200, 50));
        analyzeButton.setBackground(new Color(70, 130, 180));
        analyzeButton.setForeground(Color.WHITE);
        analyzeButton.setFont(new Font("Arial", Font.BOLD, 16));
        analyzeButton.setEnabled(false);
        analyzeButton.addActionListener(new AnalyzeListener());
        controlPanel.add(analyzeButton);
        
        controlPanel.add(Box.createVerticalGlue());
        
        return controlPanel;
    }
    
    private static JPanel createResultPanel() {
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("📊 Risultati Analisi"));
        
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setBackground(new Color(248, 248, 248));
        resultArea.setText("Seleziona un'immagine e clicca 'Analizza Difetti' per iniziare...");
        resultArea.setWrapStyleWord(true);
        resultArea.setLineWrap(true);
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        
        
        // Pulsanti azioni
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton clearButton = new JButton("🗑️ Pulisci");
        clearButton.addActionListener(e -> resultArea.setText(""));
        
        JButton saveButton = new JButton("💾 Salva Report");
        saveButton.addActionListener(new SaveReportListener());
        
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        resultPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return resultPanel;
    }
    
    private static JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        statusLabel = new JLabel("Pronto");
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        
        return statusPanel;
    }
    
    // Listener per selezione immagine
    private static class SelectImageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Immagini (*.jpg, *.jpeg, *.png, *.bmp, *.gif)", 
                "jpg", "jpeg", "png", "bmp", "gif"));
            
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedImagePath = selectedFile.getAbsolutePath();
                
                // Carica anteprima
                loadImagePreview(selectedFile);
                analyzeButton.setEnabled(true);
                statusLabel.setText("Immagine selezionata: " + selectedFile.getName());
            }
        }
    }
    
    // Listener per analisi
    private static class AnalyzeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedImagePath == null) {
                JOptionPane.showMessageDialog(frame, "Seleziona prima un'immagine!", 
                    "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Esegui analisi in background
            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    SwingUtilities.invokeLater(() -> {
                        analyzeButton.setEnabled(false);
                        statusLabel.setText("Analisi in corso... Attendere...");
                        resultArea.setText("🔄 Analizzando l'immagine con Gemma3:4b...\n" +
                                          "Questo potrebbe richiedere alcuni minuti...");
                    });
                    
                    String fabricType = (String) fabricTypeCombo.getSelectedItem();
                    return FabricDefectAnalyzer.analyzeImageForDefects(selectedImagePath, fabricType);
                }
                
                @Override
                protected void done() {
                    try {
                        String result = get();
                        resultArea.setText("✅ ANALISI COMPLETATA\n" +
                                          "================================\n\n" + result);
                        statusLabel.setText("Analisi completata con successo");
                    } catch (Exception ex) {
                        resultArea.setText("❌ ERRORE DURANTE L'ANALISI\n" +
                                          "================================\n\n" + 
                                          "Errore: " + ex.getMessage() + "\n\n" +
                                          "Suggerimenti:\n" +
                                          "• Verifica che Ollama sia in esecuzione\n" +
                                          "• Controlla che il modello Gemma3:4b sia installato\n" +
                                          "• Assicurati che l'immagine sia valida");
                        statusLabel.setText("Errore durante l'analisi");
                    } finally {
                        analyzeButton.setEnabled(true);
                    }
                }
            };
            worker.execute();
        }
    }
    
    // Listener per salvare report
    private static class SaveReportListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (resultArea.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nessun report da salvare!", 
                    "Avviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("report_analisi_tessuto.txt"));
            
            int result = fileChooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    java.nio.file.Files.write(
                        fileChooser.getSelectedFile().toPath(), 
                        resultArea.getText().getBytes()
                    );
                    JOptionPane.showMessageDialog(frame, "Report salvato con successo!", 
                        "Successo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Errore nel salvare il file: " + ex.getMessage(), 
                        "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private static void loadImagePreview(File imageFile) {
        try {
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image img = icon.getImage();
            
            // Ridimensiona per l'anteprima
            int maxWidth = 280;
            int maxHeight = 180;
            int width = img.getWidth(null);
            int height = img.getHeight(null);
            
            if (width > maxWidth || height > maxHeight) {
                double scale = Math.min((double) maxWidth / width, (double) maxHeight / height);
                width = (int) (width * scale);
                height = (int) (height * scale);
                img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            }
            
            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setText("");
        } catch (Exception ex) {
            imageLabel.setIcon(null);
            imageLabel.setText("Errore nel caricamento dell'immagine");
        }
    }
}
