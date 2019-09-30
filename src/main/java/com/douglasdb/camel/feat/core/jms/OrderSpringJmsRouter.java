package com.douglasdb.camel.feat.core.jms;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class OrderSpringJmsRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("file://F:/.camel/data/inbox?noop=true")
                .log("${header.CamelFileName}")
                .to("mock:end");



    }
}
