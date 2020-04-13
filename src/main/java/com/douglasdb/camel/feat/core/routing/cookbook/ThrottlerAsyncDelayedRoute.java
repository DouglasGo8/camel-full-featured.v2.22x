package com.douglasdb.camel.feat.core.routing.cookbook;

import org.apache.camel.builder.RouteBuilder;

public class ThrottlerAsyncDelayedRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        from("direct:start")
                .to("mock:unthrottled")
                .throttle(5)
                    .timePeriodMillis(2000)
                        .asyncDelayed()
                        .executorServiceRef("myThrottler")
                .setHeader("threadName", simple("${threadName}"))
                .to("mock:throttled")
                .end()
                .to("mock:after");
    }
}
