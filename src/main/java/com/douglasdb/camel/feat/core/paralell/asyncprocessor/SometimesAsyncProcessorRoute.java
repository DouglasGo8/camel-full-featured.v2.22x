package com.douglasdb.camel.feat.core.paralell.asyncprocessor;

import org.apache.camel.builder.RouteBuilder;


/**
 * @author dbatista
 */
public class SometimesAsyncProcessorRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("seda:in?concurrentConsumers=5")
                .to("direct:in")
                .log("Processed by: ${threadName}");

        from("direct:in")
                .log("Processing ${body}:${threadName}")
                .setHeader("initiatingThread", simple("${threadName}"))
                .process(new HeaderDrivenSlowOperationProcessor())
                .setHeader("completingThread", simple("${threadName}"))
                .log("Completed ${body}:${threadName}")
                .to("mock:out");
    }


}
