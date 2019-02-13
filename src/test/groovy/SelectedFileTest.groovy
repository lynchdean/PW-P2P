import spock.lang.Specification
import spock.lang.Unroll
import com.lynchd49.syncsafe.utils.SelectedFile

@Unroll
class SelectedFileTest extends Specification {

    // Files
    // Passwords for test files are the same as the name fo the file, i.e. the password for "test1.kdbx" is "test1"
    static File file1 = new File("test1.kdbx")
    static File file2 = new File("test2.kdbx")

    // Selected files
    static SelectedFile sf1 = new SelectedFile(file1)
    static SelectedFile sf2 = new SelectedFile(file2)

    def "Should return original file"() {
        expect:
        sf.getFile() == originalfile

        where:
        sf  || originalfile
        sf1 || file1
        sf2 || file2
    }

    def "Should return original file names"() {
        expect:
        sf.getFileName() == returnedFileName

        where:
        sf  || returnedFileName
        sf1 || "test1.kdbx"
        sf2 || "test2.kdbx"
    }

    def "Should return original file path"() {
        expect:
        sf.getPath() == filePath

        where:
        sf  || filePath
        sf1 || "test1.kdbx"
        sf2 || "test2.kdbx"
    }
 }
