package com.douglasdb.camel.feat.core.loadbalancer;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class LoadBalancerCustomStrategyRouter extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("direct:start")
            .loadBalance(new MyCustomLoadBalancer())
                .to("seda:a")
                .to("seda:b")
            .end();

        from("seda:a")
                .log("A received: ${body}")
                .to("mock:a");


        from("seda:b")
                .log("B received: ${body}")
                .to("mock:b");

    }
}
