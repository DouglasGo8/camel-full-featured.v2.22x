package com.douglasdb.camel.feat.core.errorhandling.onexception;

import org.apache.camel.builder.RouteBuilder;

import java.io.IOException;
import java.net.ConnectException;

/**
 *
 */
public class OnExceptionGapRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onException(ConnectException.class).maximumRedeliveries(5);
        onException(IOException.class).maximumRedeliveries(3).redeliveryDelay(1000);
        onException(Exception.class).maximumRedeliveries(1).redeliveryDelay(5000);

              from("direct:order")
                .bean(OrderServiceBean.class, "handleOrder")
                .bean(OrderServiceBean.class, "enrichFromFile")
                .to("mock:fail");


    }

}
