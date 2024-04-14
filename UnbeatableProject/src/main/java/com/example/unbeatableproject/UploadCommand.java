package com.example.unbeatableproject;

import javafx.stage.Stage;

public class UploadCommand implements Command {
    private final FileOperations fileOperations;
    private final Stage primaryStage;
    public UploadCommand(FileOperations fileOperations, Stage primaryStage) {
        this.fileOperations = fileOperations;
        this.primaryStage = primaryStage;
    }
    @Override
    public void execute() {
        fileOperations.uploadFiles(primaryStage);
    }
}