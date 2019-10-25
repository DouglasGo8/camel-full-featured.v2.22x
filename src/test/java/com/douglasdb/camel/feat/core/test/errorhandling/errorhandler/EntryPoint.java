package com.douglasdb.camel.feat.core.test.errorhandling.errorhandler;

import com.douglasdb.camel.feat.core.errorhandling.errorhandler.DefaultErrorHandlerRoute;
import com.douglasdb.camel.feat.core.errorhandling.errorhandler.OrderService;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("orderService", new OrderService());

        return jndi;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new DefaultErrorHandlerRoute();
    }


    @Test
    public void testOrderOk() throws InterruptedException {

        final MockEndpoint mock = super.getMockEndpoint("mock:queueOrder");

        mock.expectedBodiesReceived("amount=1,name=Camel in Action, id=123, status=OK");

        super.template.sendBody("seda:queue.inbox", "amount=1,name=Camel in Action");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOrderFail() throws InterruptedException {

       final MockEndpoint mock = super.getMockEndpoint("mock:queueOrder");

        mock.setExpectedMessageCount(0);

        super.template.sendBody("seda:queue.inbox", "amount=1,name=ActiveMQ in Action");

        // wait 5 seconds to let this test run as we expect 0 messages
        Thread.sleep(5000);


        assertMockEndpointsSatisfied();
    }

}
