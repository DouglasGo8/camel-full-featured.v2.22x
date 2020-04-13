package com.douglasdb.camel.feat.core.hystrix;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class HystrixRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
                .hystrix()
                    .to("bean:counter")
                .end()
                .log("After calling counter service: ${body}");

    }
}
