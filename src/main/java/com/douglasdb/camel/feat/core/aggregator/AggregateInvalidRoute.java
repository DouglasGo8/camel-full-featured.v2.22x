

package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 */
public class AggregateInvalidRoute extends RouteBuilder {



    @Override
    public void configure() throws Exception {


        from("direct:start")
            .log("Sending ${body} with correlation key ${header.myId}")
            .aggregate(header("myId"), new MyAggregationStrategy())
                .completionSize(3)
                .ignoreInvalidCorrelationKeys()
            .log("Sending out ${body}")
            .to("mock:result");

    }



}