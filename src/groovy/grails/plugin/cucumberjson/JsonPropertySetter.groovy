package grails.plugin.cucumberjson

import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONArray

/**
 * User: gcurrey
 * Date: 19/06/12
 * Time: 10:37 AM
 */
class JsonPropertySetter extends Closure {
    private final Binding binding;

    JsonPropertySetter(Object theTarget, Binding theBinding) {
        super(theTarget)
        binding = theBinding;
    }

    protected doCall(Object[] args) {
        String[] path = args[1].split("\\.")
        JSON json = binding.getVariable("jsonRequest") as JSON

        def child = path[-1]
        def parent = parseJsonExpression(path, json.target)

        this."${args[0]}"(parent, child, args)
    }

    private def parseJsonExpression(String[] path, parent) {
        path[0..<path.length - 1].each { String pathPart ->
            def m = pathPart =~ /(.*)\[([0-9]*)\]/
            if (m) {
                if (m[0][1]) {
                    parent = parent."${m[0][1]}"[m[0][2] as Integer]
                } else {
                    parent = parent[m[0][2] as Integer]
                }
            } else {
                parent = parent."$pathPart"
            }
        }
        return parent
    }

    private void set(def parent, def child, def args) {
        def value = args[2]
        if (value.isNumber()) {
            try {
                value = value as Long
            } catch (Exception e) {
                //If we cant make it a number, just use it as a string
            }
        }
        parent."$child" = value
    }

    private void clear(def parent, def child, def args) {
        parent."$child" = ""
    }

    private void remove(def parent, def child, def args) {
        parent.remove(child)
    }

    private void nullify(def parent, def child, def args) {
        parent."$child" = null
    }

    private void add(def parent, def child, def args) {
        if (parent."$child" == null) {
            parent."$child" = new JSONArray()
        }

        if (parent."$child" instanceof List) {
            parent."$child".add(args[2])
        }
    }

}