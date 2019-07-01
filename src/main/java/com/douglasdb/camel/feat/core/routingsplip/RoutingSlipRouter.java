

package com.douglasdb.camel.feat.core.routingsplip;

import org.apache.camel.builder.RouteBuilder;

public class RoutingSlipRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
            .log("${body}")
            .bean(SlipBean.class);
    }
    
}