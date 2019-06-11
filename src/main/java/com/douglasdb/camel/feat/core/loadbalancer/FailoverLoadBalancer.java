

package com.douglasdb.camel.feat.core.loadbalancer;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 */
public class FailoverLoadBalancer extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        from("direct:start")
            .loadBalance()
                .failover()
                 // will send to A first, and if fails then send to B afterwards
                .to("direct:a")
                .to("direct:b")
            .end();


        from("direct:a")
            .log("Receiving message: ${body}")
            .choice()
                .when(body().contains("Kaboom"))
                    .throwException(new IllegalArgumentException("Kill the Bozo"))
                .end()
            .end()
            .to("mock:a");

        from("direct:b")
            .log("B received: ${body}")
            .to("mock:b");

    }
    
}