

package com.douglasdb.camel.feat.core.loadbalancer;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 */
public class FailoverRoundRobinLoadBalancerRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
            .loadBalance()
                /**
                 * using load balancer with failover strategy
                 * (1) means that will try 1 failover attempt before exhausting
                 * (false) means that do not use Camel Exception handling
                 * (true) means use round robin strategy
                 */
                .failover(1, false, true)
                 // will send to A first, and if fails then send to B afterwards
                .to("direct:a")
                .to("direct:b")
            .end();


        from("direct:a")
            .log("A received: ${body}")
            .choice()
                .when(body().contains("Kaboom"))
                    .throwException(new IllegalArgumentException("Kill the Bozo"))
                .end()
            .end()
            .to("mock:a");

        from("direct:b")
            .log("B received: ${body}")
            .choice()
                .when(body().contains("Boom"))
                    .throwException(new IllegalArgumentException("Damn"))
                .end()
            .end()
            .to("mock:b");
    }
    
}