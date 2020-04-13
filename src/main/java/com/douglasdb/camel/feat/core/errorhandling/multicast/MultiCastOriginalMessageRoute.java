package com.douglasdb.camel.feat.core.errorhandling.multicast;

import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import java.util.concurrent.Executors;


/**
 * @author dbatista
 */
public class MultiCastOriginalMessageRoute extends RouteBuilder
        // Don`t show body history
        // on Stack trace, not recommend
        // to situations on we need log
        // all possible details
        /*MulticastBaseExceptionRoute*/ {

    @Override
    public void configure() throws Exception {


        onException(NullPointerException.class)
                //.handled(true) // if true hidden stack Camel trace
                //.useOriginalMessage() // if handle json in Memory not don't have effect
                //.logStackTrace(true) default behavior
                .log("*** Fail action ${body} ***")
                //.log("### ${header.originalPayload} ###") doesn't work
                .logExhaustedMessageHistory(true)
                .logExhaustedMessageBody(true)
                //.maximumRedeliveries(2)
                //.redeliveryDelay(2000)
                //.retryAttemptedLogLevel(LoggingLevel.ERROR)
                .end();

        onException(MulticastBusinessException.class)
                //.handled(true) // if true hidden stack Camel trace
                //.continued(true) result mock will received the message
                //.useOriginalMessage()  // if handle json in Memory not don't have effect
                .logStackTrace(true)
                .log("*** ${body} ***")
                .logExhaustedMessageHistory(true)
                .logExhaustedMessageBody(true)
                .maximumRedeliveries(2)
                .redeliveryDelay(2000)
                .retryAttemptedLogLevel(LoggingLevel.ERROR)
                .end();

        //.to("direct:fail");

        from("direct:start")
                .log("${body}")
                //.marshal()
                //    .json(JsonLibrary.Jackson)
               // .setHeader("originalPayload", simple("${body}"))
                .multicast()
                    .parallelProcessing(true).stopOnException()
                        .executorService(Executors.newFixedThreadPool(5))
                   .to("direct:BY4")
                    //.bean(BeanBrand.class)
                    //.bean(BeanDoor.class)
                .end()
                .transform(simple("${exchangeProperty.payload}"))
                .log("${body}")
                .to("mock:result")
                .end();

        from("direct:BY4")
                .setProperty("payload", simple("${body}"))
                .setBody(constant("Hi"))
                .log("${body}")
                .bean(BeanColor.class, "handleColor(${exchangeProperty.payload}, 'gray')")
                .end();

        from("direct:fail")
                .log("Printing fail")
                .log("${body}")
                .to("mock:result");





        //from("cache://enrichCache?timeToLiveSeconds=10&diskPersistent=true&diskExpiryThreadIntervalSeconds=10");


    }
}
