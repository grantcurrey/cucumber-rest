package grails.plugin.cucumberjson

import cucumber.runtime.io.FileResourceLoader

/**
 * User: gcurrey
 * Date: 8/01/13
 * Time: 12:38 PM
 */
class FileUtil {
    static def loadFileResource(String name, String directory) {
        FileResourceLoader resourceLoader = new FileResourceLoader();

        def resources = resourceLoader.resources(directory, name);
        def resourcesIterator = resources.iterator()

        if (resourcesIterator.hasNext()) {
            return resourcesIterator.next().getInputStream().text;
        } else {
            throw new RuntimeException("Unable to load file resource $directory/$name")
        }
    }
}
