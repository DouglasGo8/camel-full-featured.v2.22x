package com.douglasdb.camel.feat.core.test.aggregator;


import com.douglasdb.camel.feat.core.aggregator.AggregatorABCPojoRoute;
import com.douglasdb.camel.feat.core.aggregator.AggregatorTimeoutThreadPoolRoute;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.processor.aggregate.ClosedCorrelationKeyException;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author DouglasDb
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() {
        return new AggregatorTimeoutThreadPoolRoute();
        // AggregatorABCPojoRoute();
        //AggregatorABCLevelDBRoute();
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
    public void testXMLWithThreadPool() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:result");

        mock.setExpectedMessageCount(2);


        super.template.sendBody("direct:start", "<order name=\"motor\" amount=\"1000\" customer=\"honda\"/>");
        super.template.sendBody("direct:start", "<order name=\"motor\" amount=\"500\" customer=\"toyota\"/>");
        super.template.sendBody("direct:start", "<order name=\"gearbox\" amount=\"200\" customer=\"toyota\"/>");


        assertMockEndpointsSatisfied();


    }


}