package com.wotifgroup.cucumber.rest.setter

import groovyx.net.http.ContentType
import net.sf.json.JSONArray

/**
 * User: gcurrey
 * Date: 8/08/13
 * Time: 2:17 PM
 */
abstract class Setter {

    public static final XmlSetter XML_SETTER = new XmlSetter();
    public static final JsonSetter JSON_SETTER = new JsonSetter();

    public static Setter getSetter(def type) {
        if (type == ContentType.XML) {
            return XML_SETTER
        } else {
            return JSON_SETTER
        }
    }

    public abstract void set(def parent, def child, String value, def settings);

    public abstract void clear(def parent, def child, def value, def settings);

    public abstract void remove(def parent, def child, def value, def settings);

    public abstract void nullify(def parent, def child, def value, def settings);

    public abstract void add(def parent, def child, String value, def settings);

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
}
