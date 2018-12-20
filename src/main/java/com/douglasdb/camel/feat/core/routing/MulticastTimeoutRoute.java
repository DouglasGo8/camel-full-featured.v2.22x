package com.douglasdb.camel.feat.core.routing;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class MulticastTimeoutRoute extends RouteBuilder {


    /**
     *
     */
    public MulticastTimeoutRoute() {
        // TODO:
    }

    @Override
    public void configure() throws Exception {

        from("direct:start")
            .multicast().parallelProcessing().timeout(3000)
                .to("direct:first")
                .to("direct:second")
            .end()
            .setHeader("threadName").simple("${threadName}")
            .to("mock:afterMulticast")
            .transform(constant("response"));


        from("direct:first")
            .setHeader("firstModifies").constant("apple")
            .setHeader("threadName").simple("${threadName}")
            .to("mock:first");

        from("direct:second")
            .onCompletion()
                .onWhen(header("timedOut").isNull())
                .log("operation rolling back")
            .end()

            .setHeader("secondModifies").constant("banana")
            .setHeader("threadName").simple("${threadName}")
                .log("===> ${header.threadName}")
            .delay(5000)
            .to("mock:second")
                .filter(exchangeProperty("CamelMulticastComplete").isEqualTo(false))
                //.log("timedOut Header")
            .setHeader("timedOut", constant("false"));

    }
}
