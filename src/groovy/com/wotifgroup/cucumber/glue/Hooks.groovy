package com.wotifgroup.cucumber.glue

import com.wotifgroup.cucumber.rest.EndpointBindingUpdater

import static cucumber.api.groovy.Hooks.After
import static cucumber.api.groovy.Hooks.Before

def bindingUpdater

Before('@cucumberRest','@endpoint') {
    bindingUpdater = EndpointBindingUpdater.initialize(binding)
}

After('@cucumberRest', '@endpoint') {
    if (bindingUpdater) {
        bindingUpdater.remove()
    }
}