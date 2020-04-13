


package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;

public class SplitAggregateExceptionABCRoute extends RouteBuilder {

    /**
     * 
     */
    @Override
    public void configure() throws Exception {

        from("direct:start")
            .split(body(), new MyIgnoreFailureAggregationStrategy())
                .log("Split line ${body}")
                .bean(WordTranslateBean.class)
                .to("mock:split")
            .end()
            .log("Aggregated $}body}")
            .to("mock:result");
        
    }
}