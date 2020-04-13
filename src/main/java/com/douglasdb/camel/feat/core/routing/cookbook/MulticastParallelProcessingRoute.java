package com.douglasdb.camel.feat.core.routing.cookbook;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author Administrator
 */
public class MulticastParallelProcessingRoute extends RouteBuilder {


    public MulticastParallelProcessingRoute() {

        // TODO: auto-generated constructor stub
    }

    @Override
    public void configure() throws Exception {

        from ("direct:start")
                .multicast()
                .parallelProcessing()
                    .to("direct:first")
                    .to("direct:second")
                .end()
                .setHeader("threadName")
                .simple("${threadName}")
                .to("mock:afterMulticast")
                .transform(constant("response"));

        from("direct:first")
                .setHeader("firstModifies").constant("apple")
                .setHeader("threadName").simple("${threadName}")
                .to("mock:first");

        from("direct:second")
                .setHeader("secondModifies").constant("banana")
                .setHeader("threadName").simple("${threadName}")
                .to("mock:second");



    }
}
