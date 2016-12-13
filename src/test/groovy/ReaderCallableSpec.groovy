import com.company.ReaderCallable
import spock.lang.Specification

public class ReaderCallableSpec extends Specification {
    def "it should require at least 1 URL"() {
        when:
        new ReaderCallable()
        then:
        thrown NullPointerException
    }

    def "it should accept only 1 URL"() {
        given:
        URL url1 = new URL('https://vk.com')
        URL url2 = new URL('https://bash.im')

        when:
        new ReaderCallable(url1)
        then:
        noExceptionThrown()

        when:
        new ReaderCallable(url1, url2)
        then:
        thrown Exception
    }

    def "it should get a response from a URL"() {
        given:
        URL url = new URL('https://vk.com')
        ReaderCallable rc = new ReaderCallable(url)

        when:
        def res = rc.call()

        then:
        res == url.toString() + ': 200  OK'
    }
}