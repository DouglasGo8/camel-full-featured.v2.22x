package com.douglasdb.camel.feat.core.errorhandling.retryconditional;

import com.douglasdb.camel.feat.core.errorhandling.shared.SporadicProcessor;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author dbatista
 */
public class RetryConditionalRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        errorHandler(defaultErrorHandler().retryWhile(simple("${header.CamelRedeliveryCounter} < 2 or ${date:now:EEE} contains 'Tue'")));

        from("direct:start")
                .bean(SporadicProcessor.class)
                .to("mock:result");
    }
}
