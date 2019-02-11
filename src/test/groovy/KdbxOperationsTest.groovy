import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class KdbxOperationsTest extends Specification {

    def "Should load #path with credentials: #creds"() {
        when:
        KdbxOperations.loadKdbx(path, creds)

        then:
        notThrown(IllegalStateException)

        where:
        path            || creds
        "test1.kdbx"    || "test1"
        "test2.kdbx"    || "test2"
    }

    def "Should not load #path with credentials: #creds"() {
        when:
        KdbxOperations.loadKdbx(path, creds)

        then:
        thrown(IllegalStateException)

        where:
        path            || creds
        "test1.kdbx"    || "wrong_password"
        "test2.kdbx"    || "wrong_password"
        null            || "wrong_password"
        "test1.kdbx"    || null
        null            || null
    }
}
