package com.douglasdb.camel.feat.core.errorhandling.doTry;

import com.douglasdb.camel.feat.core.errorhandling.shared.FlakyException;
import com.douglasdb.camel.feat.core.errorhandling.shared.FlakyProcessor;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class DoTryRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:start")
                .to("mock:before")
                .doTry()
                    .bean(FlakyProcessor.class)
                    .transform(constant("Made it!"))
                .doCatch(FlakyException.class)
                    .to("mock:error")
                    .transform(constant("Something Bad Happened!"))
                .doFinally()
                    .to("mock:finally")
                .end()
                .to("mock:after");


    }
}
