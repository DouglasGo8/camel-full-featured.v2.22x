package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SplitSimpleExpressionRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("direct:in")
                .log("${body.wrapped}")
                .split(simple("${body.wrapped}"))
                .to("mock:out")
                .end();
    }
}
