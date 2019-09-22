package com.douglasdb.camel.feat.core.test.ftp;

import com.douglasdb.camel.feat.core.ftp.FtpToJmsRoute;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author douglasdias
 */
public class EntryPoint extends CamelTestSupport {

    /**
     *
     */
    public EntryPoint() {
        // TODO Auto-generated constructor stub
    }

    /**
     *
     */
    @Override
    protected CamelContext createCamelContext() throws Exception {
        // TODO Auto-generated method stub

        final CamelContext ctx = super.createCamelContext();

        ctx.addComponent("acmq",
				JmsComponent.jmsComponentAutoAcknowledge(
						new ActiveMQConnectionFactory("tcp://localhost:61616")));

        ctx.getComponent("properties", PropertiesComponent.class).setLocation("classpath:placeholder.properties");

        return ctx;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        // TODO Auto-generated method stub
        return new FtpToJmsRoute();
    }

    /**
     * @throws InterruptedException
     */
    @Test
    public void ftpToJMSWithDynamicToTest() throws InterruptedException {

        super.getMockEndpoint("mock:out").setExpectedMessageCount(1);

        assertMockEndpointsSatisfied();

    }

}
