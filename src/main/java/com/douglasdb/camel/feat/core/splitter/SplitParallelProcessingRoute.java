package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;

import java.util.concurrent.Executors;

/**
 * @author dbatista
 */
public class SplitParallelProcessingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .split(body())
                    .parallelProcessing()
                        .executorService(Executors.newSingleThreadExecutor())
                    .log("Processing message[${property.CamelSplitIndex}]")
                    .to("mock:split")
                .end()
                .to("mock:out");
    }
}
