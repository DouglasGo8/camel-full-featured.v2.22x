package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;

import java.util.concurrent.ScheduledExecutorService;

/**
 *
 */
public class AggregatorTimeoutThreadPoolRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {


        final ScheduledExecutorService threadPool = super.getContext()
                .getExecutorServiceManager()
                .newScheduledThreadPool(this, "MyThreadPool", 2);

        from("direct:start")
                .log("Sending ${body}")
                .aggregate(xpath("/order/@customer"), new MyAggregationStrategy())
                    .completionSize(2) // NoK
                    .completionTimeout(5000) //Oak
                .timeoutCheckerExecutorService(threadPool)
                .log("Sending out ${body}")
                .to("mock:result");



    }
}
