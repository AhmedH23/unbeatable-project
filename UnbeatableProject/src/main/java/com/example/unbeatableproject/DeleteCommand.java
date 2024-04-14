package com.example.unbeatableproject;

public class DeleteCommand implements Command {
    private final FileOperations fileOperations;
    public DeleteCommand(FileOperations fileOperations) {
        this.fileOperations = fileOperations;
    }
    @Override
    public void execute() {
        fileOperations.deleteFiles();
    }
}