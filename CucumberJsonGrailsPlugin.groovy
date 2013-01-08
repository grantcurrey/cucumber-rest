class CucumberJsonGrailsPlugin {
    def version = "0.1"
    def grailsVersion = "2.1 > *"
    def dependsOn = ["cucumber": "0.5.0 > *"]
    def pluginExcludes = [
            "**/.gitignore",
            "grails-app/**",
            "src/java/**",
            "web-app/**"
    ]

    def title = "Grails Cucumber Json Plugin"
    def author = "Grant Currey"
    def authorEmail = "grantc@dna-tech.net.au"
    def description = 'Test your JSON endpoints using Cucumber'
    def documentation = "https://github.com/grantcurrey/grails-cucumber-json"
    def license = "APACHE"
    def scm = [url: "https://github.com/grantcurrey/grails-cucumber-json"]
}
