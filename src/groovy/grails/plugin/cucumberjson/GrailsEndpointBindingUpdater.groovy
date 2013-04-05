package grails.plugin.cucumberjson

import com.wotifgroup.cucumber.EndpointBindingUpdater
import com.wotifgroup.cucumber.JsonPost
import com.wotifgroup.cucumber.JsonPropertySetter
import com.wotifgroup.cucumber.JsonResourceLoader
import grails.util.Environment

/**
 * User: gcurrey
 * Date: 18/06/12
 * Time: 3:36 PM
 */
class GrailsEndpointBindingUpdater extends EndpointBindingUpdater {

    static final CONFIG_NAME = "Config.groovy"
    static final CONFIG_PATH = ["grails-app", "conf", CONFIG_NAME].join(File.separator)

    GrailsEndpointBindingUpdater(Binding binding) {
        super(binding)
    }

    GrailsEndpointBindingUpdater initialize() {
        def loader = new JsonResourceLoader(this, binding)
        def setter = new JsonPropertySetter(this, binding)
        def post = new JsonPost(this, binding)

        def configObject = new ConfigSlurper(Environment.TEST.name).parse(new File(CONFIG_PATH).toURL())
        post.urlBase = configObject.grails.serverURL ?: System.getProperty("grails.testing.functional.baseUrl")

        loader.jsonDirectory = "test/cucumber/json"

        binding.setVariable(LOAD, loader)
        binding.setVariable(SET_JSON_PROPERTY, setter)
        binding.setVariable(POST, post)

        this
    }
}
