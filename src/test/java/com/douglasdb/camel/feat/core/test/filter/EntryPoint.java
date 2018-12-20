package com.douglasdb.camel.feat.core.test.filter;

import com.douglasdb.camel.feat.core.filter.OrderWithFilterRoute;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;


/**
 * @author douglasdias
 */
public class EntryPoint extends CamelTestSupport {

    public EntryPoint() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        // TODO Auto-generated method stub

        final CamelContext ctx = super.createCamelContext();

        ctx.addComponent("acmq", JmsComponent.jmsComponentAutoAcknowledge
                        (new ActiveMQConnectionFactory("tcp://localhost:61616")));


        return ctx;
    }


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        // TODO Auto-generated method stub
        return new OrderWithFilterRoute();
    }

    /**
     * @throws InterruptedException
     */
    @Test
    public void testPlacingOrders() throws InterruptedException {
    	
        super.getMockEndpoint("mock:xml").setExpectedMessageCount(1);
        assertMockEndpointsSatisfied();
        
    }

}
