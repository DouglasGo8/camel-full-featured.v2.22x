package com.douglasdb.camel.feat.core.test.errorhandling.onexception;

import com.douglasdb.camel.feat.core.errorhandling.onexception.OnExceptionGapRoute;
import com.douglasdb.camel.feat.core.errorhandling.onexception.OrderFailedException;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.net.ConnectException;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        context.setTracing(true);
        context.setStreamCaching(true);
        return new OnExceptionGapRoute();
        // OnExceptionWrappedMatchRoute();
        // OnExceptionDirectMatchRoute();
    }


    @Test
    @Ignore
    public void testOnExceptionDirectMatch() throws InterruptedException {

        super.getMockEndpoint("mock:done").setExpectedMessageCount(0);
        try {

            super.template.requestBody("direct:order", "ActiveMQ in Action");
            fail("Should throw an exception");

        } catch (CamelExecutionException e) {
            assertIsInstanceOf(OrderFailedException.class, e.getCause());
        }

        assertMockEndpointsSatisfied();
    }


    @Test
    @Ignore
    public void testOnExceptionWrappedMatch() throws InterruptedException {

        try {
            super.getMockEndpoint("mock:done").setExpectedMessageCount(0);
            super.template.requestBody("direct:order", "Camel in Action");
            fail("Should throw an exception");
        } catch (CamelExecutionException e) {
            assertIsInstanceOf(OrderFailedException.class, e.getCause());
            assertIsInstanceOf(ConnectException.class, e.getCause().getCause());
        }

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOnExceptionGab() throws Exception {

        try {

            super.getMockEndpoint("mock:fail").setExpectedMessageCount(0);

            template.requestBody("direct:order", "Camel in Action");
            fail("Should throw an exception");

        } catch (CamelExecutionException e) {
            assertIsInstanceOf(OrderFailedException.class, e.getCause());
            assertIsInstanceOf(FileNotFoundException.class, e.getCause().getCause());
        }

        assertMockEndpointsSatisfied();
    }


}
