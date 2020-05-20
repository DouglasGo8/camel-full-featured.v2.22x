package com.douglasdb.camel.feat.core.routing.cookbook;

import org.apache.camel.builder.RouteBuilder;


/**
 * @author Administrator
 */
public class MulticastShallowCopyRoute extends RouteBuilder {


    public MulticastShallowCopyRoute() {
        // TODO:  Auto-generated constructor stub
    }
    /**
     *
     * @throws Exception
     */
    @Override
    public void configure() throws Exception {

        from("direct:start")
                .multicast()
                    .to("direct:first")
                    .to("direct:second")
                .end()
                .to("mock:afterMulticast");

        from("direct:first")
                .setHeader("firstModifies")
                    .constant("apple")
                .setHeader("threadName").simple("${threadName}")
                    .to("mock:first");

        from("direct:second")
                .setHeader("secondModifies")
                    .constant("banana")
                .setHeader("threadName")
                    .simple("${threadName}")
                .to("mock:second");

    }
}
