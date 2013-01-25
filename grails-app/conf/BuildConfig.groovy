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
    def httpBuilderVersion = "0.5.0"

    dependencies {
        compile("org.codehaus.groovy.modules.http-builder:http-builder:${httpBuilderVersion}") {
            exclude "xml-apis"
            exclude "groovy"
            exclude "httpclient"
            exclude "nekohtml"
        }

        provided("info.cukes:cucumber-groovy:${cucumberVersion}") {
            excludes 'ant'
        }
    }

    plugins {
        build(":tomcat:$grailsVersion", ":release:2.0.3") {
            exclude "groovy"
            export = false
        }
    }
}
