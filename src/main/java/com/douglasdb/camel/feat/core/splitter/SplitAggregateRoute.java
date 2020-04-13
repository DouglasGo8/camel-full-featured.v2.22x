package com.douglasdb.camel.feat.core.splitter;

import com.douglasdb.camel.feat.core.aggregator.SetAggregationStrategy;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SplitAggregateRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:in")
                .split(body(), new SetAggregationStrategy())
                    .inOut("direct:out")
                .end()
                .to("mock:out");

        from("direct:out")
                .transform(simple("Processed: ${body}"));


    }
}
