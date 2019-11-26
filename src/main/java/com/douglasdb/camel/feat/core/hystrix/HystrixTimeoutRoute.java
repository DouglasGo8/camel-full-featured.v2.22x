package com.douglasdb.camel.feat.core.hystrix;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class HystrixTimeoutRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        from("direct:start")
                .hystrix()
                    .hystrixConfiguration().executionTimeoutInMilliseconds(2000)
                    .end()
                    .log("Hystrix processing start: ${threadName}")
                    //.toD("direct:${body}")
                    .log("Hystrix processing end: ${threadName}")
                .end()
                .log("After Hystrix ${body}");

        from("direct:fast")
                .log("Fast processing start: ${threadName}")
                .delay(1000)
                .transform().constant("Fast response")
                .log("Fast processing end: ${threadName}");

        from("direct:slow")
                .log("Slow processing start: ${threadName}")
                .delay(3000)
                .transform().constant("Slow response")
                .log("Slow processing end: ${threadName}");



    }
}
