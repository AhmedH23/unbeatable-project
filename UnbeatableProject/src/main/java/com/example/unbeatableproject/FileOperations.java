package com.example.unbeatableproject;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.*;

public class FileOperations {
    private final FileChooser fileChooser;
    private final ListView<String> fileList;
    private final Button uploadButton;
    private final Button deleteButton;
    private final Button downloadButton;
    private final Button undoButton;
    private final File uploadDirectory;
    private final Map<String, String> deletedFilesMap;
    private final Stack<UndoAction> undoStack;
    private final File recycleBinDirectory;

    public FileOperations(Stage primaryStage, String loggedInUsername) {
        fileChooser = new FileChooser();
        fileList = new ListView<>();
        uploadButton = new Button("Upload Files");
        deleteButton = new Button("Delete Files");
        downloadButton = new Button("Download Files");
        undoButton = new Button("Undo");
        uploadDirectory = new File("uploaded_files_" + loggedInUsername);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Download Directory");
        deletedFilesMap = new HashMap<>();
        undoStack = new Stack<>();

        recycleBinDirectory = new File("recycle_bin_" + loggedInUsername); // Initialize recycle bin directory
        if (!recycleBinDirectory.exists()) {
            recycleBinDirectory.mkdirs();
        }

        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        loadUploadedFiles();
        uploadButton.setOnAction(event -> uploadFiles(primaryStage));
        deleteButton.setOnAction(event -> deleteFiles());
        downloadButton.setOnAction(event -> downloadFiles());
        undoButton.setOnAction(event -> undoLastAction());
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
                    undoStack.push(new UndoAction(UndoAction.ActionType.UPLOAD, fileName));
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
            if (fileToDelete.exists()) {
                File recycleBinFile = new File(recycleBinDirectory, fileName);
                if (fileToDelete.renameTo(recycleBinFile)) { // Move file to recycle bin
                    fileList.getItems().remove(fileName);
                    deletedFilesMap.put(fileName, recycleBinFile.getAbsolutePath()); // Store recycle bin path
                    undoStack.push(new UndoAction(UndoAction.ActionType.DELETE, fileName));
                } else {
                    System.err.println("Failed to move file to recycle bin: " + fileName);
                }
            } else {
                System.err.println("File does not exist: " + fileName);
            }
        });
    }
    private void downloadFiles() {
        List<String> selectedFiles = new ArrayList<>(fileList.getSelectionModel().getSelectedItems());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        selectedFiles.forEach(fileName -> {
            File sourceFile = new File(uploadDirectory, fileName);
            fileChooser.setInitialFileName(fileName);
            File destinationFile = fileChooser.showSaveDialog(null);
            if (destinationFile != null) {
                try (InputStream in = new FileInputStream(sourceFile);
                     OutputStream out = new FileOutputStream(destinationFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void undoLastAction() {
        if (!undoStack.isEmpty()) {
            UndoAction lastAction = undoStack.pop();
            switch (lastAction.getActionType()) {
                case UPLOAD:
                    String uploadedFileName = lastAction.getFileName();
                    File uploadedFile = new File(uploadDirectory, uploadedFileName);
                    uploadedFile.delete();
                    fileList.getItems().remove(uploadedFileName);
                    break;
                case DELETE:
                    String deletedFileName = lastAction.getFileName();
                    String recycleBinPath = deletedFilesMap.get(deletedFileName);
                    if (recycleBinPath != null) {
                        File recycleBinFile = new File(recycleBinPath);
                        if (recycleBinFile.exists()) {
                            File restoredFile = new File(uploadDirectory, deletedFileName);
                            if (recycleBinFile.renameTo(restoredFile)) { // Restore file from recycle bin
                                fileList.getItems().add(deletedFileName);
                                deletedFilesMap.remove(deletedFileName);
                            } else {
                                System.err.println("Failed to restore file from recycle bin: " + deletedFileName);
                            }
                        } else {
                            System.err.println("File not found in recycle bin: " + deletedFileName);
                        }
                    } else {
                        System.err.println("Recycle bin path not found for file: " + deletedFileName);
                    }
                    break;
            }
        }
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
    public Button getDownloadButton() {
        return downloadButton;
    }
    public Button getUndoButton() {
        return undoButton;
    }
}
class UndoAction {
    public enum ActionType {UPLOAD, DELETE}
    private final ActionType actionType;
    private final String fileName;
    public UndoAction(ActionType actionType, String fileName) {
        this.actionType = actionType;
        this.fileName = fileName;
    }
    public ActionType getActionType() {
        return actionType;
    }
    public String getFileName() {
        return fileName;
    }
}