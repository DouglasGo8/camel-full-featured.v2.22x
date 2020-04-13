
package com.douglasdb.camel.feat.core.loadbalancer;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 */
public class LoadBalancerRoundRobin extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
            // use load balancer with round robin strategy
            .loadBalance().roundRobin()
            .to("seda:a").to("seda:b")
        .end();

        from("seda:a")
            .log("A received: ${body}")
            .to("mock:a");

        from("seda:b")
            .log("B received: ${body}")
            .to("mock:b");

    }
}