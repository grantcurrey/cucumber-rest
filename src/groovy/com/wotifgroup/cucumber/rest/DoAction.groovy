package com.wotifgroup.cucumber.rest


/**
 * User: gcurrey
 * Date: 19/06/12
 * Time: 11:29 AM
 */
class DoAction extends Closure {
    def urlBase = System.getProperty("testing.functional.baseUrl")
    private CucumberRest cucumberRest
    private String action

    DoAction(String action, Object theTarget, CucumberRest cucumberRest) {
        super(theTarget)
        this.cucumberRest = cucumberRest
        this.action = action
    }

    protected doCall(Object[] args) {
        if (!urlBase) {
            throw new Exception("Unable to determine the base url to post to.  Currently urlBase is [${urlBase}].  Either set this on JsonAction or set -Dtesting.functional.baseUrl")
        } else {
            cucumberRest.doRequest(action, urlBase, args[0])
        }
    }

    public void setUrlBase(String urlBase){
        this.urlBase = urlBase
    }
}