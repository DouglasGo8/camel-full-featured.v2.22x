package com.douglasdb.camel.feat.core.test.errorhandling.enrich;

import com.douglasdb.camel.feat.core.errorhandling.enrich.EnrichFailureProcessorRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new EnrichFailureProcessorRoute();
        // EnrichFailureRoute();
    }


    @Test
    public void testEnrichFailure() throws InterruptedException {


        // we expect the message to end up in the dead letter queue
        // is the original incoming message, and not the transformed message
        // that happens during routing
        super.getMockEndpoint("mock:dead").expectedBodiesReceived("Hello World!");

        super.getMockEndpoint("mock:dead").expectedHeaderReceived("FailureMessage",
                "The message failed because Forced Error");

        super.sendBody("direct:start", "Hello World!");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testEnrichFailureWithProcessor() throws Exception {
        // we expect the message to end up in the dead letter queue
        // is the original incoming message, and not
        // the transformed message that happens during routing
        getMockEndpoint("mock:dead").expectedBodiesReceived("Hello World");

        // the failure processor will enrich the message and set a header with a constructed error message
        getMockEndpoint("mock:dead").expectedHeaderReceived("FailureMessage",
                "The message failed because Forced Error");

        template.sendBody("direct:start", "Hello World");

        assertMockEndpointsSatisfied();
    }

}
