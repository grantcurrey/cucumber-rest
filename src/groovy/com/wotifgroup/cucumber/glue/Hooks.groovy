package com.wotifgroup.cucumber.glue

import com.wotifgroup.cucumber.rest.EndpointBindingUpdater
import grails.plugin.cucumberrest.GrailsEndpointBindingUpdater

import static cucumber.api.groovy.Hooks.After
import static cucumber.api.groovy.Hooks.Before

def bindingUpdater

Before('@cucumberRest') {
    if (GrailsEndpointBindingUpdater.isGrailsApplication()) {
        bindingUpdater = new GrailsEndpointBindingUpdater(binding)
    } else {
        bindingUpdater = new EndpointBindingUpdater(binding)
    }

    bindingUpdater.initialize()
}

Before('@endpoint') {
    if (GrailsEndpointBindingUpdater.isGrailsApplication()) {
        bindingUpdater = new GrailsEndpointBindingUpdater(binding)
    } else {
        bindingUpdater = new EndpointBindingUpdater(binding)
    }

    bindingUpdater.initialize()
}

After('@cucumberRest') {
    if (bindingUpdater) {
        bindingUpdater.remove()
    }
}

After('@endpoint') {
    if (bindingUpdater) {
        bindingUpdater.remove()
    }
}