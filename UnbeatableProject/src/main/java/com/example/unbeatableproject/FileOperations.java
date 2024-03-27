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
    private final String loggedInUsername;

    public FileOperations(Stage primaryStage, String loggedInUsername) {
        fileChooser = new FileChooser();
        fileList = new ListView<>();
        uploadButton = new Button("Upload File");
        deleteButton = new Button("Delete File");
        uploadDirectory = new File("uploaded_files");
        this.loggedInUsername = loggedInUsername;

        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        loadUserFiles();

        uploadButton.setOnAction(event -> {
            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
            if (selectedFiles != null) {
                for (File file : selectedFiles) {
                    String fileName = file.getName();
                    String destinationPath = uploadDirectory.getAbsolutePath() + File.separator + fileName;
                    File destinationFile = new File(destinationPath);
                    try (InputStream in = new FileInputStream(file); OutputStream out = new
                            FileOutputStream(destinationFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = in.read(buffer)) > 0) {
                            out.write(buffer, 0, length);
                        }
                        fileList.getItems().add(fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Handle file copying error
                    }
                }
            }
        });

        deleteButton.setOnAction(event -> {
            List<String> selectedFiles = new ArrayList<>(fileList.getSelectionModel().getSelectedItems());
            for (String fileName : selectedFiles) {
                File fileToDelete = new File(uploadDirectory, fileName);
                if (fileToDelete.exists()) {
                    if (fileToDelete.delete()) {
                        fileList.getItems().remove(fileName);
                    } else {
                        System.err.println("Failed to delete file: " + fileName);
                    }
                }
            }
        });
    }

    private void loadUserFiles() {
        File userDirectory = new File(uploadDirectory, loggedInUsername);
        File[] files = userDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                fileList.getItems().add(file.getName());
            }
        }
    }

    /*private void loadUploadedFiles() {
        File[] userFiles = getUserFiles(); // Get files associated with the currently logged-in user
        if (userFiles != null) {
            for (File file : userFiles) {
                fileList.getItems().add(file.getName());
            }
        }
    }*/
    /*private File[] getUserFiles() {
        File userDirectory = new File(uploadDirectory, loggedInUsername); // User-specific directory
        return userDirectory.listFiles();
    }*/

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