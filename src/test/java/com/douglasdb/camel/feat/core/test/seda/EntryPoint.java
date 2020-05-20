package com.douglasdb.camel.feat.core.test.seda;

import com.douglasdb.camel.feat.core.seda.SedaRoute;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new SedaRoute();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {

        final CamelContext ctx = super.createCamelContext();

        ctx.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(new
                ActiveMQConnectionFactory("tcp://localhost:61616")));

        return ctx;
    }

    @Test
    public void testPrintFile() throws InterruptedException {

        getMockEndpoint("mock:accounting").expectedMessageCount(1);
        getMockEndpoint("mock:production").expectedMessageCount(1);

        // should have one incoming message for each queue

        assertMockEndpointsSatisfied();
    }



}
