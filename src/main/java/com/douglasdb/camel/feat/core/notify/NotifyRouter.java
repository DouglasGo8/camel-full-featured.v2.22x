package com.douglasdb.camel.feat.core.notify;

import org.apache.camel.builder.RouteBuilder;

public class NotifyRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("seda:order")
                .choice()
                    .when(method(OrderService.class, "validateOrder"))
                        .bean(OrderService.class, "processOrder")
                        .log("Ok Order")
                        .to("seda:confirm")
                    .otherwise()
                        .log("NOk Order")
                        .to("seda:invalid")
                .end();

        from("seda:quote")
                .delay(2000)
                .to("log:quote");

    }
}
