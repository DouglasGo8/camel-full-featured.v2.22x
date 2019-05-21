package com.douglasdb.camel.feat.core.routing.cookbook;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class MulticastRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("direct:start")
                .multicast()
                    .to("mock:first")
                    .to("mock:second")
                    .to("mock:third")
                .end();

    }
}
