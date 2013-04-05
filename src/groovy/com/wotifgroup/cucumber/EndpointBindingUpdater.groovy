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
    def jsonPost
    def jsonPut
    def jsonDelete
    def jsonGet

    EndpointBindingUpdater(Binding binding) {
        this.binding = binding
    }

    EndpointBindingUpdater remove() {
        binding.variables.remove(LOAD)
        binding.variables.remove(SET_JSON_PROPERTY)
        binding.variables.remove(POST)
        binding.variables.remove(PUT)
        binding.variables.remove(DELETE)
        binding.variables.remove(GET)
        this
    }

    EndpointBindingUpdater initialize() {
        jsonResourceLoader = new JsonResourceLoader(this, binding)
        jsonPropertySetter = new JsonPropertySetter(this, binding)
        jsonPost = new JsonAction("post", this, binding)
        jsonPut = new JsonAction("put", this, binding)
        jsonGet = new JsonAction("get", this, binding)
        jsonDelete = new JsonAction("delete", this, binding)

        binding.setVariable(LOAD, jsonResourceLoader)
        binding.setVariable(SET_JSON_PROPERTY, jsonPropertySetter)
        binding.setVariable(POST, jsonPost)
        binding.setVariable(PUT, jsonPut)
        binding.setVariable(GET, jsonGet)
        binding.setVariable(DELETE, jsonDelete)

        this
    }
}
