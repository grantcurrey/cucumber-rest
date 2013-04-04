package com.wotifgroup.cucumber

import grails.plugin.cucumberjson.FileUtil
import groovy.json.JsonSlurper
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import org.codehaus.groovy.grails.web.json.JSONArray

import static groovyx.net.http.Method.POST

class CucumberJson {

    static final JSON_REQUEST_VARIABLE = "jsonRequest"
    static final JSON_RESPONSE_CODE_VARIABLE = "responseCode"
    static final JSON_RESPONSE_VARIABLE = "jsonResponse"

    private Binding binding

    public CucumberJson(Binding binding) {
        this.binding = binding
    }

    public void postJsonRequest(def urlBase, def path) {
        def http = new HTTPBuilder("${urlBase}${path}")
        def jsonRequest = binding.getVariable(JSON_REQUEST_VARIABLE);
        def theBinding = binding;

        http.request(POST, ContentType.JSON) { req ->
            body = jsonRequest

            response.success = { resp, json ->
                def statusCode = resp.statusLine.statusCode
                theBinding.setVariable(JSON_RESPONSE_CODE_VARIABLE, statusCode)
                theBinding.setVariable(JSON_RESPONSE_VARIABLE, json)
            }
        }
    }

    public void loadJsonRequest(def directory, def name) {
        binding.setVariable(JSON_REQUEST_VARIABLE, new JsonSlurper().parseText(FileUtil.loadFileResource(name, directory)));
    }

    public void setJsonProperty(String action, String path, def value) {
        String[] pathParts = path.split("\\.")
        def json = binding.getVariable(JSON_REQUEST_VARIABLE)

        def child = path[-1]
        def parent = parseJsonExpression(pathParts, json)

        this."${action}"(parent, child, value)
    }

    private def parseJsonExpression(String[] path, parent) {
        path[0..<path.length - 1].each { String pathPart ->
            def m = pathPart =~ /(.*)\[([0-9]*)\]/
            if (m) {
                if (m[0][1]) {
                    //support for arrays
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

    private void set(def parent, def child, def value) {
        if (value.isNumber()) {
            try {
                value = value as Long
            } catch (Exception e) {
                //If we cant make it a number, just use it as a string
            }
        }
        parent."$child" = value
    }

    private void clear(def parent, def child, def value=null) {
        parent."$child" = ""
    }

    private void remove(def parent, def child, def value=null) {
        parent.remove(child)
    }

    private void nullify(def parent, def child, def value=null) {
        parent."$child" = null
    }

    private void add(def parent, def child, def value) {
        if (parent."$child" == null) {
            parent."$child" = new JSONArray()
        }

        if (parent."$child" instanceof List) {
            parent."$child".add(value)
        }
    }
}
