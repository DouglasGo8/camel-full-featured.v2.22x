package com.douglasdb.camel.feat.core.errorhandling.multicast;

import java.util.concurrent.Executors;


/**
 * @author dbatista
 */
public class MultiCastOriginalMessageRoute extends MulticastBaseExceptionRoute {

    @Override
    public void configure() throws Exception {


        //.to("direct:fail");

        from("direct:start")
                .log("${body}")
                .multicast()
                    .parallelProcessing(true).stopOnException()
                        .executorService(Executors.newFixedThreadPool(5))
                        .bean(BeanOne.class)
                        //.bean(BeanTwo.class)
                .end()
        .end();

        from("direct:fail")
                .log("Printing fail")
                .log("${body}")
                .to("mock:result");


    }
}
