package com.example.unbeatableproject;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FileOperationsTests {
    private FileOperations fileOps;
    private Stage primaryStage;

    @BeforeEach
    void setUp() {
        Platform.startup(() -> {
            primaryStage = new Stage(); // Create Stage instance on JavaFX application thread
            fileOps = new FileOperations(primaryStage, "testUser"); // Initialize FileOperations instance
        });
    }
    @Test
    void undoTest() {
        Platform.runLater(() -> {
        // Test Undo after Upload
        fileOps.getFileList().getItems().clear(); // Clear file list
        fileOps.uploadFiles(primaryStage); // Upload a file
        fileOps.undoLastAction(); // Undo upload
        assertFalse(fileOps.getFileList().getItems().contains("testFile.txt")); // Check if file is removed after undo
        });
    }
}