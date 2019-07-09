

package com.douglasdb.camel.feat.core.splitter;

import com.douglasdb.camel.feat.core.aggregator.MyAggregationStrategy;

import org.apache.camel.builder.RouteBuilder;


/**
 * @author
 */
public class SplitterAggregateABCRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        
        from("direct:start")
            .split(body(), new MyAggregationStrategy())
                .log("Slip line ${body}")
                .bean(WordTranslateBean.class)
                .to("mock:split")
            .end()
            .log("Aggregated ${body}")
            .to("mock:result");

    }
}