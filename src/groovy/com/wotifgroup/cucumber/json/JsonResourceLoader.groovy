package com.wotifgroup.cucumber.json

/**
 * User: gcurrey
 * Date: 18/06/12
 * Time: 5:05 PM
 */
class JsonResourceLoader extends Closure {
    private String jsonDirectory = "src/test/resources/json"

    private CucumberJson cucumberJson

    JsonResourceLoader(Object theTarget, CucumberJson cucumberJson) {
        super(theTarget)
        this.cucumberJson = cucumberJson
    }

    protected doCall(Object[] args) {
        cucumberJson.loadJsonRequest(jsonDirectory, args[0] as String)
    }

    public void setJsonDirectory(String jsonDirectory) {
        this.jsonDirectory = jsonDirectory
    }
}
