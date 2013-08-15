package com.wotifgroup.cucumber.rest

import com.wotifgroup.cucumber.rest.setter.Setter
import groovy.util.slurpersupport.GPathResult
import groovy.xml.XmlUtil
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.ResponseParseException
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.ssl.SSLSocketFactory

import java.security.KeyStore

import static groovyx.net.http.Method.*

class CucumberRest {

    static final REQUEST_VARIABLE = "request"
    static final REQUEST_HEADERS_VARIABLE = "requestHeaders"
    static final REQUEST_TYPE = "requestType"
    static final RESPONSE_CODE_VARIABLE = "responseCode"
    static final RESPONSE_VARIABLE = "response"
    static final RESPONSE_HEADERS = "responseHeaders"

    private def trustStore
    private def keyStore
    private def keyStorePassword

    private Binding binding

    String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ"

    public CucumberRest(Binding binding) {
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

    public void doRequest(def methodString, def urlBase, def path) {
        def http = new HTTPBuilder("${urlBase}${path}")

        def factory = new SSLSocketFactory(keyStore, keyStorePassword, trustStore)
        factory.hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
        http.client.connectionManager.schemeRegistry.register(new Scheme("https", factory, 443))

        def method = POST
        if (methodString == "post") {
            method = POST
        } else if (methodString == "put") {
            method = PUT
        } else if (methodString == "delete") {
            method = DELETE
        } else if (methodString == "get") {
            method = GET
        }

        def requestBody = binding.getVariable(REQUEST_VARIABLE);
        def requestHeaders = binding.getVariable(REQUEST_HEADERS_VARIABLE) as Map
        def type = binding.getProperty(REQUEST_TYPE)

        if (type == ContentType.XML) {
            requestBody = XmlUtil.serialize(requestBody)
        }

        try {
            http.request(method, type) { req ->
                if (requestHeaders) {
                    headers = requestHeaders
                }

                if (method == POST || method == PUT) {
                    body = requestBody
                }

                response.success = { resp, data ->
                    def statusCode = resp.statusLine.statusCode
                    binding.setVariable(RESPONSE_CODE_VARIABLE, statusCode)
                    binding.setVariable(RESPONSE_VARIABLE, data)
                    binding.setVariable(RESPONSE_HEADERS, resp.headers)
                }

                response.failure = { resp, data ->
                    def statusCode = resp.statusLine.statusCode
                    binding.setVariable(RESPONSE_CODE_VARIABLE, statusCode)
                    binding.setVariable(RESPONSE_VARIABLE, data)
                    binding.setVariable(RESPONSE_HEADERS, resp.headers)
                }
            }
        } catch (ResponseParseException e) {
            throw new Exception("Unable to ")
        }
    }

    public void loadRequest(def directory, def name, def slurper, def type) {
        binding.setVariable(REQUEST_VARIABLE, slurper.parseText(FileUtil.loadFileResource(name, directory)));
        binding.setVariable(REQUEST_TYPE, type)
    }

    public void setHeader(String headerName, String value) {
        if (!headerName && !value) {
            binding.setVariable(REQUEST_HEADERS_VARIABLE, null)
        } else {
            if (!binding.hasVariable(REQUEST_HEADERS_VARIABLE)) {
                binding.setVariable(REQUEST_HEADERS_VARIABLE, [:])
            }

            def headers = binding.getVariable(REQUEST_HEADERS_VARIABLE)
            binding.setVariable(REQUEST_HEADERS_VARIABLE, headers)
            headers."$headerName" = value
        }
    }

    public void setProperty(String action, String path, String value) {
        String[] pathParts = path.split("\\.")
        def document = binding.getVariable(REQUEST_VARIABLE)
        def type = binding.getVariable(REQUEST_TYPE)

        def child = pathParts[-1]
        def parent = ExpressionUtil.parseGPathExpression(pathParts, document)

        Setter.getSetter(type)."${action}"(parent, child, value, [dateFormat: dateFormat, binding: binding])
    }
}
