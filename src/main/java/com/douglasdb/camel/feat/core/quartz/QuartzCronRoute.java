package com.douglasdb.camel.feat.core.quartz;

import org.apache.camel.builder.RouteBuilder;

public class QuartzCronRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("quartz2://report?cron=0/2+*+*+*+*+?")
                .setBody().simple("I was fired at ${header.fireTime}")
                .to("stream:out")
                .to("mock:end");
    }
}
