package com.wotifgroup.cucumber.rest

/**
 * User: gcurrey
 * Date: 19/06/12
 * Time: 10:37 AM
 */
class PropertySetter extends Closure {
    private CucumberRest cucumberRest

    PropertySetter(Object theTarget, CucumberRest cucumberRest) {
        super(theTarget)
        this.cucumberRest = cucumberRest
    }

    protected doCall(Object[] args) {
        cucumberRest.setJsonProperty(args[0] as String, args[1] as String, args.length == 3 ? args[2] : null)
    }
}