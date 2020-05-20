package com.douglasdb.camel.feat.core.paralell.threadpools;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolBuilder;

import java.util.concurrent.ExecutorService;

/**
 *
 */
public class CustomThreadPoolInlineRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        final ExecutorService executorService = new ThreadPoolBuilder(super.getContext())
                .poolSize(5)
                .maxPoolSize(100)
                .build("CustomThreadPool");

        from("direct:in")
                .log("Received ${body}:${threadName}")
                .threads()
                    .executorService(executorService)
                    .log("Processing ${body}:${threadName}")
                .transform(simple("${threadName}"))
                .to("mock:out");
    }
}
