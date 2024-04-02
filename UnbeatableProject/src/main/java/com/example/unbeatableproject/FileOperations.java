package com.example.unbeatableproject;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileOperations {
    private final FileChooser fileChooser;
    private final ListView<String> fileList;
    private final Button uploadButton;
    private final Button deleteButton;
    private final File uploadDirectory;

    public FileOperations(Stage primaryStage, String loggedInUsername) {
        fileChooser = new FileChooser();
        fileList = new ListView<>();
        uploadButton = new Button("Upload File");
        deleteButton = new Button("Delete File");
        uploadDirectory = new File("uploaded_files_" + loggedInUsername);

        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        loadUploadedFiles();
        uploadButton.setOnAction(event -> uploadFiles(primaryStage));
        deleteButton.setOnAction(event -> deleteFiles());
    }
    private void uploadFiles(Stage primaryStage) {
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
        if (selectedFiles != null) {
            selectedFiles.forEach(file -> {
                String fileName = file.getName();
                File destinationFile = new File(uploadDirectory, fileName);
                try (InputStream in = new FileInputStream(file);
                     OutputStream out = new FileOutputStream(destinationFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    fileList.getItems().add(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    private void deleteFiles() {
        List<String> selectedFiles = new ArrayList<>(fileList.getSelectionModel().getSelectedItems());
        selectedFiles.forEach(fileName -> {
            File fileToDelete = new File(uploadDirectory, fileName);
            if (fileToDelete.exists() && fileToDelete.delete()) {
                fileList.getItems().remove(fileName);
            } else {
                System.err.println("Failed to delete file: " + fileName);
            }
        });
    }
    private void loadUploadedFiles() {
        File[] files = uploadDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                fileList.getItems().add(file.getName());
            }
        }
    }
    public ListView<String> getFileList() {
        fileList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        return fileList;
    }
    public Button getUploadButton() {
        return uploadButton;
    }
    public Button getDeleteButton() {
        return deleteButton;
    }
}
