package grails.plugin.cucumberjson

/**
 * User: gcurrey
 * Date: 18/06/12
 * Time: 3:36 PM
 */
class EndpointBindingUpdater {

    final Binding binding

    EndpointBindingUpdater(Binding binding) {
        this.binding = binding
    }

    EndpointBindingUpdater initialize() {
        binding.setVariable("load", new JsonResourceLoader(this, binding))
        binding.setVariable("setJsonProperty", new JsonPropertySetter(this, binding))
        binding.setVariable("post", new JsonPost(this, binding))
        this
    }

    EndpointBindingUpdater remove() {
        binding.variables.remove("load")
        binding.variables.remove("setJsonProperty")
        binding.variables.remove("post")
        this
    }
}
