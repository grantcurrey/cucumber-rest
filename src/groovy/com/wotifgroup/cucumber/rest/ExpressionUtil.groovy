package com.wotifgroup.cucumber.rest

/**
 * User: gcurrey
 * Date: 14/08/13
 * Time: 9:47 PM
 */
class ExpressionUtil {
    public static def parseGPathExpression(String[] path, def parent) {
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

    public static evaluateGPathExpression(String pathStr, def parent){
        def path = pathStr.split("\\.")
        def root = parseGPathExpression(path,parent)
        return root."${path[-1]}"
    }

    public static evaluateGPathExpression(String[] path, def parent){
        def root = parseGPathExpression(path,parent)
        return root."${path[-1]}"
    }

    public static def parseStringToType(String value, String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ", def binding = null) {
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
        } else if (value.startsWith("lastResponse") && binding != null) {
            return evaluateGPathExpression(value, binding)
        } else if (value.startsWith("lastResponseHeaders") && binding != null) {
            return evaluateGPathExpression(value, binding)
        } else {
            if (value.contains(".")) {
                return value as Float
            } else {
                return value as Long
            }
        }
    }
}
