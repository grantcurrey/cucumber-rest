package com.wotifgroup.cucumber

class EndpointBindingUpdater {

    static final String LOAD = "load"
    static final String SET_JSON_PROPERTY = "setJsonProperty"
    static final String POST = "post"

    Binding binding

    def jsonResourceLoader
    def jsonPropertySetter
    def jsonPost

    EndpointBindingUpdater(Binding binding) {
        this.binding = binding
    }

    EndpointBindingUpdater remove() {
        binding.variables.remove(LOAD)
        binding.variables.remove(SET_JSON_PROPERTY)
        binding.variables.remove(POST)
        this
    }

    EndpointBindingUpdater initialize() {
        jsonResourceLoader = new JsonResourceLoader(this, binding)
        jsonPropertySetter = new JsonPropertySetter(this, binding)
        jsonPost = new JsonPost(this, binding)

        binding.setVariable(LOAD, jsonResourceLoader)
        binding.setVariable(SET_JSON_PROPERTY, jsonPropertySetter)
        binding.setVariable(POST, jsonPost)

        this
    }
}
