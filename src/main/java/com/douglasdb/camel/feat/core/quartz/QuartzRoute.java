package com.douglasdb.camel.feat.core.quartz;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class QuartzRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("quartz2://myTimer?trigger.repeatInterval=2000&trigger.repeatCount=-1")
                .setBody().simple("I was fried at ${header.fireTime}")
                .to("stream:out")
                .to("mock:end");
    }
}
