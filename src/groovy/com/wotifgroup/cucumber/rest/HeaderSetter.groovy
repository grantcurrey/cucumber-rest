package com.wotifgroup.cucumber.rest

/**
 * User: gcurrey
 * Date: 12/08/13
 * Time: 2:49 PM
 */
class HeaderSetter extends Closure {
    private CucumberRest cucumberRest

    HeaderSetter(Object theTarget, CucumberRest cucumberRest) {
        super(theTarget)
        this.cucumberRest = cucumberRest
    }

    protected doCall(Object[] args) {
        cucumberRest.setHeader(args[0] as String, args[1] as String)
    }
}