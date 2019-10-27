package com.douglasdb.camel.feat.core.errorhandling.reuse;

/**
 *
 */
public class OrderRouteBuilder extends BaseRouteBuilder {

    @Override
    public void configure() throws Exception {
        super.configure();// must call super to reuse the common error handler


        // route to process the order
        from("seda:queue.inbox")
                .bean("orderService", "validate")
                .bean("orderService", "enrich")
                .to("mock:queue.order");
    }
}
