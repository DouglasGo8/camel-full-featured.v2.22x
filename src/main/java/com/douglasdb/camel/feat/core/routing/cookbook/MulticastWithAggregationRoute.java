package com.douglasdb.camel.feat.core.routing.cookbook;

import org.apache.camel.builder.RouteBuilder;

public class MulticastWithAggregationRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {



        from("direct:start")
                .multicast()
                .aggregationStrategy(MulticastWithAggregationOfRequestRoute::aggregate)
                .to("direct:first")
                .to("direct:second")
                .end()
                .transform(body());


        from("direct:first")
                .transform(constant("first response"));

        from("direct:second")
                .transform(constant("second response"));
    }
}
