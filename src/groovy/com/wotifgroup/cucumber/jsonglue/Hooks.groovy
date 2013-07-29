package com.wotifgroup.cucumber.jsonglue

import com.wotifgroup.cucumber.json.EndpointBindingUpdater
import grails.plugin.cucumberjson.GrailsEndpointBindingUpdater

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