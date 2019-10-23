package com.douglasdb.camel.feat.core.errorhandling.enrich;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
  */
public class EnrichFailureRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        errorHandler(deadLetterChannel("direct:dead").useOriginalMessage());

        from("direct:start")
                .transform(constant("This is a changed body"))
                .throwException(new IllegalArgumentException("Forced Error"));

        from("direct:dead")
                .bean(FailureBean.class)
                .to("mock:dead");


    }

}
