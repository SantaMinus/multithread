import com.company.ReaderCallable

class ReaderThreadSpec extends spock.lang.Specification {
    def "it should require at least 1 URL"() {
        when:
        new ReaderCallable()
        then:
        def e = thrown NullPointerException
        e.getMessage() == "url should not be null"
    }

    def "it should accept a URL"() {
        given:
        URL url = new URL('https://vk.com')

        when:
        new ReaderCallable(url)

        then:
        noExceptionThrown()
    }

    // TODO: test a run() method somehow
}