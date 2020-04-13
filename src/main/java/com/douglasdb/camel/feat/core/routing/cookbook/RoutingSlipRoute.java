package com.douglasdb.camel.feat.core.routing.cookbook;

import org.apache.camel.builder.RouteBuilder;

public class RoutingSlipRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("direct:start")
                .routingSlip(header("myRoutingSlipHeader"));

        from("direct:other")
                .to("mock:other");
    }
}
