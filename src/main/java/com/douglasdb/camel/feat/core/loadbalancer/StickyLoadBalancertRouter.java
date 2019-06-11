
package com.douglasdb.camel.feat.core.loadbalancer;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 */
public class StickyLoadBalancertRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
            .loadBalance()
                .sticky(header("type"))
                .to("seda:a", "seda:b")
            .end();

        from("seda:a")
            .log("A received: ${body}")
            .to("mock:a");

        from("seda:b")
            .log("B received: ${body}")
            .to("mock:b");
    }

}