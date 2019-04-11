
package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedExchangeAggregationStrategy;


/**
 * 
 */
public class AggregateGroupRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
            .log("Sending ${body} with correlation key ${header.myId}")
            .aggregate(header("myId"), new GroupedExchangeAggregationStrategy())
                .completionSize(3)
            .log("Sending out ${body}")
            .to("mock:result");

    }

}