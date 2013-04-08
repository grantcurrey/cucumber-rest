package com.wotifgroup.cucumber.json

/**
 * User: gcurrey
 * Date: 19/06/12
 * Time: 11:29 AM
 */
class JsonAction extends Closure {
    def urlBase = System.getProperty("testing.functional.baseUrl")
    private CucumberJson cucumberJson
    private String action

    JsonAction(String action, Object theTarget, CucumberJson cucumberJson) {
        super(theTarget)
        this.cucumberJson = cucumberJson
        this.action = action
    }

    protected doCall(Object[] args) {
        if (!urlBase) {
            throw new Exception("Unable to determine the base url to post to.  Currently urlBase is [${urlBase}].  Either set this on JsonAction or set -Dtesting.functional.baseUrl")
        } else {
            cucumberJson.doJsonRequest(action, urlBase, args[0])
        }
    }

    public void setUrlBase(String urlBase){
        this.urlBase = urlBase
    }
}