package grails.plugin.cucumberjson

import com.wotifgroup.cucumber.CucumberJson

/**
 * User: gcurrey
 * Date: 19/06/12
 * Time: 10:37 AM
 */
class JsonPropertySetter extends Closure {
    private CucumberJson cucumberJson

    JsonPropertySetter(Object theTarget, Binding binding) {
        super(theTarget)
        cucumberJson = new CucumberJson(binding)
    }

    protected doCall(Object[] args) {
        cucumberJson.setJsonProperty(args[0] as String, args[1] as String, args.length == 3 ? args[2] : null)
    }
}