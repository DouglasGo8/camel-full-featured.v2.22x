package com.douglasdb.camel.feat.core.test.tracer;

import com.douglasdb.camel.feat.core.tracer.TraceRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class TraceTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new TraceRoute();
    }

    @Test
    public void testTrace() throws InterruptedException {
        getMockEndpoint("mock:result").expectedBodiesReceived("Tracing Hello Camel");

        template.sendBody("direct:start", "Hello Camel");

        assertMockEndpointsSatisfied();
    }
}
