package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;

import java.util.concurrent.Executors;

/**
 * @author dbatista
 */
public class SplitExecutorServiceRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:in")
                .split(body())
                    .executorService(Executors.newFixedThreadPool(20))
                .log("Processing message[${property.CamelSplitIndex}]")
                .to("mock:split")
                .end()
                .to("mock:out");
    }
}
