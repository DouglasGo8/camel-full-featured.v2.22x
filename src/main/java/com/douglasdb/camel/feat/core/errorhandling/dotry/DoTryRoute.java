package com.douglasdb.camel.feat.core.errorhandling.dotry;

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
                .   to("mock:error")
                    .transform(constant("Something Bad Happened!"))
                .doFinally()
                .   to("mock:finally")
                .end()
                .to("mock:after");

        from("direct:unhandled")
                .to("mock:before")
                .doTry()
                    .bean(FlakyProcessor.class)
                    .transform(constant("Made it!"))
                .doCatch(FlakyException.class)
                    .handled(false)
                    .to("mock:error")
                    .transform(constant("Something Bad Happened!"))
                .doFinally()
                    .to("mock:finally")
                .end()
                .to("mock:after");

        from("direct:conditional")
                .to("mock:before")
                .doTry()
                    .bean(FlakyProcessor.class)
                    .transform(constant("Made it!"))
                .doCatch(FlakyException.class)
                    .onWhen(header("jedi").isNull())
                    .to("mock:error")
                    .transform(constant("Something Bad Happened!"))
                .doFinally()
                .to("mock:finally")
                .end()
                .to("mock:after");

    }
}
