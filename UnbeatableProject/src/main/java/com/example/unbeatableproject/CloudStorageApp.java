package com.example.unbeatableproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class CloudStorageApp extends Application {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private int failedAttempts = 0; // Remove 'final' to allow updates

    @Override
    public void start(Stage primaryStage) {
        LoginBox loginBox = new LoginBox();
        FileOperations fileOperations = new FileOperations(primaryStage, USERNAME);
        AccountManagement accountManagement = new AccountManagement();

        VBox root = new VBox(10);
        root.getChildren().add(loginBox.getLoginBox());

        Label errorMessageLabel = new Label();
        errorMessageLabel.setTextFill(Color.RED);

        loginBox.getLoginButton().setOnAction(event -> {
            String enteredUsername = loginBox.getUsername().trim(); // Trim to remove whitespace
            String enteredPassword = loginBox.getPassword().trim(); // Trim to remove whitespace

            // Load accounts
            List<Account> accounts = accountManagement.loadAccounts();

            // Check if the entered credentials match any account
            boolean validLogin = false;
            for (Account account : accounts) {
                if (account.getUsername().equals(enteredUsername) && account.getPassword().equals(enteredPassword)) {
                    validLogin = true;
                    break;
                }
            }

            if (validLogin) {
                // Successful login
                root.getChildren().addAll(fileOperations.getFileList(), fileOperations.getUploadButton(), fileOperations.getDeleteButton());
                fileOperations.getUploadButton().setDisable(false);
                fileOperations.getDeleteButton().setDisable(false);
                root.getChildren().remove(loginBox.getLoginBox());
                accountManagement.setupSignOut(root, loginBox, fileOperations);
            } else {
                // Failed login attempt
                failedAttempts++;
                if (failedAttempts >= 3) {
                    errorMessageLabel.setText("Too many failed attempts. Your account is locked!");
                } else {
                    errorMessageLabel.setText("Invalid username or password");
                }
            }
        });

        root.getChildren().add(errorMessageLabel);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cloud Storage App by Unbeatable Project");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}