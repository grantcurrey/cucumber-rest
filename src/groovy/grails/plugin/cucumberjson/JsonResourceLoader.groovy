package grails.plugin.cucumberjson

import com.wotifgroup.cucumber.CucumberJson

/**
 * User: gcurrey
 * Date: 18/06/12
 * Time: 5:05 PM
 */
class JsonResourceLoader extends Closure {
    private static final String JSON_DIR = "test/cucumber/json"

    private CucumberJson cucumberJson

    JsonResourceLoader(Object theTarget, Binding binding) {
        super(theTarget)
        cucumberJson = new CucumberJson(binding)
    }

    protected doCall(Object[] args) {
        cucumberJson.loadJsonRequest(args[0] as String, JSON_DIR)
    }
}
