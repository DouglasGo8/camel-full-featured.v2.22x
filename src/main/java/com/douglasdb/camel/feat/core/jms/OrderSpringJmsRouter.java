package com.douglasdb.camel.feat.core.jms;

import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class OrderSpringJmsRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("file://D:/.camel/data/inbox?noop=true")
                .log("${header.CamelFileName}")
                //.to("jms:incomingOrders");
                .to("mock:end");

        /*from("jms:incomingOrders")
                .log("${body}")
                .choice()
                    .when(header("CamelFilename").endsWith(".xml"))
                        .to("jms:topic:xmlOrders")
                    .when(header("CamelFileName").endsWith(".csv"))
                        .to("jms:topic.csvOrders");

        from("jms:topic:xmlOrders")
                .to("jms:accounting");
        */
       // from("jms:topic:csvOrders")
         //       .to("jms:production");

        /*from("jms:accounting")
                .process(exchange -> System.out.println("Accounting received order: "
                        + exchange.getIn().getHeader("CamelFileName")));*/

        //from("jms:production")
         //       .process(exchange -> System.out.println("Production received order: "
          //              + exchange.getIn().getHeader("CamelFileName")));


    }
}
