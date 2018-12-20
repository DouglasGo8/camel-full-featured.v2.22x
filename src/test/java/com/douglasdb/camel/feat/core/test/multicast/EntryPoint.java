package com.douglasdb.camel.feat.core.test.multicast;


import com.douglasdb.camel.feat.core.ftp.multicast.OrderWithParallelMulticastRoute;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author douglasdias
 */
public class EntryPoint extends CamelTestSupport {


    public EntryPoint() {
        // TODO Auto-generated constructor stub
    }

    /**
     *
     */
    @Override
    public void setUp() throws Exception {
        // TODO Auto-generated method stub

        super.deleteDirectory("activemq-data");
        super.setUp();

    }

    /**
     *
     */
    @Override
    protected CamelContext createCamelContext() throws Exception {
        // TODO Auto-generated method stub

        final CamelContext ctx = super.createCamelContext();

        ctx.addComponent("acmq",
                JmsComponent.jmsComponentAutoAcknowledge
                        (
                                new ActiveMQConnectionFactory("tcp://localhost:61616"))
        );

        return ctx;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        // TODO Auto-generated method stub
        return new OrderWithParallelMulticastRoute();
        //OrderWithMulticastRoute();
        //OrderWithMulticastSOERoute();
    }


    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testPlacingOrdersWithMulticastSOE() throws InterruptedException {

        super.getMockEndpoint("mock:accounting_before_exception")
                .expectedMessageCount(1);
        super.getMockEndpoint("mock:accounting").expectedMessageCount(0);
        super.getMockEndpoint("mock:production").expectedMessageCount(0);

        assertMockEndpointsSatisfied();
    }


    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testPlacingOrdersWithMulticast() throws InterruptedException {


        super.getMockEndpoint("mock:accounting").expectedMessageCount(1);
        super.getMockEndpoint("mock:production").expectedMessageCount(1);

        assertMockEndpointsSatisfied();
    }

    @Test

    public void testPlacingOrdersWithParallelMulticast() throws InterruptedException {


        super.getMockEndpoint("mock:accounting").expectedMessageCount(1);
        super.getMockEndpoint("mock:production").expectedMessageCount(1);

        assertMockEndpointsSatisfied();
    }


}
