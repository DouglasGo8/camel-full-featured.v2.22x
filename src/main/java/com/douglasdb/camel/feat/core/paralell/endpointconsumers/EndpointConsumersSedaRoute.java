package com.douglasdb.camel.feat.core.paralell.endpointconsumers;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class EndpointConsumersSedaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("seda:in?concurrentConsumers=10")
                .delay(200)
                .log("Processing ${body}:${threadName}")
                .to("mock:out");
    }
}
