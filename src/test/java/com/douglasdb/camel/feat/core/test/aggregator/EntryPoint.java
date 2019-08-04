package com.douglasdb.camel.feat.core.test.aggregator;


import com.douglasdb.camel.feat.core.aggregator.AggregateDynamicCompletionSizeRoute;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.processor.aggregate.ClosedCorrelationKeyException;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author DouglasDb
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() {
        return new AggregateDynamicCompletionSizeRoute();
        // AggregateCompletionConditionRoute();
        // AggregatorSimpleRoute();
        // AggregatorTimeoutThreadPoolRoute();
        // AggregatorABCPojoRoute();
        // AggregatorABCLevelDBRoute();
        // AggregateABCRecoveryRoute();
        // AggregateInvalidRoute();
        // AggregateGroupRoute();
        // AggregatorEagerRoute();
        // AggregatorXMLRoute();
        // AggregatorABCRoute();
    }


    @Override
    public void setUp() throws Exception {
        deleteDirectory("data");
        super.setUp();
    }

    @Test
    @Ignore
    public void testABC() throws Exception {

        MockEndpoint mock = super.getMockEndpoint("mock:result");

        mock.expectedMessageCount(1);
        mock.expectedBodiesReceived("ABC");

        super.template.sendBodyAndHeader("direct:start", "A", "correlationId", 1);
        super.template.sendBodyAndHeader("direct:start", "B", "correlationId", 1);

        super.template.sendBodyAndHeader("direct:start", "F", "correlationId", 2);

        super.template.sendBodyAndHeader("direct:start", "C", "correlationId", 1);

        assertMockEndpointsSatisfied();

    }

    @Test
    @Ignore
    public void testXML() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:result");

        mock.expectedMessageCount(2);

        super.template.sendBody("direct:start", "<order name=\"motor\" amount=\"1000\" customer=\"honda\"/>");
        super.template.sendBody("direct:start", "<order name=\"motor\" amount=\"500\" customer=\"toyota\"/>");
        super.template.sendBody("direct:start", "<order name=\"gearbox\" amount=\"200\" customer=\"toyota\"/>");

        //
        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testABCEND() throws InterruptedException {

        MockEndpoint result = super.getMockEndpoint("mock:result");

        result.expectedBodiesReceived("ABC");

        super.template.sendBodyAndHeader("direct:start", "A", "myId", 1);
        super.template.sendBodyAndHeader("direct:start", "B", "myId", 1);

        // diff Correlation
        super.template.sendBodyAndHeader("direct:start", "F", "myId", 2);

        super.template.sendBodyAndHeader("direct:start", "C", "myId", 1);

        template.sendBodyAndHeader("direct:start", "END", "myId", 1);

        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testABCClose() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:result");

        mock.expectedBodiesReceived("ABC");

        super.template.sendBodyAndHeader("direct:start", "A", "myId", 1);
        super.template.sendBodyAndHeader("direct:start", "B", "myId", 1);
        super.template.sendBodyAndHeader("direct:start", "F", "myId", 2);
        super.template.sendBodyAndHeader("direct:start", "C", "myId", 1);

        try {

            template.sendBodyAndHeader("direct:start", "A2", "myId", 1);

        } catch (CamelExecutionException e) {
            ClosedCorrelationKeyException cause =
                    assertIsInstanceOf(ClosedCorrelationKeyException.class, e.getCause());
            assertEquals("1", cause.getCorrelationKey());
        }

        assertMockEndpointsSatisfied();
    }


    @Test
    @Ignore
    public void testABCGroup() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:result");

        mock.setExpectedMessageCount(1);

        super.template.sendBodyAndHeader("direct:start", "A", "myId", 1);
        super.template.sendBodyAndHeader("direct:start", "B", "myId", 1);
        super.template.sendBodyAndHeader("direct:start", "F", "myId", 2);
        super.template.sendBodyAndHeader("direct:start", "C", "myId", 1);


        assertMockEndpointsSatisfied();

        final Exchange exchange = mock.getExchanges().get(0);


        final List<?> exchanges = exchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);


        assertEquals("Should contain the 3 arrived exchanges", 3, exchanges.size());

        assertEquals("A", ((Exchange) exchanges.get(0)).getIn().getBody());
        assertEquals("B", ((Exchange) exchanges.get(1)).getIn().getBody());
        assertEquals("C", ((Exchange) exchanges.get(2)).getIn().getBody());


    }

    @Test
    @Ignore
    public void testABCInvalid() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:result");

        mock.expectedBodiesReceived("ABC");

        super.template.sendBodyAndHeader("direct:start", "A", "myId", 1);
        super.template.sendBodyAndHeader("direct:start", "B", "myId", 1);
        super.template.sendBodyAndHeader("direct:start", "F", "myId", 2);

        super.template.sendBody("direct:start", "C");

        super.template.sendBodyAndHeader("direct:start", "C", "myId", 1);

        assertMockEndpointsSatisfied();

    }


    @Test
    @Ignore
    public void testABCRecover() throws InterruptedException {


        super.getMockEndpoint("mock:result").expectedMessageCount(0);

        MockEndpoint mock = super.getMockEndpoint("mock:aggregate");
        MockEndpoint dead = super.getMockEndpoint("mock:dead");

        mock.setExpectedMessageCount(5); // ABC Aggregate

        dead.setExpectedMessageCount(1);
        dead.expectedBodiesReceived("ABC");

        dead.message(0).header(Exchange.REDELIVERED).isEqualTo(true);
        dead.message(0).header(Exchange.REDELIVERY_COUNTER).isEqualTo(4);


        super.template.sendBodyAndHeader("direct:start", "A", "myId", 1);
        super.template.sendBodyAndHeader("direct:start", "B", "myId", 1);
        super.template.sendBodyAndHeader("direct:start", "C", "myId", 1);

        assertMockEndpointsSatisfied(20, TimeUnit.SECONDS);
    }

    @Test
    @Ignore
    public void testABCLevelDB() throws InterruptedException {

        //MockEndpoint mock = super.getMockEndpoint("mock:result");

        //mock.setExpectedMessageCount(1);

        //assertMockEndpointsSatisfied(20, TimeUnit.SECONDS);

        TimeUnit.SECONDS.sleep(20);

    }

    @Test
    @Ignore
    public void testABCWithPojo() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:result");

        mock.expectedBodiesReceived("ABC");

        super.template.sendBodyAndHeader("direct:start", "A", "myId", 1);
        super.template.sendBodyAndHeader("direct:start", "B", "myId", 1);
        super.template.sendBodyAndHeader("direct:start", "F", "myId", 2);
        super.template.sendBodyAndHeader("direct:start", "C", "myId", 1);

        assertMockEndpointsSatisfied();

    }

    @Test
    @Ignore
    public void testXMLWithThreadPool() throws InterruptedException {

        final MockEndpoint mock = super.getMockEndpoint("mock:result");

        mock.setExpectedMessageCount(2);


        super.template.sendBody("direct:start",
                "<order name=\"motor\" amount=\"1000\" customer=\"honda\"/>");
        super.template.sendBody("direct:start",
                "<order name=\"motor\" amount=\"500\" customer=\"toyota\"/>");
        super.template.sendBody("direct:start",
                "<order name=\"gearbox\" amount=\"200\" customer=\"toyota\"/>");

        assertMockEndpointsSatisfied();

    }

    @Test
    @Ignore
    public void testAggregationCompletionCondition() throws InterruptedException {

        final MockEndpoint mock = super.getMockEndpoint("mock:out");

        mock.setExpectedMessageCount(2);

        super.template.sendBodyAndHeader("direct:in", "One", "group", "odd");
        super.template.sendBodyAndHeader("direct:in", "Two", "group", "even");
        super.template.sendBodyAndHeader("direct:in", "Three", "group", "odd");
        super.template.sendBodyAndHeader("direct:in", "Four", "group", "even");
        super.template.sendBodyAndHeader("direct:in", "Five", "group", "odd");
        super.template.sendBodyAndHeader("direct:in", "Six", "group", "even");
        super.template.sendBodyAndHeader("direct:in", "Seven", "group", "odd");
        super.template.sendBodyAndHeader("direct:in", "Eight", "group", "even");
        super.template.sendBodyAndHeader("direct:in", "Nine", "group", "odd");
        super.template.sendBodyAndHeader("direct:in", "Ten", "group", "even");

        assertMockEndpointsSatisfied();

        List<Exchange> receivedExchanges = mock.getReceivedExchanges();
        @SuppressWarnings("unchecked")
        Set<String> odd = Collections.checkedSet(receivedExchanges.get(0).getIn().getBody(Set.class), String.class);
        assertTrue((odd.containsAll(Arrays.asList("One", "Three", "Five", "Seven", "Nine"))));

        @SuppressWarnings("unchecked")
        Set<String> even = Collections.checkedSet(receivedExchanges.get(1).getIn().getBody(Set.class), String.class);
        assertTrue(even.containsAll(Arrays.asList("Two", "Four", "Six", "Eight", "Ten")));
    }

    @Test
    public void testAggregationCompletionSize() throws InterruptedException {
        final MockEndpoint mock = super.getMockEndpoint("mock:out");
        mock.setExpectedMessageCount(2);

        Map<String, Object> oddHeaders = new HashMap<String, Object>();
        oddHeaders.put("group", "odd");
        oddHeaders.put("batchSize", "5");

        Map<String, Object> evenHeaders = new HashMap<String, Object>();
        evenHeaders.put("group", "even");
        evenHeaders.put("batchSize", "4");

        super.template.sendBodyAndHeaders("direct:in", "One", oddHeaders);
        super.template.sendBodyAndHeaders("direct:in", "Two", evenHeaders);
        super.template.sendBodyAndHeaders("direct:in", "Three", oddHeaders);
        super.template.sendBodyAndHeaders("direct:in", "Four", evenHeaders);
        super.template.sendBodyAndHeaders("direct:in", "Five", oddHeaders);
        super.template.sendBodyAndHeaders("direct:in", "Six", evenHeaders);
        super.template.sendBodyAndHeaders("direct:in", "Seven", oddHeaders);
        super.template.sendBodyAndHeaders("direct:in", "Eight", evenHeaders);
        super.template.sendBodyAndHeaders("direct:in", "Nine", oddHeaders);

        assertMockEndpointsSatisfied();

        List<Exchange> receivedExchanges = mock.getReceivedExchanges();
        @SuppressWarnings("unchecked")
        Set<String> even = Collections.checkedSet(receivedExchanges.get(0).getIn().getBody(Set.class), String.class);
        assertTrue(even.containsAll(Arrays.asList("Two", "Four", "Six", "Eight")));

        @SuppressWarnings("unchecked")
        Set<String> odd = Collections.checkedSet(receivedExchanges.get(1).getIn().getBody(Set.class), String.class);
        assertTrue(odd.containsAll(Arrays.asList("One", "Three", "Five", "Seven", "Nine")));
    }



}