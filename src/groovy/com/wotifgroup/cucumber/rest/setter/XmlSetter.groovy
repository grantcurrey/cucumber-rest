package com.wotifgroup.cucumber.rest.setter

import static com.wotifgroup.cucumber.rest.ExpressionUtil.parseStringToType

/**
 * User: gcurrey
 * Date: 8/08/13
 * Time: 2:16 PM
 */
class XmlSetter extends Setter {
    @Override
    void set(parent, child, String value, def settings = [:]) {
        parent."${child}" = parseStringToType(value, settings.dateFormat)
    }

    @Override
    void clear(parent, child, value, def settings = [:]) {
        parent."${child}" = null;
    }

    @Override
    void remove(parent, child, value, def settings = [:]) {
        parent."${child}".replaceNode {}
    }

    @Override
    void nullify(parent, child, value, def settings = [:]) {
        parent."${child}" = null;
    }

    @Override
    void add(parent, child, String value, def settings = [:]) {
        parent.appendNode {
            "${child}"(parseStringToType(value, settings.dateFormat))
        }
    }
}
