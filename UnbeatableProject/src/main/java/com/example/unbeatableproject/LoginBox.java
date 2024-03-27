package com.example.unbeatableproject;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginBox {
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button loginButton;
    private final Button createAccountButton;

    public LoginBox() {
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        loginButton = new Button("Login");
        createAccountButton = new Button("Create an Account");

        createAccountButton.setOnAction(event -> {
            String username = "";
            String email = "";
            String password = "";
            createAccount(username, email, password);
        });
    }

    public void createAccount(String username, String email, String password) {
        Account newAccount = new Account(username, email, password);
        AccountManagement accountManagement = new AccountManagement();
        accountManagement.createAccount(newAccount);
    }

    public VBox getLoginBox() {
        VBox loginBox = new VBox(10, usernameField, passwordField, loginButton);
        loginBox.setPadding(new Insets(20));

        // Create an HBox to hold the "Create Account" button
        HBox createAccountBox = new HBox(createAccountButton);
        createAccountBox.setPadding(new Insets(5, 0, 0, 0));
        createAccountBox.setSpacing(10);

        // Add the "Create Account" button to the login box
        loginBox.getChildren().add(createAccountBox);

        return loginBox;
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getCreateAccountButton() {
        return createAccountButton;
    }
}