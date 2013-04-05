package com.wotifgroup.cucumber

/**
 * User: gcurrey
 * Date: 19/06/12
 * Time: 11:29 AM
 */
class JsonAction extends Closure {
    def urlBase = System.getProperty("testing.functional.baseUrl")
    private CucumberJson cucumberJson
    private String action

    JsonAction(String action, Object theTarget, Binding binding) {
        super(theTarget)
        cucumberJson = new CucumberJson(binding)
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

    public void setSSLDetails(String trustStoreFile, String trustStorePassword = null, String keyStoreFile = null, String keyStorePassword = null){
        this.cucumberJson.initializeSSL(keyStoreFile, trustStoreFile, keyStorePassword, trustStorePassword)
    }
}