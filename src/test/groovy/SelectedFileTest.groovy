import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class SelectedFileTest extends Specification {

    // Files
    static File file1 = new File("test1.kdbx") // pw = test1
    static File file2 = new File("test2.kdbx") // pw = test2

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
