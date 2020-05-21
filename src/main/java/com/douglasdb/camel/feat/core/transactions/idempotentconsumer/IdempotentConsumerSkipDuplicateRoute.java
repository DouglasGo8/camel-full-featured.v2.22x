
package com.douglasdb.camel.feat.core.transactions.idempotentconsumer;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;

public class IdempotentConsumerSkipDuplicateRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:in")
                .log("Received message ${header[messageId]}")
                .idempotentConsumer(header("messageId"), new MemoryIdempotentRepository())
                    .skipDuplicate(false)
                    .choice()
                        .when(exchangeProperty(Exchange.DUPLICATE_MESSAGE))
                            .log("Duplicate")
                            .to("mock:duplicate")
                        .otherwise()
                            .to("mock:ws")
                    .end()
                .log("Completing")
                .to("mock:out")
                .end();
    }

}