package com.wotifgroup.cucumber.json

class EndpointBindingUpdater {

    static final String LOAD = "load"
    static final String SET_JSON_PROPERTY = "setJsonProperty"
    static final String POST = "post"
    static final String PUT = "put"
    static final String GET = "get"
    static final String DELETE = "delete"

    Binding binding

    CucumberJson cucumberJson

    def jsonResourceLoader
    def jsonPropertySetter

    def httpActions = []

    EndpointBindingUpdater(Binding binding) {
        this.binding = binding
        this.binding.setVariable("jsonBindingUpdater", this)
        this.cucumberJson = new CucumberJson(binding)
    }

    EndpointBindingUpdater remove() {
        binding.variables.remove(LOAD)
        binding.variables.remove(SET_JSON_PROPERTY)

        [POST, PUT, DELETE, GET].each { value ->
            binding.variables.remove(value)
        }

        binding.variables.remove("jsonBindingUpdater")
        this
    }

    EndpointBindingUpdater initialize() {
        jsonResourceLoader = new JsonResourceLoader(this, cucumberJson)
        jsonPropertySetter = new JsonPropertySetter(this, cucumberJson)

        [POST, PUT, DELETE, GET].each { value ->
            def action = new JsonAction(value, this, cucumberJson)
            httpActions.add(action)
            binding.setVariable(value, action)
        }

        binding.setVariable(LOAD, jsonResourceLoader)
        binding.setVariable(SET_JSON_PROPERTY, jsonPropertySetter)
        this
    }

    public void setDateFormat(String dateFormat){
        cucumberJson.dateFormat = dateFormat
    }

    public void setBaseUrl(String url) {
        httpActions.each { JsonAction action ->
            action.setUrlBase(url)
        }
    }

    public void setSSLDetails(String trustStoreFile, String trustStorePassword = null, String keyStoreFile = null, String keyStorePassword = null) {
        if (trustStoreFile) {
            cucumberJson.initializeSSLTruststore(trustStoreFile, trustStorePassword)
        }

        if (keyStoreFile) {
            cucumberJson.initializeSSLKeystore(keyStoreFile, keyStorePassword)
        }
    }
}
