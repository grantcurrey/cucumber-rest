package com.wotifgroup.cucumber.rest

import grails.plugin.cucumberrest.GrailsEndpointBindingUpdater

class EndpointBindingUpdater {

    static final String LOAD = "load"
    static final String SET_PROPERTY = "setProperty"
    static final String SET_HEADER = "setHeader"
    static final String POST = "post"
    static final String PUT = "put"
    static final String GET = "get"
    static final String DELETE = "delete"
    static final String CUCUMBER_REST_BINDING_UPDATER = "cucumberRestBindingUpdater"

    Binding binding
    CucumberRest cucumberRest
    ResourceLoader resourceLoader
    PropertySetter propertySetter
    HeaderSetter headerSetter

    def httpActions = []

    public static def initialize(def binding) {
        def bindingUpdater
        if (binding.hasVariable(CUCUMBER_REST_BINDING_UPDATER)) {
            bindingUpdater = binding.getVariable(CUCUMBER_REST_BINDING_UPDATER)
        } else {
            if (GrailsEndpointBindingUpdater.isGrailsApplication()) {
                bindingUpdater = new GrailsEndpointBindingUpdater(binding)
            } else {
                bindingUpdater = new EndpointBindingUpdater(binding)
            }
            bindingUpdater.initialize()
        }
        return bindingUpdater
    }

    EndpointBindingUpdater(Binding binding) {
        this.binding = binding
        this.binding.setVariable(CUCUMBER_REST_BINDING_UPDATER, this)
        this.cucumberRest = new CucumberRest(binding)
    }

    EndpointBindingUpdater remove() {
        binding.variables.remove(LOAD)
        binding.variables.remove(SET_PROPERTY)
        binding.variables.remove(SET_HEADER)

        [POST, PUT, DELETE, GET].each { value ->
            binding.variables.remove(value)
        }

        binding.variables.remove(CUCUMBER_REST_BINDING_UPDATER)
        this
    }

    void initialize() {
        resourceLoader = new ResourceLoader(this, cucumberRest)
        propertySetter = new PropertySetter(this, cucumberRest)
        headerSetter = new HeaderSetter(this, cucumberRest)

        [POST, PUT, DELETE, GET].each { value ->
            def action = new DoAction(value, this, cucumberRest)
            httpActions.add(action)
            binding.setVariable(value, action)
        }

        binding.setVariable(LOAD, resourceLoader)
        binding.setVariable(SET_PROPERTY, propertySetter)
        binding.setVariable(SET_HEADER, headerSetter)
    }

    public void setDateFormat(String dateFormat) {
        cucumberRest.dateFormat = dateFormat
    }

    public String getDateFormat() {
        cucumberRest.dateFormat
    }

    public void setBaseUrl(String url) {
        httpActions.each { DoAction action ->
            action.setUrlBase(url)
        }
    }

    public void setResourceLoaderBaseDir(String dir) {
        resourceLoader.setResourceDirectory(dir)
    }

    public void setSSLDetails(String trustStoreFile, String trustStorePassword = null, String keyStoreFile = null, String keyStorePassword = null) {
        if (trustStoreFile) {
            cucumberRest.initializeSSLTruststore(trustStoreFile, trustStorePassword)
        }

        if (keyStoreFile) {
            cucumberRest.initializeSSLKeystore(keyStoreFile, keyStorePassword)
        }
    }
}
