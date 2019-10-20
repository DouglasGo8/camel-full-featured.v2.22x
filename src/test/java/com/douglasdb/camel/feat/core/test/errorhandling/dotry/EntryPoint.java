package com.douglasdb.camel.feat.core.test.errorhandling.dotry;

import com.douglasdb.camel.feat.core.errorhandling.dotry.DoTryRoute;
import com.douglasdb.camel.feat.core.errorhandling.shared.FlakyException;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.cdi.Mock;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new DoTryRoute();
    }

    @Test
    @Ignore
    public void testDoTryHappy() throws InterruptedException {

        final MockEndpoint mockBefore = super.getMockEndpoint("mock:before");
        mockBefore.expectedBodiesReceived("Foo");

        final MockEndpoint mockError = super.getMockEndpoint("mock:error");
        mockError.expectedMessageCount(0);

        final MockEndpoint mockFinally = super.getMockEndpoint("mock:finally");
        mockFinally.expectedBodiesReceived("Made it!");

        final MockEndpoint mockAfter = getMockEndpoint("mock:after");
        mockAfter.expectedBodiesReceived("Made it!");

        String response = null;
        try {
            response = super.template.requestBody("direct:start", "Foo", String.class);
        } catch (Throwable e) {
            fail("Shouldn't get here");
        }

        assertEquals("Made it!", response);
        assertMockEndpointsSatisfied();


    }

    @Test
    public void testDoTryError() throws InterruptedException {

        final MockEndpoint mockBefore = super.getMockEndpoint("mock:before");
        mockBefore.expectedBodiesReceived("Kaboom");

        final MockEndpoint mockError = getMockEndpoint("mock:error");
        mockError.expectedBodiesReceived("Kaboom");

        final MockEndpoint mockFinally = getMockEndpoint("mock:finally");
        mockFinally.expectedBodiesReceived("Something Bad Happened!");

        final MockEndpoint mockAfter = getMockEndpoint("mock:after");
        mockAfter.expectedBodiesReceived("Something Bad Happened!");

        String response = null;
        try {
            response = super.template.requestBody("direct:start", "Kaboom", String.class);
        } catch (Throwable e) {
            fail("Shouldn't get here");
        }

        assertEquals("Something Bad Happened!", response);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testDoTryUnHandled() throws InterruptedException {

        final MockEndpoint mockError = super.getMockEndpoint("mock:error");
        final MockEndpoint mockAfter = super.getMockEndpoint("mock:after");
        final MockEndpoint mockBefore = super.getMockEndpoint("mock:before");
        final MockEndpoint mockFinally = super.getMockEndpoint("mock:finally");

        mockError.expectedBodiesReceived("Kaboom");
        mockAfter.expectedMessageCount(0);
        mockBefore.expectedBodiesReceived("Kaboom");
        mockFinally.expectedBodiesReceived("Something Bad Happened!");


        boolean threwException = false;

        try {
            super.template.requestBody("direct:unhandled", "Kaboom", String.class);
        } catch (Throwable e) {
            threwException = true;
            CamelExecutionException cee = assertIsInstanceOf(CamelExecutionException.class, e);
            Throwable cause = cee.getCause();
            assertIsInstanceOf(FlakyException.class, cause);
        }
        assertTrue(threwException);


        assertMockEndpointsSatisfied();


    }

    @Test
    public void testDoTryConditional() throws InterruptedException {

        final MockEndpoint mockBefore = super.getMockEndpoint("mock:before");
        final MockEndpoint mockError = super.getMockEndpoint("mock:error");
        final MockEndpoint mockFinally = super.getMockEndpoint("mock:finally");
        final MockEndpoint mockAfter = super.getMockEndpoint("mock:after");

        mockBefore.expectedBodiesReceived("Kaboom", "Kaboom");
        mockError.expectedBodiesReceived("Kaboom");
        mockFinally.expectedBodiesReceived("Something Bad Happened!", "Kaboom");
        mockAfter.expectedBodiesReceived("Something Bad Happened!");

        String response = null;
        try {
            response = template.requestBody("direct:conditional", "Kaboom", String.class);
        } catch (Throwable e) {
            fail("Shouldn't get here");
        }

        assertEquals("Something Bad Happened!", response);

        response = null;
        boolean threwException = false;

        try {
            response = super.template.requestBodyAndHeader("direct:conditional", "Kaboom",
                    "jedi", "This isn't the Exception you are looking for...", String.class);

        } catch (Throwable e) {
            threwException = true;
            CamelExecutionException cee = assertIsInstanceOf(CamelExecutionException.class, e);
            Throwable cause = cee.getCause();
            assertIsInstanceOf(FlakyException.class, cause);
        }
        assertTrue(threwException);


        assertMockEndpointsSatisfied();
    }



}
