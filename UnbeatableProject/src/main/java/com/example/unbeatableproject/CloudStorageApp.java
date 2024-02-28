package com.example.unbeatableproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CloudStorageApp extends Application {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";
    private int failedAttempts = 0;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button loginButton = new Button("Login");

        VBox loginBox = new VBox(10, usernameField, passwordField, loginButton);
        loginBox.setPadding(new Insets(20));
        root.getChildren().add(loginBox);

        FileChooser fileChooser = new FileChooser();
        Button uploadButton = new Button("Upload File");
        Button deleteButton = new Button("Delete File");
        Button createAccountButton = new Button("Create Account");
        Button helpButton = new Button("Help");
        ListView<String> fileList = new ListView<>();
        fileList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        uploadButton.setDisable(true);
        deleteButton.setDisable(true);

        Label errorMessageLabel = new Label();
        errorMessageLabel.setTextFill(Color.RED);

        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.equals(USERNAME) && password.equals(PASSWORD)) {
                root.getChildren().remove(loginBox);
                root.getChildren().addAll(fileList, uploadButton, deleteButton);
                uploadButton.setDisable(false);
                deleteButton.setDisable(false);

                // Replace Create Account button with Sign Out button
                Button signOutButton = new Button("Sign Out");
                signOutButton.setOnAction(e -> {
                    // Reset UI and go back to home page
                    root.getChildren().removeAll(signOutButton, fileList, uploadButton, deleteButton);
                    root.getChildren().add(loginBox);
                    uploadButton.setDisable(true);
                    deleteButton.setDisable(true);
                    usernameField.clear();
                    passwordField.clear();
                });
                root.getChildren().remove(createAccountButton);
                root.getChildren().add(signOutButton);
            } else {
                failedAttempts++;
                if (failedAttempts >= 3) {
                    errorMessageLabel.setText("Too many failed attempts. Your account is locked!");
                    //failedAttempts = 0; // Reset failed attempts counter
                } else {
                    errorMessageLabel.setText("Invalid username or password");
                }
            }
        });

        uploadButton.setOnAction(event -> {
            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
            if (selectedFiles != null) {
                for (File file : selectedFiles) {
                    fileList.getItems().add(file.getName());
                }
            }
        });

        deleteButton.setOnAction(event -> {
            List<String> selectedFiles = new ArrayList<>(fileList.getSelectionModel().getSelectedItems());
            fileList.getItems().removeAll(selectedFiles);
        });

        helpButton.setOnAction(event -> {
            // Display help content or perform help action
            System.out.println("Help button clicked.");
        });

        createAccountButton.setOnAction(event -> {
            // Create account dialog
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Create Account");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField newUsernameField = new TextField();
            newUsernameField.setPromptText("Username");
            TextField emailField = new TextField();
            emailField.setPromptText("Email");
            PasswordField newPasswordField = new PasswordField();
            newPasswordField.setPromptText("Password");

            grid.add(new Label("Username:"), 0, 0);
            grid.add(newUsernameField, 1, 0);
            grid.add(new Label("Email:"), 0, 1);
            grid.add(emailField, 1, 1);
            grid.add(new Label("Password:"), 0, 2);
            grid.add(newPasswordField, 1, 2);

            dialog.getDialogPane().setContent(grid);

            ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == createButtonType) {
                    String newUsername = newUsernameField.getText();
                    String email = emailField.getText();
                    String newPassword = newPasswordField.getText();

                    if (newUsername.isEmpty() || email.isEmpty() || newPassword.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText(null);
                        alert.setContentText("Please fill in all fields.");
                        alert.showAndWait();
                    } else {
                        // Account creation logic
                        System.out.println("New account created:");
                        System.out.println("Username: " + newUsername);
                        System.out.println("Email: " + email);
                        System.out.println("Password: " + newPassword);
                    }
                }
                return null;
            });

            dialog.showAndWait();
        });

        VBox buttonBox = new VBox(10, helpButton, createAccountButton);
        root.getChildren().add(buttonBox);

        VBox errorBox = new VBox(errorMessageLabel);
        root.getChildren().add(errorBox);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cloud Storage");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}