import com.company.Main
import groovyjarjarcommonscli.MissingOptionException

class MainSpec extends spock.lang.Specification {
    def "it should get al least 1 argument"() {
        when:
        Main.main()

        then:
        MissingOptionException e = thrown()
        e.getMessage() == "Please specify at least 1 URL"
    }
}