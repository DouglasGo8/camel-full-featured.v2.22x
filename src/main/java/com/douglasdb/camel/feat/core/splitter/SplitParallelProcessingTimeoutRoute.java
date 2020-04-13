package com.douglasdb.camel.feat.core.splitter;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SplitParallelProcessingTimeoutRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .split(body())
                    .parallelProcessing()
                    .timeout(1000)
                .log("Processing message[${property.CamelSplitIndex}]")
                .to("direct:delay20th")
                .end()
                .to("mock:out");

        from("direct:delay20th")
                .choice()
                    .when(simple("${property.CamelSplitIndex} == 20"))
                        .to("direct:longDelay")
                    .otherwise()
                        .to("mock:split")
                .end();

        from("direct:longDelay")
                .delay(1500)
                .to("mock:delayed");
    }
}
