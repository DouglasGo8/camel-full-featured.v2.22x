package com.douglasdb.camel.feat.core.test.transactions.idempotentconsumer;

import com.douglasdb.camel.feat.core.transactions.idempotentconsumer.IdempotentConsumerMultipleEndpointsRoute;
import com.douglasdb.camel.feat.core.transactions.idempotentconsumer.IdempotentConsumerRoute;
import com.douglasdb.camel.feat.core.transactions.idempotentconsumer.IdempotentConsumerSkipDuplicateRoute;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;


public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new IdempotentConsumerMultipleEndpointsRoute();
                // IdempotentConsumerSkipDuplicateRoute();
                // IdempotentConsumerRoute();
    }

    @Test
    @Ignore
    public void testReplayOfSameMessageWillNotTriggerCall() throws InterruptedException {

        MockEndpoint ws = super.getMockEndpoint("mock:ws");
        ws.setExpectedMessageCount(1);

        MockEndpoint out = super.getMockEndpoint("mock:out");
        out.setExpectedMessageCount(2);


        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1);
        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1); // again

        assertMockEndpointsSatisfied();

    }

    @Test
    @Ignore
    public void testReplayOfSameMessageWillTriggerDuplicateEndpoint() throws InterruptedException {
        MockEndpoint mockWs = getMockEndpoint("mock:ws");
        mockWs.setExpectedMessageCount(1);

        MockEndpoint mockDuplicate = getMockEndpoint("mock:duplicate");
        mockDuplicate.setExpectedMessageCount(1);

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(2);

        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1);
        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1); // again

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testReplayOfSameMessageWillNotTriggerCallWithMultipleEndpoints() throws InterruptedException {
        MockEndpoint mockWs = getMockEndpoint("mock:ws");
        mockWs.setExpectedMessageCount(1);

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.setExpectedMessageCount(2);

        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1);
        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1); // again

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testErrorAfterBlockWillMeanBlockNotReentered() throws InterruptedException {
        MockEndpoint mockWs = getMockEndpoint("mock:ws");
        // the web service should be invoked once only
        mockWs.setExpectedMessageCount(1);

        MockEndpoint mockOut = getMockEndpoint("mock:out");
        mockOut.whenExchangeReceived(1, exchange -> {
            throw new IllegalStateException("Out system is down");
        });
        mockOut.setExpectedMessageCount(2);

        try {
            template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1);
            fail("No exception thrown");
        } catch (CamelExecutionException cee) {
            assertEquals("Out system is down", cee.getCause().getMessage());
        }
        template.sendBodyAndHeader("direct:in", "Insert", "messageId", 1); // again

        assertMockEndpointsSatisfied();
    }

}
