package com.douglasdb.camel.feat.core.test.jms;

import com.douglasdb.camel.feat.core.jms.OrderJmsRouter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import static org.apache.camel.component.jms.JmsComponent.jmsComponentClientAcknowledge;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new OrderJmsRouter();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {

        final CamelContext camelContext = super.createCamelContext();

        camelContext.addComponent("jms",
                jmsComponentClientAcknowledge(new ActiveMQConnectionFactory("tcp://localhost:61616")));

        return camelContext;
    }

    @Test
    public void testClientGetsReply() throws InterruptedException {


        final String body = super.template.requestBody("jms:incomingOrders",
                "<order name=\"motor\" amount=\"1\" customer=\"honda\"/>", String.class);

        assertEquals("Valid", body);

    }

    @Test
    public void testInvalidMessage() throws Exception {
        final String requestBody = template.requestBody("jms:incomingOrders",
                "<order name=\"fork\" amount=\"1\" customer=\"honda\"/>", String.class);
        assertEquals("Invalid", requestBody);
    }
}
