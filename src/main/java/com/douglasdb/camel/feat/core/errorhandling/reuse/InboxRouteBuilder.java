package com.douglasdb.camel.feat.core.errorhandling.reuse;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class InboxRouteBuilder extends BaseRouteBuilder  {

    @Override
    public void configure() throws Exception {
        // must call super to reuse the common error handler
        super.configure();

        from("file://F:/.camel/data/orders?delay=10000")
                .bean("orderService", "toCsv")
                .to("mock:file")
                .to("seda:queue.inbox");
    }
}
