package com.douglasdb.camel.feat.core.errorhandling.onexception;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class OnExceptionDirectMatchRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onException(OrderFailedException.class)
                .maximumRedeliveries(3);

        from("direct:order")
                .bean(OrderServiceBean.class, "handleOrder")
                .to("mock:done");


    }
}
