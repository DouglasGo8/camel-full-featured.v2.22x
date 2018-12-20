package com.douglasdb.camel.feat.core.routing;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author Administrator
 */
public class MulticastStopOnExceptionRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
                .multicast()
                .stopOnException()
                .to("direct:first")
                .to("direct:second")
                .end()
                .log("continuing with ${body}")
                .to("mock:afterMulticast");

        from("direct:first")
                .onException(Exception.class)
                    .handled(true)
                    .log("Caught Exception")
                    .to("mock:exceptionHandler")
                    .transform(constant("Oops"))
                .end()
                .to("mock:first")
                .process(exchange -> {
                   throw new IllegalStateException("Something went horribly wrong!!!");
                });


        from("direct:second")
                .to("mock:second")
                .transform(constant("All OK here"));
    }
}
