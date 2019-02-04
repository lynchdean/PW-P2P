import java.io.File;

public class SelectedFile {

    private File file;

    SelectedFile(File selectedFile) {
        file = selectedFile;
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

    // Return full file path string
    public String getPath() {
        return file.getPath();
    }
}
