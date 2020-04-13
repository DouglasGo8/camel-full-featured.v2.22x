package com.douglasdb.camel.feat.core.seda;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class SedaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("file:///F:/.camel/data/inbox?noop=true")
                .to("seda:incomingOrders");

        from("seda:incomingOrders")
                .choice()
                    .when(header("CamelFileName").endsWith(".xml"))
                        .to("seda:xmlOrders")
                    .when(header("CamelFileName").endsWith(".csv"))
                        .to("seda:csvOrders");

        from("seda:xmlOrders?multipleConsumers=true").to("jms:accounting");
        from("seda:csvOrders?multipleConsumers=true").to("jms:production");

        from("jms:accounting")
                .process(exchange -> {
                    System.out.println("Accounting received order: "
                            + exchange.getIn().getHeader("CamelFileName"));
                })
                .to("mock:accounting");

        from("jms:production")
                .process(exchange -> {
                    System.out.println("Production  received order: "
                            + exchange.getIn().getHeader("CamelFileName"));
                })
                .to("mock:production");

    }
}
