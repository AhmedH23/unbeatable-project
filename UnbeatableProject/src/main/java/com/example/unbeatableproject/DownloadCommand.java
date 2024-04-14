package com.example.unbeatableproject;

public class DownloadCommand implements Command {
    private final FileOperations fileOperations;
    public DownloadCommand(FileOperations fileOperations) {
        this.fileOperations = fileOperations;
    }
    @Override
    public void execute() {
        fileOperations.downloadFiles();
    }
}