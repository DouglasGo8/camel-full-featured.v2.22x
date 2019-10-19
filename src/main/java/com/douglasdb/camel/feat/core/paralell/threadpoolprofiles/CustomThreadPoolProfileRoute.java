package com.douglasdb.camel.feat.core.paralell.threadpoolprofiles;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolProfileBuilder;

/**
 * @author dbatista
 */
public class CustomThreadPoolProfileRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        super.getContext()
                .getExecutorServiceManager()
                .registerThreadPoolProfile(new ThreadPoolProfileBuilder("customThreadPoolProfile")
                        .poolSize(5).maxQueueSize(100).build());

        from("direct:in")
                .log("Received ${body}:${threadName}")
                .threads()
                    .executorServiceRef("customThreadPoolProfile")
                .log("Processing ${body}:${threadName}")
                .transform(simple("${threadName}"))
                .to("mock:out");




    }
}
