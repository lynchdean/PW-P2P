import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class KdbxOperationsTest extends Specification {

    def "Should successfully open #path file"() {
        expect:
        KdbxOperations.testCreds(path, creds)

        where:
        path            || creds
        "test1.kdbx"    || "test1"
        "test2.kdbx"    || "test2"
    }

    def "Should not open #path file"() {
        expect:
        !KdbxOperations.testCreds(path, creds)

        where:
        path            || creds
        "test1.kdbx"    || "wrong_password"
        "test2.kdbx"    || "wrong_password"
    }
}
