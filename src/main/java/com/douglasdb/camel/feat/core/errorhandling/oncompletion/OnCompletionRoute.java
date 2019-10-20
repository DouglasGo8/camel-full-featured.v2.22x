package com.douglasdb.camel.feat.core.errorhandling.oncompletion;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class OnCompletionRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onCompletion()
                .log("global onCompletion thread: ${threadName}")
                .to("mock:global");

        from("direct:onCompletion")
                .onCompletion()
                    .log("onCompletion triggered: ${threadName}")
                    .to("mock:completed")
                .end()
                .log("Processing message: ${threadName}");

        from("direct:noOnCompletion")
                .log("Original thread: ${threadName}")
                .choice()
                    .when(simple("${body} contains 'explode'"))
                        .throwException(new IllegalArgumentException("Exchange caused explosion"))
                .endChoice();

        from("direct:onCompletionFailure")
                .onCompletion()
                    .onFailureOnly()
                    .log("onFailureOnly thread: ${threadName}")
                    .to("mock:failed")
                .end()
                .log("Original thread: ${threadName}")
                .choice()
                    .when(simple("${body} contains 'explode'"))
                        .throwException(new IllegalArgumentException("Exchange caused explosion"))
                .endChoice();

        from("direct:onCompletionFailureConditional")
                .onCompletion()
                    .onFailureOnly()
                        .onWhen(simple("${exception.class} == 'java.lang.IllegalArgumentException'"))
                        .log("onFailureOnly thread: ${threadName}: ${exception.class}")
                        .to("mock:failed")
                .end()
                .choice()
                    .when(simple("${body} contains 'explode'"))
                        .throwException(new IllegalArgumentException("Exchange caused explosion"))
                .endChoice();

        from("direct:onChained")
                .log("onChained fired")
                .onCompletion()
                    .onCompleteOnly()
                    .log("onCompleteOnly thread: ${threadName}")
                    .to("mock:completed")
                .end()
                .to("direct:onCompletionFailure");

        from("direct:onCompletionChoice")
                .onCompletion()
                    .to("direct:processCompletion")
                .end()
                .log("Original thread: ${threadName}")
                .choice()
                    .when(simple("${body} contains 'explode'"))
                        .throwException(new IllegalArgumentException("Exchange caused explosion"))
                .endChoice();

        from("direct:processCompletion")
                .log("onCompletion thread: ${threadName}")
                .log("${body}")
                .choice()
                    .when(simple("${body} contains 'complete'"))
                        .to("mock:completed")
                    .otherwise()
                        .to("mock:failed")
                .endChoice();
    }
}
