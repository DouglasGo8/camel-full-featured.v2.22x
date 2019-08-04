package com.douglasdb.camel.feat.core.aggregator;

import lombok.NoArgsConstructor;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
@NoArgsConstructor
public class AggregateCompletionConditionRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .log("${threadName} - ${body}")
                .aggregate(header("group"), new SetAggregationStrategy())
                    .completionPredicate(simple("${body.size} == 5"))
                    .log("${threadName} - out")
                    .to("mock:out")
                .end();
    }
}
