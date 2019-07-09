
package com.douglasdb.camel.feat.core.test.splitter;

import java.util.Arrays;

import com.douglasdb.camel.feat.core.splitter.Customer;
import com.douglasdb.camel.feat.core.splitter.CustomerService;
import com.douglasdb.camel.feat.core.splitter.SplitStopOnExceptionRouter;

import org.apache.camel.CamelExchangeException;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.RoutesBuilder;
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
        return new SplitStopOnExceptionRouter();
        // SplitterBeanRouter();
        // new SplitAggregateExceptionABCRouter();
        // SplitterAggregateABCRouter();
        // SplitterABCRouter();
    }

    @Test
    @Ignore
    public void testSplitABC() throws Exception {

        MockEndpoint mock = super.getMockEndpoint("mock:split");

        mock.expectedBodiesReceived("A", "B", "C");

        super.template.sendBody("direct:start", Arrays.asList("A", "B", "C"));

        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testSplitAggregateABC() throws Exception {

        MockEndpoint split = super.getMockEndpoint("mock:split");
        split.expectedBodiesReceived("Camel rocks", "Hi mom", "Yes it works");

        MockEndpoint result = super.getMockEndpoint("mock:result");
        result.expectedBodiesReceived("Camel rocksHi momYes it works");

        super.template.sendBody("direct:start", "A,B,C");

        assertMockEndpointsSatisfied();

    }

    @Test
    public void testSplitAggregateExceptionABC() throws Exception {

        MockEndpoint split = getMockEndpoint("mock:split");
        // we expect 2 messages successfully to be split and translated into a quote
        split.expectedBodiesReceived("Camel rocks", "Yes it works");

        MockEndpoint result = getMockEndpoint("mock:result");
        // and one combined aggregated message as output with two two quotes together
        result.expectedBodiesReceived("Camel rocks+Yes it works");

        super.template.sendBody("direct:start", "A,F,C");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testSplitBean() throws Exception {

        MockEndpoint mock = super.getMockEndpoint("mock:split");

        mock.expectedMessageCount(3);

        Customer customer = CustomerService.createCustomer();

        super.template.sendBody("direct:start", customer);

        assertMockEndpointsSatisfied();

    }

    @Test
    public void testSplitStopOnException() throws Exception {

        MockEndpoint split = super.getMockEndpoint("mock:split");

        split.expectedBodiesReceived("Camel rocks");

        MockEndpoint result = super.getMockEndpoint("mock:result");
        result.expectedMessageCount(0);


        try {

            super.template.sendBody("direct:start", "A,F,C");
            fail("Should have thrown an exception");


        } catch (CamelExecutionException e) {
            CamelExchangeException cause = assertIsInstanceOf(CamelExchangeException.class, e.getCause());
            IllegalArgumentException iae = assertIsInstanceOf(IllegalArgumentException.class, cause.getCause());
            assertEquals("Key not a known word F", iae.getMessage());

        }


        assertMockEndpointsSatisfied();
    }

}