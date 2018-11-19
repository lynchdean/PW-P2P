class KdbxConnectionTest extends GroovyTestCase {
    def "Open .kdbx database with password"() {
        given:
        KdbxConnection testConnection = new KdbxConnection()
        def path = 'test.kdbx'
        def password = 'hunter2'

        when:
        def database = testConnection.openConnection(path, password)
        def name = database.getName()

        then:
        name == 'test'
    }
}