package com.wotifgroup.cucumber.glue

import com.wotifgroup.cucumber.rest.CucumberRest

import groovyx.net.http.ContentType

import static cucumber.api.groovy.EN.Given
import static cucumber.api.groovy.EN.Then

Given(~/^the date format is "(.*)"/) { String format ->
    cucumberRestBindingUpdater.setDateFormat(format)
}

Given(~/^the base url is \"(.*)\"/) { String url ->
    cucumberRestBindingUpdater.setBaseUrl(url)
}

Given(~/^the ssl keystore is \"(.*)\" and the keystore password is \"(.*)\"/) { String keystore, String password ->
    cucumberRestBindingUpdater.setSSLDetails(null, null, keystore, password)
}

Given(~/^the ssl truststore is \"(.*)\" and the truststore password is \"(.*)\"/) { String truststore, String password ->
    cucumberRestBindingUpdater.setSSLDetails(truststore, password, null, null)
}

Given(~'^I am sending a \"(.*)\" (json|xml) request$') { String name, String type ->
    load name.replace(" ", "_") + ".json", ContentType.valueOf(type)
}

Given(~'^I am sending a \"(.*)\"$') { String json ->
    load json.replace(" ", "_") + ".json", ContentType.JSON
}

Given(~'^I set the request \"(.*)\" property to (.*)$') { String property, String value ->
    setJsonProperty "set", property, value
}

Given(~'^I add \"(.*)\" to the request \"(.*)\" property$') { String value, String property ->
    setJsonProperty "add", property, value
}

Given(~'^I (remove|clear|nullify) the request \"(.*)\" property$') { String action, String property ->
    setJsonProperty action, property
}

Then(~/^I post the "([\w ]+)" to "(.*)"/) { String type, String resource ->
    post resource
}

Then(~/^I get the "([\w ]+)" to "(.*)"/) { String type, String resource ->
    get resource
}

Then(~/^I put the "([\w ]+)" to "(.*)"/) { String type, String resource ->
    put resource
}

Then(~/^I delete the "([\w ]+)" to "(.*)"/) { String type, String resource ->
    delete resource
}

Then(~/^the http response code is "([0-9]*)"/) { int code ->
    assert (responseCode as Integer == code as Integer)
}

Then(~'^the response property \"(.*)\" has a value$') { String property ->
    path = property.split("\\.")
    child = path[-1]
    parent = CucumberRest.parseJsonExpression(path, jsonResponse)

    assert parent."$child" != null && String.valueOf(parent."$child") != ""
}

Then(~'^the response property \"(.*)\" equals (\"?.*\"?)$') { String property, String value ->
    path = property.split("\\.")
    child = path[-1]
    parent = CucumberRest.parseJsonExpression(path, jsonResponse)

    assert (parent."$child" == CucumberRest.parseStringToType(value, cucumberRestBindingUpdater.getDateFormat()))
}

Then(~'^the response property \"(.*)\" is (\"?.*\"?)$') { String property, String value ->
    path = property.split("\\.")
    child = path[-1]
    parent = CucumberRest.parseJsonExpression(path, jsonResponse)

    assert (parent."$child" == CucumberRest.parseStringToType(value, cucumberRestBindingUpdater.getDateFormat()))
}

Then(~'^the response property \"(.*)\" contains \"?(.*)\"?$') { String property, String value ->
    path = property.split("\\.")
    child = path[-1]
    parent = CucumberRest.parseJsonExpression(path, jsonResponse)

    assert String.valueOf(parent."$child").contains(value)
}
