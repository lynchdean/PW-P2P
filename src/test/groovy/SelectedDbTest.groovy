import com.lynchd49.syncsafe.utils.SelectedDb
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class SelectedDbTest extends Specification {

    // Files
    // Passwords for test files are the same as the name fo the file, i.e. the password for "test1.kdbx" is "test1"
    static File file1 = new File("test1.kdbx")
    static File file2 = new File("test2.kdbx")

    // Selected files
    static SelectedDb sf1 = new SelectedDb(file1)
    static SelectedDb sf2 = new SelectedDb(file2)


    def "Should set a new .kdbx file"() {
        when:
        sf.setFile(newFile)

        then:
        sf.getFile() == newFile

        cleanup:
        sf.setFile(origninalFile)

        where:
        sf  || newFile  || origninalFile
        sf1 || file2    || file1
        sf2 || file1    || file2
    }

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
        sf.getFilePath() == filePath

        where:
        sf  || filePath
        sf1 || "test1.kdbx"
        sf2 || "test2.kdbx"
    }
 }
