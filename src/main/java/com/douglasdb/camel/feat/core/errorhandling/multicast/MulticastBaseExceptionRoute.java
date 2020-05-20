package com.douglasdb.camel.feat.core.errorhandling.multicast;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 * @apiNote don't use if stack trace exception is necessary
 */
public class MulticastBaseExceptionRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        onException(NullPointerException.class)
                //.useOriginalMessage()
                //.handled(true)
                .logStackTrace(true)
                .log("*** Fail action ${body} ***")
                .logExhaustedMessageHistory(true)
                .logExhaustedMessageBody(true)
                //.maximumRedeliveries(2)
                //.redeliveryDelay(2000)
                //.retryAttemptedLogLevel(LoggingLevel.ERROR)
                .end();

        onException(MulticastBusinessException.class)
                //.handled(true)
                // .continued(true) result mock will received the message
                //.useOriginalMessage()
                .logStackTrace(true)
                .log("*** ${body} ***")
                .logExhaustedMessageHistory(true)
                .logExhaustedMessageBody(true)
                .maximumRedeliveries(2)
                .redeliveryDelay(2000)
                .retryAttemptedLogLevel(LoggingLevel.ERROR)
                .end();

    }
}
