import groovy.text.SimpleTemplateEngine

includeTargets << grailsScript("_GrailsInit")

target('default': "The description of the script goes here!") {
    templateDir = "$cucumberJsonPluginDir/src/templates"
    cucumberDir = "$basedir/test/cucumber"
    templateEngine = new SimpleTemplateEngine()

    printMessage "$templateDir"

    try {
        printMessage "Installing endpoint step definitions: $cucumberDir/step_definitions/EndpointCommon_Steps.groovy"
        ant.copy file: "$templateDir/EndpointCommon_Steps.groovy.template", tofile: "$cucumberDir/step_definitions/EndpointCommon_Steps.groovy", overwrite: true

        templateEngine = new SimpleTemplateEngine()

        def templateFile = new File(templateDir, 'env.groovy.template')
        def envFile = new File(cucumberDir, 'support/env.groovy')

        def existingEnv = ""
        if (envFile.exists()) {
            existingEnv = envFile.text
        }

        printMessage "Adding binding support to env.groovy"
        envFile.withWriter { writer ->
            templateEngine.createTemplate(templateFile.text).make([existingBody: existingEnv]).writeTo(writer)
        }
    } catch (Exception e) {
        errorMessage "${e.getMessage()}"
    }
}

printMessage = { String message -> event('StatusUpdate', [message]) }
errorMessage = { String message -> event('StatusError', [message]) }