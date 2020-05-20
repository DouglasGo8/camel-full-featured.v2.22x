package com.douglasdb.camel.feat.core.errorhandling.onexception;

import org.apache.camel.builder.RouteBuilder;

public class OnExceptionFallbackRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        super.getContext().setTracing(true);

        onException(IllegalArgumentException.class).maximumRedeliveries(3);

        from("direct:order")
                .bean(OrderServiceBean.class, "handleOrder")
                .bean(OrderServiceBean.class, "saveToDB");
    }
}
