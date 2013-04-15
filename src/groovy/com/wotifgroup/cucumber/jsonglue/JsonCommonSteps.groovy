package com.wotifgroup.cucumber.jsonglue

import com.wotifgroup.cucumber.json.CucumberJson

import static cucumber.api.groovy.EN.Given
import static cucumber.api.groovy.EN.Then

Given(~/^the date format is "(.*)"/) { String format ->

}

Given(~/^the base url is \"(.*)\"/) { String url ->
    jsonBindingUpdater.setBaseUrl(url)
}

Given(~/^the ssl keystore is \"(.*)\" and the keystore password is \"(.*)\"/) { String keystore, String password ->
    jsonBindingUpdater.setSSLDetails(null, null, keystore, password)
}

Given(~/^the ssl truststore is \"(.*)\" and the truststore password is \"(.*)\"/) { String truststore, String password ->
    jsonBindingUpdater.setSSLDetails(truststore, password, null, null)
}

Given(~'^I am sending a \"(.*)\"$') { String json ->
    load json.replace(" ", "_") + ".json"
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

Then(~'^the response property \"(.*)\" is (\"?.*\"?)$') { String property, String value ->
    path = property.split("\\.")
    child = path[-1]
    parent = CucumberJson.parseJsonExpression(path, jsonResponse)

    assert (parent."$child" == CucumberJson.parseStringToType(value))
}
