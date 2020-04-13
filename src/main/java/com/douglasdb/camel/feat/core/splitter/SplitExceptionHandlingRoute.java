package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SplitExceptionHandlingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .split(simple("${body}"))
                .process(exchange -> {
                    if (exchange.getProperty("CamelSplitIndex").equals(0))
                        throw new IllegalStateException("boom");
                } )
                .log("Processing fragment[${property[CamelSplitIndex]}]:${body}")
                .to("mock:split")
                .end()
                .to("mock:out");
    }
}
