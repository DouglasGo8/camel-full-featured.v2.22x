package com.douglasdb.camel.feat.core.test.netty;

import com.douglasdb.camel.feat.core.netty.NettyCustomCodecRoute;
import com.douglasdb.camel.feat.core.netty.WelderDecoder;
import com.douglasdb.camel.feat.core.netty.WelderEncoder;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * @author dbatista
 */
public class EntryPoint extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new NettyCustomCodecRoute();
        //NettyTcpRoute();
    }


    @Override
    protected JndiRegistry createRegistry() throws Exception {

        JndiRegistry jndi = super.createRegistry();
        jndi.bind("welderDecoder", new WelderDecoder());
        jndi.bind("welderEncoder", new WelderEncoder());
        return jndi;
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {

        final CamelContext ctx = super.createCamelContext();

        ctx.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(new
                ActiveMQConnectionFactory("tcp://localhost:61616")));

        return ctx;
    }


    @Test
    public void testSendToTcp() throws InterruptedException {

        MockEndpoint mock = super.getMockEndpoint("mock:end");

        mock.setExpectedMessageCount(1);
        mock.expectedBodiesReceived(STATUS_GOOD);

        super.template.sendBody("netty4:tcp://localhost:8999?textline=true&sync=false", STATUS_GOOD);

        mock.assertIsSatisfied();


    }

    @Test
    public void testSendCToCustomTcp() throws InterruptedException {
        MockEndpoint mock = getMockEndpoint("mock:end");
        mock.expectedBodiesReceived(STATUS_GOOD);

        template.sendBody("netty4:tcp://localhost:8998?encoder=#welderEncoder&decoder=#welderDecoder&sync=false",
                "23717481");

        mock.assertIsSatisfied();
    }

    private static final String STATUS_GOOD = "MachineID=2371748;Status=Good";

}
