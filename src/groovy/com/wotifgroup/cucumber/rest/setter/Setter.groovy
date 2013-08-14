package com.wotifgroup.cucumber.rest.setter

import com.wotifgroup.cucumber.rest.ExpressionUtil
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
}
