package com.douglasdb.camel.feat.core.scheduler;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SchedulerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("scheduler://myScheduler?delay=2000")
                .setBody().simple("Current time is ${header.CamelTimerFiredTime}")
                .to("stream:out")
                .to("mock:end");
    }
}
