package com.douglasdb.camel.feat.core.test.errorhandling.exception;

import com.douglasdb.camel.feat.core.errorhandling.exception.ExceptionRoute;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new ExceptionRoute();
    }

    @Test
    public void testException() throws InterruptedException {

        final MockEndpoint mockError = super.getMockEndpoint("mock:error");
        final MockEndpoint mockResult = super.getMockEndpoint("mock:result");
        final MockEndpoint mockGenericError = super.getMockEndpoint("mock:genericError");

        mockResult.expectedMessageCount(1);
        mockError.expectedMessageCount(1);
        mockGenericError.expectedMessageCount(0);

        try {
            super.template.sendBody("direct:start", "Foo");

        } catch (Throwable e) {
            fail("Shouldn't get here");
        }

        mockResult.assertIsSatisfied();

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
    public void testExceptionHandled() throws InterruptedException {

        final MockEndpoint mockResult = super.getMockEndpoint("mock:result");
        final MockEndpoint mockError = getMockEndpoint("mock:error");

        mockResult.expectedBodiesReceived("All Good!");
        mockError.expectedBodiesReceived("Something Bad Happened!");
        mockError.expectedMessageCount(1);

        String response;

        try {
            response = super.template.requestBody("direct:handled", "Foo", String.class);
            assertEquals("All Good!", response);
        } catch (Throwable e) {
            fail("Shouldn't get here");
        }

        mockResult.assertIsSatisfied();

        try {
            response = super.template.requestBody("direct:handled", "KaBoom", String.class);
            assertEquals("Something Bad Happened!", response);
        } catch (Throwable e) {
            fail("Shouldn't get here");
        }


        assertMockEndpointsSatisfied();
    }

    @Test
    public void testExceptionContinue() throws InterruptedException {

        final MockEndpoint mockResult = super.getMockEndpoint("mock:result");
        final MockEndpoint mockError = getMockEndpoint("mock:ignore");
        final MockEndpoint mockGenericError = getMockEndpoint("mock:3");

        mockResult.setExpectedMessageCount(2);
        mockResult.expectedBodiesReceived("All Good!", "All Good!");
        mockError.expectedMessageCount(1);
        mockGenericError.expectedMessageCount(0);


        String response;

        try {
            response = template.requestBody("direct:continue", "Foo", String.class);
            assertEquals("All Good!", response);
        } catch (Throwable e) {
            fail("Shouldn't get here");
        }

        try {
            response = template.requestBody("direct:continue", "KaBoom", String.class);
            assertEquals("All Good!", response);
        } catch (Throwable e) {
            fail("Shouldn't get here either");
        }
        assertMockEndpointsSatisfied();

    }
}
