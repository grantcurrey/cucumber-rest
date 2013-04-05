package com.wotifgroup.cucumber

class EndpointBindingUpdater {

    static final String LOAD = "load"
    static final String SET_JSON_PROPERTY = "setJsonProperty"
    static final String POST = "post"
    static final String PUT = "put"
    static final String GET = "get"
    static final String DELETE = "delete"

    Binding binding

    def jsonResourceLoader
    def jsonPropertySetter

    def httpActions = []

    EndpointBindingUpdater(Binding binding) {
        this.binding = binding
    }

    EndpointBindingUpdater remove() {
        binding.variables.remove(LOAD)
        binding.variables.remove(SET_JSON_PROPERTY)

        [POST, PUT, DELETE, GET].each { value ->
            binding.variables.remove(value)
        }

        this
    }

    EndpointBindingUpdater initialize() {
        jsonResourceLoader = new JsonResourceLoader(this, binding)
        jsonPropertySetter = new JsonPropertySetter(this, binding)

        [POST, PUT, DELETE, GET].each { value ->
            def action = new JsonAction(value, this, binding)
            httpActions.add(action)
            binding.setVariable(value, action)
        }

        binding.setVariable(LOAD, jsonResourceLoader)
        binding.setVariable(SET_JSON_PROPERTY, jsonPropertySetter)

        this
    }

    public void setBaseUrl(String url) {
        httpActions.each { JsonAction action ->
            action.setUrlBase(url)
        }
    }

    public void setSSLDetails(String trustStoreFile, String trustStorePassword = null, String keyStoreFile = null, String keyStorePassword = null) {
        httpActions.each { JsonAction action ->
            action.setSSLDetails(trustStoreFile, trustStorePassword, keyStoreFile, keyStorePassword)
        }
    }
}
