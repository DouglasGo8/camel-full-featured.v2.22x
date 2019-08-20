package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SplitNaturalRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("direct:in")
                .split(body())
                    .to("mock:split")
                .end()
                .to("mock:out");
    }
}
