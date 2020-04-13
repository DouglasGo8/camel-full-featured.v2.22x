package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;

import java.util.concurrent.Executors;

public class AggregateExecutorServiceRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {


        from("direct:in")
                .aggregate(header("group"), new SetAggregationStrategy())
                    .completionSize(10)
                    .completionTimeout(400)
                    .executorService(Executors.newFixedThreadPool(20))
                .log("${threadName} - processing output")
                .delay(500)
                .to("mock:out")
                .end();

    }
}
