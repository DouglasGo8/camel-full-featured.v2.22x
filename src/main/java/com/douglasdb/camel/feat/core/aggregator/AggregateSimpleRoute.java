package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class AggregateSimpleRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .log("${threadName} - ${body}")
                .aggregate(header("group"), new SetAggregationStrategy())
                .completionSize(5)
                .log("${threadName} - out")
                .to("mock:out")
        .end();

    }
}

