import geb.binding.BindingUpdater
import geb.Browser
import grails.plugin.cucumberjson.EndpointBindingUpdater

import static cucumber.api.groovy.Hooks.*

def bindingUpdater

Before("~@endpoint") {
    bindingUpdater = new BindingUpdater(binding, new Browser())
    bindingUpdater.initialize()
}

Before("@endpoint") {
    bindingUpdater = new EndpointBindingUpdater(binding);
    bindingUpdater.initialize()
}

After () {
    if(bindingUpdater){
        bindingUpdater.remove()
    }
}
