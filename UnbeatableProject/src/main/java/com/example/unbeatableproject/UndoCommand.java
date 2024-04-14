package com.example.unbeatableproject;

public class UndoCommand implements Command {
    private final FileOperations fileOperations;
    public UndoCommand(FileOperations fileOperations) {
        this.fileOperations = fileOperations;
    }
    @Override
    public void execute() {
        fileOperations.undoLastAction();
    }
}