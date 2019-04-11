
package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 */
public class AggregateABCCloseRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
            .log("Sending ${body} with correlation key ${header.myId}")
            .aggregate(header("myId"), new MyAggregationStrategy())
                .completionSize(3)
                .closeCorrelationKeyOnCompletion(2000)
            .log("Sending out ${body}")
        .to("mock:result");
    }

}