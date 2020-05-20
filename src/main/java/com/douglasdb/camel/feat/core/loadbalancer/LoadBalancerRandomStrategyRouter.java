
package com.douglasdb.camel.feat.core.loadbalancer;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 */
public class LoadBalancerRandomStrategyRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
        // use load balancer with round robin strategy
           .loadBalance().random()
           .to("seda:a", "seda:b", "seda:c")
       .end();

       from("seda:a")
            .log("A received: ${body}");

       from("seda:b")
            .log("B received: ${body}");
               
        from("seda:c")
            .log("C received: ${body}");

    }
}