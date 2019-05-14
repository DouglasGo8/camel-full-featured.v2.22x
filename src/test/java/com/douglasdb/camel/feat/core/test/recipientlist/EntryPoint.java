package com.douglasdb.camel.feat.core.test.recipientlist;


import com.douglasdb.camel.feat.core.recipientlist.RecipientListUnrecognizedEndpointRoute;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
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

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        // TODO Auto-generated method stub
        return new RecipientListUnrecognizedEndpointRoute();

        // RecipientListRoute();
        // OrderWithRecipientListBean();
        // OrderWithRecipientListAnnotation();
    }

    /**
     *
     */
    //@Override
    protected CamelContext createCamelContext() throws Exception {
        // TODO Auto-generated method stub

        final CamelContext ctx = super.createCamelContext();

        ctx.addComponent("acmq",
                JmsComponent.jmsComponentAutoAcknowledge(new
                        ActiveMQConnectionFactory("tcp://localhost:61616")));

        return ctx;
    }

    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testPlacingOrdersRecipientListAnnotation() throws InterruptedException {
        getMockEndpoint("mock:accounting").expectedMessageCount(2);
        getMockEndpoint("mock:production").expectedMessageCount(1);
        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testPlacingOrdersRecipientList() throws InterruptedException {
        getMockEndpoint("mock:accounting").expectedMessageCount(2);
        getMockEndpoint("mock:production").expectedMessageCount(1);
        assertMockEndpointsSatisfied();
    }

    @Test
    @Ignore
    public void testNormalOrder() throws InterruptedException {

        final String payload = "body";

        super.template.sendBodyAndHeader("direct:start", payload, "orderType", "normal");

        super.getMockEndpoint("mock:billing").setExpectedMessageCount(1);
        super.getMockEndpoint("mock:order.normal").setExpectedMessageCount(1);
        super.getMockEndpoint("mock:order.priority").setExpectedMessageCount(0);
        super.getMockEndpoint("mock:unrecognized").setExpectedMessageCount(0);

        assertMockEndpointsSatisfied();

    }


    /**
     * @throws InterruptedException
     */
    @Test
    @Ignore
    public void testUnknownOrder() throws InterruptedException {

        final String payload = "body";

        super.template.sendBody("direct:start", payload);

        super.getMockEndpoint("mock:billing").setExpectedMessageCount(0);
        super.getMockEndpoint("mock:order.normal").setExpectedMessageCount(0);
        super.getMockEndpoint("mock:order.priority").setExpectedMessageCount(0);
        super.getMockEndpoint("mock:unrecognized").setExpectedMessageCount(1);

        assertMockEndpointsSatisfied();

    }

    @Test
    public void testMessageRoutedToMulticastEndpoints() throws InterruptedException {
        final String messageBody = "Message to be distributed via recipientList";


        MockEndpoint mock_first = super.getMockEndpoint("mock:first");
        MockEndpoint mock_second = super.getMockEndpoint("mock:second");

        mock_first.setExpectedMessageCount(1);
        mock_first.message(0).body().isEqualTo(messageBody);
        mock_second.setExpectedMessageCount(1);
        mock_second.message(0).body().isEqualTo(messageBody);

        super.template.sendBody("direct:start", messageBody);

        assertMockEndpointsSatisfied();

    }


}
