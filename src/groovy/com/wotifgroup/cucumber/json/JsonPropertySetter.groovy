package com.wotifgroup.cucumber.json

/**
 * User: gcurrey
 * Date: 19/06/12
 * Time: 10:37 AM
 */
class JsonPropertySetter extends Closure {
    private CucumberJson cucumberJson

    JsonPropertySetter(Object theTarget, CucumberJson cucumberJson) {
        super(theTarget)
        this.cucumberJson = cucumberJson
    }

    protected doCall(Object[] args) {
        cucumberJson.setJsonProperty(args[0] as String, args[1] as String, args.length == 3 ? args[2] : null)
    }
}