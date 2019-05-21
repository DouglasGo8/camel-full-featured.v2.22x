package com.douglasdb.camel.feat.core.transform.simple;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class SimpleRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("direct:start")
            .transform(simple("Hello ${body}"));
    }
}
