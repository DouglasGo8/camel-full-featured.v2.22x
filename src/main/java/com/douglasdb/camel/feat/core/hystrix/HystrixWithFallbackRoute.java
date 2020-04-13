package com.douglasdb.camel.feat.core.hystrix;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class HystrixWithFallbackRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
                .hystrix()
                    .to("bean:counter")
                .onFallback()
                    .transform(constant("No Counter Ready"))
                .end()
                .log("After calling counter service: ${body}");

    }
}
