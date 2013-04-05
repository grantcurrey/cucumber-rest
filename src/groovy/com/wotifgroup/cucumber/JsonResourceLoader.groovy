package com.wotifgroup.cucumber

/**
 * User: gcurrey
 * Date: 18/06/12
 * Time: 5:05 PM
 */
class JsonResourceLoader extends Closure {
    String jsonDirectory = "src/test/resources/json"

    private CucumberJson cucumberJson

    JsonResourceLoader(Object theTarget, Binding binding) {
        super(theTarget)
        cucumberJson = new CucumberJson(binding)
    }

    protected doCall(Object[] args) {
        cucumberJson.loadJsonRequest(jsonDirectory, args[0] as String)
    }
}
