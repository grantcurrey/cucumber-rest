package grails.plugin.cucumberjson

import com.wotifgroup.cucumber.CucumberJson
import grails.util.Environment

/**
 * User: gcurrey
 * Date: 19/06/12
 * Time: 11:29 AM
 */
class JsonPost extends Closure {

    static final CONFIG_NAME = "Config.groovy"
    static final CONFIG_PATH = ["grails-app", "conf", CONFIG_NAME].join(File.separator)

    private def configObject
    private CucumberJson cucumberJson

    JsonPost(Object theTarget, Binding binding) {
        super(theTarget)
        configObject = new ConfigSlurper(Environment.TEST.name).parse(new File(CONFIG_PATH).toURL())
        cucumberJson = new CucumberJson(binding)
    }

    protected doCall(Object[] args) {
        def urlBase = configObject.grails.serverURL ?: System.getProperty("grails.testing.functional.baseUrl")

        if (!urlBase) {
            throw new Exception("Unable to find the url grails is running on.  I tried config->grails.serverURL and -Dgrails.testing.functional.baseURL.  Please set one of these variables")
        } else {
            cucumberJson.postJsonRequest(urlBase, args[0])
        }
    }
}