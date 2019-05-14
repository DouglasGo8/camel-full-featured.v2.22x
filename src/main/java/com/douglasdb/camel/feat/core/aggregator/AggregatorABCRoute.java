

package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author Douglas Db
 */
public class AggregatorABCRoute extends RouteBuilder {

    
    @Override
    public void configure() throws Exception {

        from("direct:start")
            .log("*** Sending ${body} with correlation key ${header.correlationId} ***")
            .aggregate(header("correlationId"), new MyAggregationStrategy())
                .completionSize(3)
            .log("Sending out ${body}")
            .to("mock:result")
        .end();
    }

    
}