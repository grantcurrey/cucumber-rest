package com.wotifgroup.cucumber.json

import groovy.json.JsonSlurper
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.ResponseParseException
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.ssl.SSLSocketFactory
import org.codehaus.groovy.grails.web.json.JSONArray

import java.security.KeyStore

import static groovyx.net.http.Method.*

class CucumberJson {

    static final JSON_REQUEST_VARIABLE = "jsonRequest"
    static final JSON_RESPONSE_CODE_VARIABLE = "responseCode"
    static final JSON_RESPONSE_VARIABLE = "jsonResponse"

    private def keyStore
    private def trustStore
    private def keyStorePassword

    String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ"

    private Binding binding

    public CucumberJson(Binding binding) {
        this.binding = binding
    }

    public void initializeSSL(String keyStoreFile, String trustStoreFile, String keyStorePassword, String trustStorePassword) {
        initializeSSLKeystore(keyStoreFile, keyStorePassword)
        initializeSSLTruststore(trustStoreFile, trustStorePassword)
    }

    public void initializeSSLKeystore(String keyStoreFile, String keyStorePassword) {
        if (keyStoreFile) {
            keyStore = KeyStore.getInstance(KeyStore.defaultType)

            getClass().getResource(keyStoreFile).withInputStream {
                keyStore.load(it, keyStorePassword.toCharArray())
            }
            this.keyStorePassword = keyStorePassword
        }
    }

    public void initializeSSLTruststore(String trustStoreFile, trustStorePassword) {
        if (trustStoreFile) {
            trustStore = KeyStore.getInstance(KeyStore.defaultType)
            getClass().getResource(trustStoreFile).withInputStream {
                trustStore.load(it, trustStorePassword.toCharArray())
            }
        }
    }

    public void doJsonRequest(def methodString, def urlBase, def path) {
        def http = new HTTPBuilder("${urlBase}${path}")

        if (keyStore && trustStore) {
            def factory = new SSLSocketFactory(keyStore, keyStorePassword, trustStore)
            factory.hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
            http.client.connectionManager.schemeRegistry.register(new Scheme("https", factory, 443))
        }

        def method = POST
        if (methodString == "post") {
            method = POST
        } else if (methodString == "put") {
            method = PUT
        } else if (methodString == "delete") {
            method = DELETE
        } else if (methodString == "get") {
            method == GET
        }

        try {
            http.request(method, ContentType.JSON) { req ->
                if (method == POST || method == PUT) {
                    body = binding.getVariable(JSON_REQUEST_VARIABLE);
                }

                response.success = { resp, json ->
                    def statusCode = resp.statusLine.statusCode
                    binding.setVariable(JSON_RESPONSE_CODE_VARIABLE, statusCode)
                    binding.setVariable(JSON_RESPONSE_VARIABLE, json)
                }

                response.failure = { resp, json ->
                    def statusCode = resp.statusLine.statusCode
                    binding.setVariable(JSON_RESPONSE_CODE_VARIABLE, statusCode)
                    binding.setVariable(JSON_RESPONSE_VARIABLE, json)
                }
            }
        } catch (ResponseParseException e) {
            throw new Exception("Unable to ")
        }
    }

    public void loadJsonRequest(def directory, def name) {
        binding.setVariable(JSON_REQUEST_VARIABLE, new JsonSlurper().parseText(FileUtil.loadFileResource(name, directory)));
    }

    public void setJsonProperty(String action, String path, String value) {
        String[] pathParts = path.split("\\.")
        def json = binding.getVariable(JSON_REQUEST_VARIABLE)

        def child = pathParts[-1]
        def parent = parseJsonExpression(pathParts, json)

        this."${action}"(parent, child, value)
    }

    public static def parseJsonExpression(String[] path, parent) {
        path[0..<path.length - 1].each { String pathPart ->
            def m = pathPart =~ /(.*)\[([0-9]*)\]/
            if (m) {
                if (m[0][1]) {
                    //support for arrays
                    parent = parent."${m[0][1]}"[m[0][2] as Integer]
                } else {
                    parent = parent[m[0][2] as Integer]
                }
            } else {
                parent = parent."$pathPart"
            }
        }
        return parent
    }

    public static def parseStringToType(String value, String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ") {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1)
        } else if (value == "today") {
            return new Date().clearTime().format(dateFormat)
        } else if (value == "tomorrow") {
            return (new Date() + 1).clearTime().format(dateFormat)
        } else if (value == "yesterday") {
            return (new Date() - 1).clearTime().format(dateFormat)
        } else if (value == "random") {
            return UUID.randomUUID() as String
        } else if (!value.isNumber()) {
            return value
        } else {
            if (value.contains(".")) {
                return value as Float
            } else {
                return value as Long
            }
        }
    }

    private void set(def parent, def child, String value) {
        parent."$child" = parseStringToType(value, dateFormat)
    }

    private void clear(def parent, def child, def value = null) {
        parent."$child" = ""
    }

    private void remove(def parent, def child, def value = null) {
        parent.remove(child)
    }

    private void nullify(def parent, def child, def value = null) {
        parent."$child" = null
    }

    private void add(def parent, def child, String value) {
        if (parent."$child" == null) {
            parent."$child" = new JSONArray()
        }

        if (parent."$child" instanceof List) {
            parent."$child".add(parseStringToType(value))
        }
    }
}
