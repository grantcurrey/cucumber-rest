package com.wotifgroup.cucumber.rest

import org.junit.Test

/**
 * User: gcurrey
 * Date: 14/08/13
 * Time: 10:06 PM
 */
class ExpressionUtilTest {
    @Test
    void bindingResponseHeadersKeywordShouldGiveAValue(){
        def binding = [lastResponseHeaders:["x-onBehalfOf":"test"]]
        assert (ExpressionUtil.evaluateGPathExpression("lastResponseHeaders.x-onBehalfOf",binding) == "test")
    }
}
