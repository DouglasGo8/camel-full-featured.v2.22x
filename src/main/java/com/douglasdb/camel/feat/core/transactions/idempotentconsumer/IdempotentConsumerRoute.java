<<<<<<< HEAD
package com.douglasdb.camel.feat.core.transactions.idempotentconsumer;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;

public class IdempotentConsumerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        from("direct:in")
                .log("Received message ${header[messageId]}")
                .idempotentConsumer(header("messageId"), new MemoryIdempotentRepository())
                    .log("Invoking WS")
                    .to("mock:ws")
                .end()
                .log("Completing")
                .to("mock:out");

    }

}
=======
package com.douglasdb.camel.feat.core.transactions.idempotentconsumer;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;

public class IdempotentConsumerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        from("direct:in")
                .log("Received message ${header[messageId]}")
                .idempotentConsumer(header("messageId"), new MemoryIdempotentRepository())
                    .log("Invoking WS")
                    .to("mock:ws")
                .end()
                .log("Completing")
                .to("mock:out");

    }

}
>>>>>>> 99853127246a0c2317b71478827adeee088d29ac
