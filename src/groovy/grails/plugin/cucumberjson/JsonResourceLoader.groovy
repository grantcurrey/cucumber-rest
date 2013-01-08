package grails.plugin.cucumberjson

import grails.converters.JSON

/**
 * User: gcurrey
 * Date: 18/06/12
 * Time: 5:05 PM
 */
class JsonResourceLoader extends Closure {
    private static final String JSON_DIR = "test/cucumber/json"

    private final Binding binding;

    JsonResourceLoader(Object theTarget, Binding theBinding) {
        super(theTarget)
        binding = theBinding;
    }

    protected doCall(Object[] args) {
        binding.setVariable("jsonRequest", JSON.parse(FileUtil.loadFileResource(args[0].toString(), JSON_DIR)));
    }
}
