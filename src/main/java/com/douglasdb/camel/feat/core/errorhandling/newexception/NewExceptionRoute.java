package com.douglasdb.camel.feat.core.errorhandling.newexception;

import org.apache.camel.builder.RouteBuilder;


/**
 * @author dbatista
 */
public class NewExceptionRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        onException(AuthorizationException.class)
                .handled(true)
                .log("Hi from AuthorizationException")
                .process(new NotAllowedProcessor());

        onException(Exception.class)
                .handled(true)
                .process(new GeneralErrorProcessor());

        from("direct:start")
                .log("User ${header.name} is calling us")
                .filter(simple("${header.name} == 'Kaboom'"))
                    .throwException(new AuthorizationException("Forbidden"))
                .end()
                .to("mock:done");
    }
}
