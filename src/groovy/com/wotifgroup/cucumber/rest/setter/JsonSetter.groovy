package com.wotifgroup.cucumber.rest.setter

import net.sf.json.JSONArray

/**
 * User: gcurrey
 * Date: 8/08/13
 * Time: 2:16 PM
 */
class JsonSetter extends Setter{
    public void set(def parent, def child, String value, def settings = [:]) {
        parent."$child" = parseStringToType(value, settings.dateFormat)
    }

    public void clear(def parent, def child, def value = null, def settings = [:]) {
        parent."$child" = ""
    }

    public void remove(def parent, def child, def value = null, def settings = [:]) {
        parent.remove(child)
    }

    public void nullify(def parent, def child, def value = null, def settings = [:]) {
        parent."$child" = null
    }

    public void add(def parent, def child, String value, def settings = [:]) {
        if (parent."$child" == null) {
            parent."$child" = new JSONArray()
        }

        if (parent."$child" instanceof List) {
            parent."$child".add(parseStringToType(value))
        }
    }
}
