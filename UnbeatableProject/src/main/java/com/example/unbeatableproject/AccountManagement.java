package com.example.unbeatableproject;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AccountManagement {
    private static final String ACCOUNTS_DIRECTORY = "accounts";
    private static final String ACCOUNTS_FILE = ACCOUNTS_DIRECTORY + File.separator + "accounts.txt";

    public void setupSignOut(VBox root, LoginBox loginBox, FileOperations fileOperations) {
        root.getChildren().removeIf(node -> node instanceof Button && ((Button) node).getText().equals("Sign Out"));
        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(event -> {
            root.getChildren().removeAll(signOutButton, fileOperations.getFileList(), fileOperations.getUploadButton(),
                    fileOperations.getDeleteButton());
            root.getChildren().add(loginBox.getLoginBox());
            fileOperations.getUploadButton().setDisable(true);
            fileOperations.getDeleteButton().setDisable(true);
            loginBox.getUsername();
            loginBox.getPassword();
        });
        root.getChildren().remove(loginBox.getCreateAccountButton());
        root.getChildren().add(signOutButton);
    }

    public void createAccount(Account account) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Create an Account");
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
                    showAlert();
                } else {
                    persistAccount(newUsername, email, newPassword);
                }
            }
            return null;
        });
        dialog.showAndWait();
        persistAccount(account.getUsername(), account.getEmail(), account.getPassword());
    }

    private void persistAccount(String username, String email, String password) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(ACCOUNTS_FILE, true)))) {
            writer.println(username + "," + email + "," + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Account> loadAccounts(String loggedInUsername) {
        List<Account> accounts = new ArrayList<>();
        File directory = new File(ACCOUNTS_DIRECTORY);
        if (directory.exists() && directory.isDirectory()) {
            File[] accountFiles = directory.listFiles();
            if (accountFiles != null) {
                for (File file : accountFiles) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(",");
                            if (parts.length == 3 && parts[0].equals(loggedInUsername)) {
                                accounts.add(new Account(parts[0], parts[1], parts[2]));
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return accounts;
    }
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Please fill in all fields.");
        alert.showAndWait();
    }
}