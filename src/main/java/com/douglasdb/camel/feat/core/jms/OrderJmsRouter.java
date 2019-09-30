package com.douglasdb.camel.feat.core.jms;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class OrderJmsRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("jms:incomingOrders")
                .inOut("jms:validate");

        from("jms:validate")
                .bean(ValidatorBean.class);


    }
}
