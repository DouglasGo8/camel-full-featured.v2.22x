
package com.douglasdb.camel.feat.core.test.splitter;

import com.douglasdb.camel.feat.core.domain.splitter.ListWrapper;
import com.douglasdb.camel.feat.core.splitter.Customer;
import com.douglasdb.camel.feat.core.splitter.CustomerService;
import com.douglasdb.camel.feat.core.splitter.SplitNaturalRoute;
import com.douglasdb.camel.feat.core.splitter.SplitSimpleExpressionRoute;
import org.apache.camel.CamelExchangeException;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new SplitNaturalRoute();
        // SplitSimpleExpressionRoute();
        // SplitStopOnExceptionRoute();
        // SplitterBeanRoute();
        // SplitAggregateExceptionABCRoute();
        // SplitterAggregateABCRoute();
        // SplitterABCRoute();
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
    @Ignore
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

    @Test
    @Ignore
    public void testSplitSimpleExpression() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:out");
        mock.setExpectedMessageCount(3);
        mock.expectedBodiesReceived("one", "two", "three");

        ListWrapper wrapper = new ListWrapper();
        wrapper.setWrapped(Arrays.asList("one", "two", "three"));
        super.template.sendBody("direct:in", wrapper);

        assertMockEndpointsSatisfied();

    }

    @Test
    public void testSplitNaturalArray() throws InterruptedException {
        String[] array = new String[]{"one", "two", "three"};
        MockEndpoint mockSplit = super.getMockEndpoint("mock:split");

        mockSplit.expectedMessageCount(3);
        mockSplit.expectedBodiesReceived("one", "two", "three");

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(1);
        mockOut.message(0).body().isEqualTo(array);

        super.template.sendBody("direct:in", array);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testSplitNaturalList() throws InterruptedException {
        List<String> list = Arrays.asList("one", "two", "three");
        MockEndpoint mockSplit = super.getMockEndpoint("mock:split");

        mockSplit.expectedMessageCount(3);
        mockSplit.expectedBodiesReceived("one", "two", "three");

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(1);
        mockOut.message(0).body().isEqualTo(list);

        super.template.sendBody("direct:in", list);

        assertMockEndpointsSatisfied();
    }


    @Test
    public void testSplitNaturalIterable() throws InterruptedException {
        Set<String> set = new TreeSet<>();
        set.add("one");
        set.add("two");
        set.add("three");
        Iterator<String> iterator = set.iterator();
        MockEndpoint mockSplit = super.getMockEndpoint("mock:split");

        mockSplit.expectedMessageCount(3);
        mockSplit.expectedBodiesReceivedInAnyOrder("one", "two", "three");

        MockEndpoint mockOut = super.getMockEndpoint("mock:out");
        mockOut.expectedMessageCount(1);
        mockOut.message(0).body().isEqualTo(iterator);

        super.template.sendBody("direct:in", iterator);

        assertMockEndpointsSatisfied();
    }

    // testSplitMultiLine
}