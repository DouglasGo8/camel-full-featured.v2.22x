package com.douglasdb.camel.feat.core.debug;

import org.apache.camel.builder.RouteBuilder;

public class DebugRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start").transform(simple("Debug ${body}"))
                .log("${body}")
                .to("mock:result");

    }
}
