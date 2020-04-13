package com.douglasdb.camel.feat.core.transactions.idempotentconsumer;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;

public class IdempotentConsumerMultipleEndpointsRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        from("direct:in")
                .log("Received message ${header[messageId]}")
                .enrich("direct:invokeWs")
                .log("Completing")
                .to("mock:out");

        from("direct:invokeWs")
                .idempotentConsumer(header("messageId"), new MemoryIdempotentRepository())
                .log("Invoking WS")
                .to("mock:ws");

    }
}
