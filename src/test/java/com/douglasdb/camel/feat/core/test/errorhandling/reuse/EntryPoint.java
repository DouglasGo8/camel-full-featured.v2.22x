package com.douglasdb.camel.feat.core.test.errorhandling.reuse;

import com.douglasdb.camel.feat.core.errorhandling.reuse.InboxRouteBuilder;
import com.douglasdb.camel.feat.core.errorhandling.reuse.OrderRouteBuilder;
import com.douglasdb.camel.feat.core.errorhandling.reuse.OrderService;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder[] createRouteBuilders() throws Exception {
        return new RouteBuilder[]{new InboxRouteBuilder(), new OrderRouteBuilder()};
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        // register our order service bean in the Camel registry
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("orderService", new OrderService());
        return jndi;
    }

    @Test
    public void testOrderOk() throws Exception {
        // we do not expect any errors and therefore no messages in the dead letter queue
        super.getMockEndpoint("mock:dead").expectedMessageCount(0);

        // we expect the file to be converted to csv and routed to the 2nd route
        final MockEndpoint file = getMockEndpoint("mock:file");
        file.expectedMessageCount(1);

        // we expect the 2nd route to complete
        final MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedBodiesReceived("amount=1,name=Camel in Action,id=123,status=OK");

        super.template.sendBodyAndHeader("file://F:/.camel/data/orders", "amount=1#name=Camel in Action",
                Exchange.FILE_NAME, "order.txt");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testOrderActiveMQFail() throws Exception {
        // we expect the order to fail and end up in the dead letter queue
        super.getMockEndpoint("mock:dead").setExpectedMessageCount(1);

        // we expect the file to be converted to csv and routed to the 2nd route
        final MockEndpoint file = getMockEndpoint("mock:file");
        file.expectedMessageCount(1);

        // we do not expect the 2nd route to complete
        final MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedMessageCount(0);

        super.template.sendBodyAndHeader("file://F:/.camel/data/orders",
                "amount=1#name=ActiveMQ in Action", Exchange.FILE_NAME, "order.txt");

        // wait 5 seconds to let this test run
        Thread.sleep(5000);

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testXmlOrderFail() throws Exception {
        // we expect the order to fail and end up in the dead letter queue
        super.getMockEndpoint("mock:dead").expectedMessageCount(1);

        // we do not expect the file to be converted to csv
        final MockEndpoint file = getMockEndpoint("mock:file");
        file.expectedMessageCount(0);

        // and therefore no messages in the 2nd route
        final MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedMessageCount(0);

        template.sendBodyAndHeader("file://F:/.camel/data/orders", "<?xml version=\"1.0\"?><order>"
                + "<amount>1</amount><name>Camel in Action</name></order>", Exchange.FILE_NAME, "order2.xml");

        // wait 5 seconds to let this test run
        Thread.sleep(5000);

        assertMockEndpointsSatisfied();
    }
}
