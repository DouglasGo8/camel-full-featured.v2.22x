package com.douglasdb.camel.feat.core.errorhandling.multicast;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;


/**
 * @author dbatista
 */
public class MultiCastOriginalMessageRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        onException(MulticastBusinessException.class)
                .handled(true)
                //.useOriginalMessage()
                .log("*** ${body} ***")
                .logExhaustedMessageHistory(true)
                .logExhaustedMessageBody(true)
                .maximumRedeliveries(2)
                .redeliveryDelay(2000)
                .retryAttemptedLogLevel(LoggingLevel.ERROR);


        //.to("direct:fail");


        from("direct:start")
                .log("${body}")
                .multicast()
                    .parallelProcessing(true)
                        .bean(BeanOne.class)
                        .bean(BeanTwo.class)
                .end();



        from("direct:fail")
                .log("Printing fail")
                .log("${body}")
                .to("mock:result");


    }
}
