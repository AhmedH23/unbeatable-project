package com.example.unbeatableproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.List;

public class MainActivity extends Application {
    private int failedAttempts = 0;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        LoginBox loginBox = new LoginBox();
        AccountManager accountManagement = new AccountManager();
        root.getChildren().add(loginBox.getLoginBox());

        Label errorMessageLabel = new Label();
        errorMessageLabel.setTextFill(Color.RED);

        loginBox.getLoginButton().setOnAction(event -> {
            String enteredUsername = loginBox.getUsername().trim();
            String enteredPassword = loginBox.getPassword().trim();
            List<Account> accounts = accountManagement.loadAccounts(enteredUsername);
            boolean validLogin = accounts.stream().anyMatch(account -> account.getUsername().equals(enteredUsername) &&
                    account.getPassword().equals(enteredPassword));
            if (validLogin) {
                FileOperations fileOperations = new FileOperations(primaryStage, enteredUsername);
                root.getChildren().addAll(fileOperations.getFileList(), fileOperations.getUploadButton(),
                        fileOperations.getDeleteButton());
                fileOperations.getUploadButton().setDisable(false);
                fileOperations.getDeleteButton().setDisable(false);
                root.getChildren().remove(loginBox.getLoginBox());
                accountManagement.setupSignOut(root, loginBox, fileOperations);
            } else {
                failedAttempts++;
                errorMessageLabel.setText(failedAttempts >= 3 ? "Too many failed attempts. Your account is locked!" :
                        "Invalid username or password");
            }
        });
        root.getChildren().add(errorMessageLabel);
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setTitle("Cloud Storage App by Unbeatable Project");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}