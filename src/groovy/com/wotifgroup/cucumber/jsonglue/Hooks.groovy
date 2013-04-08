package com.wotifgroup.cucumber.jsonglue

import com.wotifgroup.cucumber.json.EndpointBindingUpdater

import static cucumber.api.groovy.Hooks.After
import static cucumber.api.groovy.Hooks.Before

def bindingUpdater

Before('@endpoint') {
    bindingUpdater = new EndpointBindingUpdater(binding)
    bindingUpdater.initialize()
}

After('@endpoint') {
    if (bindingUpdater) {
        bindingUpdater.remove()
    }
}