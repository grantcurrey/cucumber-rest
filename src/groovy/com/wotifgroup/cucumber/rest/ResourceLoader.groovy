package com.wotifgroup.cucumber.rest

import groovy.json.JsonSlurper
import groovyx.net.http.ContentType

/**
 * User: gcurrey
 * Date: 18/06/12
 * Time: 5:05 PM
 */
class ResourceLoader extends Closure {
    private String resourceDirectory = "src/test/resources"

    private CucumberRest cucumberRest

    ResourceLoader(Object theTarget, CucumberRest cucumberRest) {
        super(theTarget)
        this.cucumberRest = cucumberRest
    }

    protected doCall(Object[] args) {
        def slurper;
        ContentType type = args[1]
        if (type == ContentType.XML) {
            slurper = new JsonSlurper()
        } else {
            slurper = new XmlSlurper()
        }
        cucumberRest.loadRequest("${resourceDirectory}/${type.name().toLowerCase()}", args[0] as String, slurper, type);
    }

    public void setResourceDirectory(String resourceDirectory) {
        this.resourceDirectory = resourceDirectory
    }
}
