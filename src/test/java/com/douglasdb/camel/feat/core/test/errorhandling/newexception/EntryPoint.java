package com.douglasdb.camel.feat.core.test.errorhandling.newexception;

import com.douglasdb.camel.feat.core.errorhandling.newexception.NewExceptionRoute;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new NewExceptionRoute();
    }


    @Test
    public void testNoError() throws Exception {

        super.getMockEndpoint("mock:done").expectedMessageCount(1);

        super.template.sendBodyAndHeader("direct:start", "Hello Camel",
                "name", "Camel");

        assertMockEndpointsSatisfied();
    }


    @Test
    public void testNewException() throws InterruptedException {

        super.getMockEndpoint("mock:done").setExpectedMessageCount(0);

        try {

            super.template.sendBodyAndHeader("direct:start", "Hello Bomb",
                    "name", "Kaboom");

            fail("Should have thrown exception");
        } catch(CamelExecutionException e) {
            // we expect a NullPointerException because that is what NotAllowedProcessor throws
            // while handling the first AuthorizationException which is thrown from the filter in the route
            assertIsInstanceOf(NullPointerException.class, e.getCause());
        }

        assertMockEndpointsSatisfied();
    }



}
