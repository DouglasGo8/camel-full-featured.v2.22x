package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class AggregateCompletionIntervalRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .log("${threadName} - ${body}")
                .aggregate(header("group"), new SetAggregationStrategy())
                    .completionSize(10)
                    .completionInterval(400)
                .log("${threadName} - out")
                .delay(500)
                .to("mock:out")
                .end();
    }
}
