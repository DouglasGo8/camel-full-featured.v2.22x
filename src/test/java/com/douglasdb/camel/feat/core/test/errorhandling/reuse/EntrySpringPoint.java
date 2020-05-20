package com.douglasdb.camel.feat.core.test.errorhandling.reuse;

import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author dbatista
 */
public class EntrySpringPoint extends CamelSpringTestSupport {

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(
                "META-INF/spring/errorhandling/reuse/errorhandler/app-context.xml");
    }

    @Override
    public void setUp() throws Exception {
        deleteDirectory("F:/.camel/data/orders");
        super.setUp();
    }

    @Test
    public void testOrderOk() throws InterruptedException {

        super.getMockEndpoint("mock:dead").expectedMessageCount(0);

        final MockEndpoint mockFile = super.getMockEndpoint("mock:file");
        mockFile.setExpectedMessageCount(1);

        final MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedBodiesReceived("amount=1,name=Camel in Action,id=123,status=OK");

        super.template.sendBodyAndHeader("file://F:/.camel/data/orders",
                "amount=1#name=Camel in Action", Exchange.FILE_NAME, "order.txt");


        assertMockEndpointsSatisfied();


    }

    @Test
    public void testOrderActiveMQFail() throws InterruptedException {

        super.getMockEndpoint("mock:dead").setExpectedMessageCount(1);

        final MockEndpoint mockFile = super.getMockEndpoint("mock:file");
        mockFile.setExpectedMessageCount(1);

        final MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.setExpectedMessageCount(0);

        super.template.sendBodyAndHeader("file://F:/.camel/data/orders",
                "amount=1#name=ActiveMQ in Action", Exchange.FILE_NAME, "order.txt");

        TimeUnit.SECONDS.sleep(5);

        assertMockEndpointsSatisfied();

    }


    @Test
    public void testXmlOrderFail() throws Exception {
        // we expect the order to fail and end up in the dead letter queue
        getMockEndpoint("mock:dead").expectedMessageCount(1);

        // we do not expect the file to be converted to csv
        MockEndpoint file = getMockEndpoint("mock:file");
        file.expectedMessageCount(0);

        // and therefore no messages in the 2nd route
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedMessageCount(0);

        template.sendBodyAndHeader("file://F:/.camel/data/orders", "<?xml version=\"1.0\"?><order>"
                + "<amount>1</amount><name>Camel in Action</name></order>", Exchange.FILE_NAME, "order2.xml");

        // wait 5 seconds to let this test run
        Thread.sleep(5000);

        assertMockEndpointsSatisfied();
    }
}
