package com.wotifgroup.cucumber.jsonglue

import static cucumber.api.groovy.EN.Given
import static cucumber.api.groovy.EN.Then

Given(~/^The base url is \"(.*)\"/) { String url ->
    jsonBindingUpdater.setBaseUrl(url)
}

Given(~/^The ssl keystore is \"(.*)\" and the keystore password is \"(.*)\"/) { String keystore, String password ->
    jsonBindingUpdater.setSSLDetails(null,null,keystore,password)
}

Given(~/^The ssl truststore is \"(.*)\" and the truststore password is \"(.*)\"/) { String truststore, String password ->
    jsonBindingUpdater.setSSLDetails(truststore,password,null,null)
}

Given(~'^I am sending a \"(.*)\"$') { String json ->
    load json.replace(" ", "_") + ".json"
}

Given(~'^I set the \"(.*)\" property to (.*)$') { String property, String value ->
    setJsonProperty "set", property, value
}

Given(~'^I add \"(.*)\" to the \"(.*)\" property$') { String value, String property ->
    setJsonProperty "add", property, value
}

Given(~'^I (remove|clear|nullify) the \"(.*)\" property$') { String action, String property ->
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

Then(~/^The http response code is "([0-9]*)"/) { int code ->
    assert (responseCode as Integer == code as Integer)
}

Then(~'^the response property \"(.*)\" is \"(.*)\"$') { String property, String value ->
    def parent = jsonResponse

    String[] path = property.split("\\.")
    path[0..<path.length].each { String pathPart ->
        def m = pathPart =~ /(.*)\[([0-9]*)\]/
        if (m) {
            parent = parent."${m[0][1]}"[m[0][2] as Integer]
        } else {
            parent = parent."$pathPart"
        }
    }

    assert (parent == value)
}
