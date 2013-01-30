grails-cucumber-json plugin
===========================

This plugin provides support for writing cucumber functional tests that post to a restfull endpoint.

Installation & Configuration
-------------

### Install the plugin

### Ensure you have installed the latest version of [grails cucumber plugin](https://github.com/hauner/grails-cucumber)

### Register the handler

Edit support/env.groovy (required for the cucumber-json plugin) and add the following snippet.
This sets up up a handler for all functional tests that are tagged as @endpoint.  You could of course change

    Before("@endpoint") {
        bindingUpdater = new EndpointBindingUpdater(binding);
        bindingUpdater.initialize()
    }}

You can change the tag (@endpoint) to be anything you want.  If you have Geb already integrated with cucumber, be sure to exclude all @endpoint functional tests from your geb binding updater, ie:

    Before("~@endpoint") {
        bindingUpdater = new BindingUpdater(binding, new Browser())
        bindingUpdater.initialize()
    }

An example env.groovy file is available in the templates folder of the plugin.

### Install Step Definitions

Located in the templates folder is EndpointCommon_Steps.groovy.  You should copy this script to test/cucumber/step_definitions (default).

The step definitions provide the following cucumber dsl's which can be used in any cucumber functional test.

* I am sending a "(.*)"
* I set the "(.*)" property to (.*)
* I (remove|clear|nullify) the "(.*)" property
* I post the "([\w ]+)
* I post the "([\w ]+)" to (.*)
* the response property "(.*)" is "(.*)"

### Install your example JSON file to post

The DSL will load, configure and send an example JSON packet to an endpoint running on your grails server.   The plugin looks to load the json message
you specify in (I am sending a "(.*)") from the cucumber/json folder.

Eg, the following dsl:

`I am sending a "test message"`

will look for the following file

`test/cucumber/testMessage.json`

Example Scenarios
-----------------

    Scenario: Some restfull scenario
        Given I am sending a "json message"
        And I set the "property" property to aValue
        When I post the "json message"
        And the response property "result" is "OK"
