

package com.douglasdb.camel.feat.core.splitter;

import com.douglasdb.camel.feat.core.aggregator.MyAggregationStrategy;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 */
public class SplitStopOnExceptionRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        
        from("direct:start")
            .split(body(),  new MyAggregationStrategy())
                .stopOnException()
            .log("Split line ${body}")
            .bean(WordTranslateBean.class)
            .to("mock:split")
            .end()
         .log("Aggregated ${body}")
         .to("mock:result");

        
    }
}