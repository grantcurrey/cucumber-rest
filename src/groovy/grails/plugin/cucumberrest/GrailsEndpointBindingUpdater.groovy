package grails.plugin.cucumberrest

import com.wotifgroup.cucumber.rest.EndpointBindingUpdater

/**
 * User: gcurrey
 * Date: 18/06/12
 * Time: 3:36 PM
 */
class GrailsEndpointBindingUpdater extends EndpointBindingUpdater {

    static final CONFIG_NAME = "Config.groovy"
    static final CONFIG_PATH = ["grails-app", "conf", CONFIG_NAME].join(File.separator)

    GrailsEndpointBindingUpdater(Binding binding) {
        super(binding)
    }

    void initialize() {
        super.initialize()

        def configObject = new ConfigSlurper("TEST").parse(new File(CONFIG_PATH).toURL())
        def urlBase = configObject.grails.serverURL ?: System.getProperty("grails.testing.functional.baseUrl")

        setBaseUrl(urlBase)
        setResourceLoaderBaseDir("test/cucumber/json")
    }

    public static def isGrailsApplication(){
        try{
            Class.forName("grails.util.Environment")
            return new File(CONFIG_PATH).exists()
        } catch (Exception e){
            false
        }
    }
}
