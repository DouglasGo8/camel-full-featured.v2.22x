
package com.douglasdb.camel.feat.core.loadbalancer;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 */
public class TopicLoadBalancerRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
    	
        from("direct:start")
                // use load balancer with topic strategy
            .loadBalance().topic()
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
