package com.douglasdb.camel.feat.core.java;

import org.apache.camel.builder.RouteBuilder;

public class SimpleTransformRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:in").transform(simple("Modified: ${body}")).to("mock:out");
    }
}
