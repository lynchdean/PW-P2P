package com.lynchd49.syncsafe.utils;

import java.io.File;

public class SelectedFile {

    private File file;

    SelectedFile(File selectedFile) {
        setFile(selectedFile);
    }

    // Set file
    public void setFile(File file) {
        this.file = file;
    }

    // Return file object
    public File getFile() {
        return file;
    }

    // Return file name string
    public String getFileName() {
        return file.getName();
    }


    public String getFilePath() {
        return file.getPath();
    }
}
