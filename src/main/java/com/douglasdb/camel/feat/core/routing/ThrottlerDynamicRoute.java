package com.douglasdb.camel.feat.core.routing;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class ThrottlerDynamicRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {


        from("direct:start")
                .to("mock:unthrottled")
                    .throttle(header("throttleHeaderRate"))
                        .timePeriodMillis(10000)
                    .to("mock:throttled")
                    .end()
                .to("mock:after");


    }
}
