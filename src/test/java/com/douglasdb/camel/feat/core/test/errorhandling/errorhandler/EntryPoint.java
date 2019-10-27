package com.douglasdb.camel.feat.core.test.errorhandling.errorhandler;

import com.douglasdb.camel.feat.core.errorhandling.errorhandler.DefaultErrorHandlerAsyncRoute;
import com.douglasdb.camel.feat.core.errorhandling.errorhandler.OrderService;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.cdi.Mock;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    public void setUp() throws Exception {
        deleteDirectory("D:/.camel/data/orders");
        super.setUp();
    }


    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("orderService", new OrderService());

        return jndi;
    }


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {

       // super.context.setStreamCaching(true);

        return new DefaultErrorHandlerAsyncRoute();
        //ScopeRoute();
        //DefaultErrorHandlerRoute();
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

    @Test
    public void testOrderOkWithScopeRoute() throws InterruptedException {

        final MockEndpoint mockFile = super.getMockEndpoint("mock:file");

        mockFile.setExpectedMessageCount(1);

        MockEndpoint mockQueueOrder = getMockEndpoint("mock:queueOrder");
        mockQueueOrder.expectedBodiesReceived("amount=1,name=Camel in Action, id=123, status=OK");

        super.template.sendBodyAndHeader("file://D:/.camel/data/orders",
                "amount=1#name=Camel in Action", Exchange.FILE_NAME, "order.txt");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOrderActiveMQ() throws InterruptedException {

        final MockEndpoint mockFile = super.getMockEndpoint("mock:file");
        mockFile.expectedMessageCount(1);

        final MockEndpoint mockQueueOrder = getMockEndpoint("mock:queueOrder");
        mockQueueOrder.expectedMessageCount(0);

        super.template.sendBodyAndHeader("file://D:/.camel/data/orders",
                "amount=1#name=ActiveMQ in Action", Exchange.FILE_NAME, "order.txt");

        TimeUnit.SECONDS.sleep(10);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testXmlOrderFail() throws InterruptedException {


        final MockEndpoint mockFile = super.getMockEndpoint("mock:file");
        final MockEndpoint mockQueueOrder = super.getMockEndpoint("mock:queueOrder");


        mockFile.setExpectedMessageCount(0);
        mockQueueOrder.setExpectedMessageCount(0);

        final String xml = "<?xml version='1.0'?>\n" +
                "<order>\n" +
                "  <amount>1</amount><name>Camel in Action</name>\n" +
                "</order>";

        super.template.sendBodyAndHeader("file://D:/.camel/data/orders", xml,
                Exchange.FILE_NAME, "order2.xml");

        TimeUnit.SECONDS.sleep(10);

        assertMockEndpointsSatisfied();


    }

    @Test
    public void testOrderOkAsync() throws InterruptedException {

        final MockEndpoint mockQueueOrder = super.getMockEndpoint("mock:queueOrder");
        mockQueueOrder.setExpectedMessageCount(1);
        mockQueueOrder.expectedBodiesReceived("amount=1,name=Camel in Action, id=123, status=OK");

        super.template.sendBody("seda:queue.inbox","amount=1,name=Camel in Action");

        assertMockEndpointsSatisfied();

        TimeUnit.SECONDS.sleep(10);
    }


    @Test
    public void testOrderFailAsync() throws InterruptedException {

        final MockEndpoint mockQueueOrder = super.getMockEndpoint("mock:queueOrder");
        mockQueueOrder.setExpectedMessageCount(0);

        super.template.sendBody("seda:queue.inbox","amount=1,name=ActiveMQ in Action");

        TimeUnit.SECONDS.sleep(10);

        assertMockEndpointsSatisfied();
    }


}
