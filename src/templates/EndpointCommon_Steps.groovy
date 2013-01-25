import static cucumber.api.groovy.EN.*

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

Then(~/^I post the "([\w ]+)"/) { String type ->
    post "api/check"
}

Then(~/^I post the "([\w ]+)" to (.*)/) { String type, String resource ->
    post resource
}

Then(~'^the response property \"(.*)\" is \"(.*)\"$') { String property, String value ->
    def parent = jsonResponse

    String[] path = property.split("\\.")
    path[0..<path.length].each { String pathPart ->
        def m = pathPart =~ /(.*)\[([0-9]*)\]/
        if (m){
            parent = parent."${m[0][1]}"[m[0][2] as Integer]
        } else {
            parent = parent."$pathPart"
        }
    }

    assert (parent == value)
}

