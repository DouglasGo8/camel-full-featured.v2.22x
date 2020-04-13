package com.douglasdb.camel.feat.core.paralell.threadsdsl;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class ThreadsDslRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .log("Received ${body}:${threadName}")
                .threads(5, 100)
                .delay(1000)
                .log("Processing ${body}:${threadName}")
                .to("mock:out");

        from("direct:inOut")
                .log("Received ${body}:${threadName}")
                .threads()
                .delay(200)
                .log("Processing ${body}:${threadName}")
                .to("mock:out")
                .transform(constant("Processed"));

    }
}
