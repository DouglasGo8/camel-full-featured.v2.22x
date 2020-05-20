package com.douglasdb.camel.feat.core.paralell.asyncprocessor;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class AsyncProcessorRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("seda:in?concurrentConsumers=5")
                .log("${body}")
                .to("direct:in")
                .log("Processed by: ${threadName}");

        from("direct:in")
                .log("Processing ${body}:${threadName}")
                .process(new SlowOperationProcessor())
                .log("Completed ${body}:${threadName}")
                .to("mock:out");


        from("direct:sync?synchronous=true")
                .log("Processing ${body}:${threadName}")
                .process(new SlowOperationProcessor())
                .log("Completed ${body}:${threadName}")
                .to("mock:out");


    }
}
