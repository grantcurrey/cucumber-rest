grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {
    inherits("global") {
    }

    log "warn"

    repositories {
        grailsCentral()
    }

    def cucumberVersion = "1.1.1"
    def httpBuilderVersion = "0.6"

    dependencies {
        compile "org.codehaus.groovy.modules.http-builder:http-builder:${httpBuilderVersion}"

        compile("info.cukes:cucumber-groovy:${cucumberVersion}") {
            excludes 'ant'
        }
    }

    plugins {
        build(":tomcat:$grailsVersion",
                ":release:2.0.3",
                ":rest-client-builder:1.0.2") {
            export = false
        }
    }
}
