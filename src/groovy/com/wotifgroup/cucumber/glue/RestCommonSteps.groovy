package com.wotifgroup.cucumber.glue

import com.wotifgroup.cucumber.rest.ExpressionUtil
import groovyx.net.http.ContentType

import static com.wotifgroup.cucumber.rest.ExpressionUtil.evaluateGPathExpression
import static com.wotifgroup.cucumber.rest.ExpressionUtil.parseStringToType
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
    load name.replace(" ", "_") + "." + type, ContentType.valueOf(type.toUpperCase())
}

Given(~'^I am sending a \"(.*)\"$') { String json ->
    load json.replace(" ", "_") + ".json", ContentType.JSON
}

Given(~'^I clear( the)? request headers$') { String notUsed->
    setHeader null, null
}

Given(~'^I set the request header \"(.*)\" property to (.*)$') { String header, String value ->
    setHeader header, value
}

Given(~'^I set the request \"(.*)\" property to (.*)$') { String property, String value ->
    setProperty "set", property, value
}

Given(~'^I add \"(.*)\" to the request \"(.*)\" property$') { String value, String property ->
    setProperty "add", property, value
}

Given(~'^I (remove|clear|nullify) the request \"(.*)\" property$') { String action, String property ->
    setProperty action, property
}

Then(~/^I post the "([\w ]+)" to "?([^"]*)"?/) { String type, String resource ->
    post resource
}

Then(~/^I call the "?([^"]*)"? resource/) { String resource ->
    get resource
}

Then(~/^I get the "([\w ]+)" to "?([^"]*)"?/) { String type, String resource ->
    get resource
}

Then(~/^I put the "([\w ]+)" to "?([^"]*)"?/) { String type, String resource ->
    put resource
}

Then(~/^I delete the "([\w ]+)" to "?([^"]*)"?/) { String type, String resource ->
    delete resource
}

Then(~/^the( http)? response code is "([0-9]*)"/) { String notUsed, int code ->
    assert (responseCode as Integer == code as Integer)
}

Then(~'^the response header \"(.*)\" equals ("?.*"?)$') { String header, String value ->
    assert (responseHeaders."${header}" == parseStringToType(value))
}

Then(~'^the response header \"(.*)\" has a value$') { String header ->
    assert (responseHeaders."${header}" != null)
}

Then(~'^the response header \"(.*)\" matches ("?.*"?)$') { String header, String pattern ->
    assert responseHeaders."${header}" =~ /${pattern}/
}

Then(~'^the response equals ("?.*"?)$') { String value ->
    assert (response as String) == value
}

Then(~'^the response contains ("?.*"?)$') { String value ->
    assert (response as String).contains(value)
}

Then(~'^the response matches ("?.*"?)$') { String pattern ->
    assert (response as String) =~ /${pattern}/
}

Then(~'^the response (property|element) \"(.*)\" matches ("?.*"?)$') { String type, String property, String pattern ->
    assert evaluateGPathExpression(property, response) =~ /${pattern}/
}

Then(~'^the response (property|element) \"(.*)\" has a value$') { String type, String property ->
    assert evaluateGPathExpression(property, response) != null && String.valueOf(parent."$child") != ""
}

Then(~'^the response (property|element) \"(.*)\" equals ("?.*"?)$') { String type, String property, String value ->
    assert (evaluateGPathExpression(property, response) == parseStringToType(value, cucumberRestBindingUpdater.getDateFormat()))
}

Then(~'^the response (property|element) \"(.*)\" is ("?.*"?)$') { String type, String property, String value ->
    assert (evaluateGPathExpression(property, response) == parseStringToType(value, cucumberRestBindingUpdater.getDateFormat()))
}

Then(~'^the response (property|element) \"(.*)\" contains "?(.*)"?$') { String type, String property, String value ->
    assert String.valueOf(evaluateGPathExpression(property, response)).contains(value)
}
