package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class AggregateCompletionTimeoutRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:in")
                .log("${threadName} - ${body}")
                .aggregate(header("group"), new SetAggregationStrategy())
                .completionSize(10).completionTimeout(1000)
                .log("${threadName} - out")
//                .delay(500)
                .to("mock:out")
                .end();
    }
}
