
package com.douglasdb.camel.feat.core.aggregator;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author Douglas Db
 */
public class AggregatorEagerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
            .log("Sending ${body} with correlation key ${header.myId}")
            
            .aggregate(header("myId"), new MyEndAggregationStrategy())
                .log("Completed by ${property.CamelAggregatedCompletedBy}")
                .completionPredicate(body().isEqualTo("END"))
                    .eagerCheckCompletion()
                .log("Send out ${body}")
            .to("mock:result");
    }

}