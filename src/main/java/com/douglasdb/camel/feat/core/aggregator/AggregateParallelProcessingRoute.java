package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;

public class AggregateParallelProcessingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .aggregate(header("group"), new SetAggregationStrategy())
                .completionSize(10)
                .completionTimeout(400)
                .parallelProcessing()
                .log("${threadName} - processing output")
                .delay(500)
                .to("mock:out")
                .end();
    }
}
