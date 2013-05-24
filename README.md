grails-cucumber-json plugin
===========================

This plugin provides support for writing cucumber functional tests that post to a restful endpoint.

There is now support for both cucumber-jvm standalone, and cucumber-jvm via a grails plugin.

Installation & Configuration - Standalone
-------------

This operates as a standard java library, so no special installation instructions exist other than adding the required library to your classpath.

### Register the Handler

In one of your glue files (step files), include the following code block:

    def endpointBinding = new EndpointBindingUpdater(binding)
    endpointBinding.initialize()
    endpointBinding.setBaseUrl("https://your-base-url")

    //If you need to provide keystore & truststore files for ssl, you can set them here.  Note, keystore & truststore are classpath locations
    endpointBinding.setSSLDetails(keystore,keystore-password,truststore,truststore-password)

### Install Step Definitions

The standalone library ships with JsonCommonSteps.groovy which is a step definition file.  This should be included in your glue path:

    @Cucumber.Options(glue={"src/test/feature/steps","classpath:com/wotifgroup/cucumber/glue"})

Installation & Configuration - GRAILS
-------------

### Ensure you have installed the latest version of [grails cucumber plugin](https://github.com/hauner/grails-cucumber)

### Install the plugin

After you have installed the plugin, there is a script that you can run to auto-configure your application.  The script will execute the steps outlined below so you dont have to.

You can invoke the script by executing the following command:

    grails json-setup

The script will do the following

* Update test/cucumber/support/env.groovy, adding a new Before handler for all features / scenarios that are tagged with @endpoint
* Installs EndpointCommon_Steps.groovy into test/cucumber/step_definitions

If you take this approach, you can jump to the last step.

### Register the handler

Edit support/env.groovy (required for the cucumber-json plugin) and add the following snippet.
This sets up up a handler for all functional tests that are tagged as @endpoint.  You could of course change:

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
* I post|delete|put|get the "([\w ]+)
* I post|delete|put|get the "([\w ]+)" to (.*)
* the response property "(.*)" is "(.*)"

### Install your example JSON file to post

The DSL will load, configure and send an example JSON packet to an endpoint running on your grails server.   The plugin looks to load the json message
you specify in (I am sending a "(.*)") from the cucumber/json folder.

Eg, the following dsl:

`I am sending a "test message"`

will look for the following file

`test/cucumber/json/testMessage.json`

Example Scenarios
-----------------

    Scenario: Some restful scenario
        Given I am sending a "json message"
        And I set the "property" property to aValue
        When I post the "json message" to someEndpoint/someAction
        And the response property "result" is "OK"
