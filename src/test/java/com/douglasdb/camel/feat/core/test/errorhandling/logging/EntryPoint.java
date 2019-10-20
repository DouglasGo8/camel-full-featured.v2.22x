package com.douglasdb.camel.feat.core.test.errorhandling.logging;

import com.douglasdb.camel.feat.core.errorhandling.logging.LoggingRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new LoggingRoute();
    }



    @Test
    public void testLogging() throws InterruptedException {

        super.getMockEndpoint("mock:result").expectedMessageCount(1);

        try {
            super.template.sendBody("direct:start", "Foo");
        } catch (Throwable e) {
            fail("Shouldn't get here");
        }

        boolean threwException = false;

        try {
            super.template.sendBody("direct:start", "KaBoom");
        } catch (Throwable e) {
            threwException = true;
        }
        assertTrue(threwException);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testLoggingRouteSpecific() throws Exception {
        super.getMockEndpoint("mock:result").expectedMessageCount(1);

        try {
            template.sendBody("direct:routeSpecific", "Foo");
        } catch (Throwable e) {
            fail("Shouldn't get here");
        }

        boolean threwException = false;
        try {
            template.sendBody("direct:routeSpecific", "KaBoom");
        } catch (Throwable e) {
            threwException = true;
        }
        assertTrue(threwException);

        assertMockEndpointsSatisfied();
    }
}
