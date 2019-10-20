package com.douglasdb.camel.feat.core.errorhandling.exception;

import com.douglasdb.camel.feat.core.errorhandling.shared.FlakyException;
import com.douglasdb.camel.feat.core.errorhandling.shared.FlakyProcessor;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class ExceptionRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onException(FlakyException.class)
                .to("mock:error");

        from("direct:start")
                .bean(FlakyProcessor.class)
                .to("mock:result");

        from("direct:handled")
                .onException(FlakyException.class)
                    .handled(true)
                    .transform(constant("Something Bad Happened!"))
                    .to("mock:error")
                .end()
                .bean(FlakyProcessor.class)
                .transform(constant("All Good!"))
                .to("mock:result");

        from("direct:continue")
                .onException(FlakyException.class)
                    .continued(true)
                    .to("mock:ignore")
                .end()
                .bean(FlakyProcessor.class)
                .transform(constant("All Good!"))
                .to("mock:result");

    }
}
