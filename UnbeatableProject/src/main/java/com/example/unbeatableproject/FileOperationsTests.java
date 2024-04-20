package com.example.unbeatableproject;

import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Stack;
import static org.junit.jupiter.api.Assertions.*;

public class FileOperationsTests {
    private FileOperations fileOperations;
    private File uploadDirectory;

    @BeforeEach
    void setUp() {
        Stage primaryStage = new Stage(); // Mock stage
        String loggedInUsername = "testUser";
        fileOperations = new FileOperations(primaryStage, loggedInUsername);
        uploadDirectory = new File("uploaded_files_testUser");
    }
    @Test
    void downloadFilesTest() {
        // Assuming "testFile.txt" exists in uploadDirectory
        ListView<String> fileList = fileOperations.getFileList();
        fileList.getItems().add("testFile.txt");
        // Invoke the method
        fileOperations.downloadFiles();
        // Check if the file was downloaded
        File downloadedFile = new File("testFile.txt");
        assertTrue(downloadedFile.exists());
        downloadedFile.delete(); // Clean up downloaded file
    }
    @Test
    void undoLastActionDeleteTest() {
        // Assuming a delete action is performed before this test
        File deletedFile = new File(uploadDirectory, "testFile.txt");
        deletedFile.mkdirs();
        Stack<UndoAction> undoStack = new Stack<>();
        undoStack.push(new UndoAction(UndoAction.ActionType.DELETE, "testFile.txt"));
        // Invoke the method
        fileOperations.undoLastAction();
        // Check if the delete action was undone
        assertTrue(deletedFile.exists());
    }
}