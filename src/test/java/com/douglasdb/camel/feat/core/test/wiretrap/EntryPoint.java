package com.douglasdb.camel.feat.core.test.wiretrap;

import com.douglasdb.camel.feat.core.domain.cheese.Cheese;
import com.douglasdb.camel.feat.core.wiretrap.WireTapStateNoLeaksRoute;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.language.simple.SimpleLanguage;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author douglasdias
 */
public class EntryPoint extends CamelTestSupport {

	public EntryPoint() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new WireTapStateNoLeaksRoute();
			// WireTrapRoute2();
			// WireTrapRoute();
	}

	@Override
	protected CamelContext createCamelContext() throws Exception {
		// TODO Auto-generated method stub

		final CamelContext ctx = super.createCamelContext();

		ctx.addComponent("acmq",
				JmsComponent.jmsComponentAutoAcknowledge(new
						ActiveMQConnectionFactory("tcp://localhost:61616")));

		return ctx;
	}

	@Test
	@Ignore
	public void testPlacingOrders() throws InterruptedException {

		super.getMockEndpoint("mock:wiretap").expectedMessageCount(1);
		super.getMockEndpoint("mock:xml").expectedMessageCount(1);
		super.getMockEndpoint("mock:csv").expectedMessageCount(0);

		assertMockEndpointsSatisfied();
	}

	@Test
	@Ignore
	public void testMessageRoutedToWireTapEndpoint() throws InterruptedException {

		final String payload = "Message to be tapped";

		MockEndpoint tapped = super.getMockEndpoint("mock:tapped");
		MockEndpoint out = super.getMockEndpoint("mock:out");

		tapped.expectedBodiesReceived(payload); // exactly the same
		out.expectedBodiesReceived(payload);

		super.template.sendBody("direct:start", payload);

		assertMockEndpointsSatisfied();

	}

	@Test
	public void testOutMessageUnaffectedByTappedRoute() throws InterruptedException {

		final Cheese cheese = new Cheese();
		cheese.setAge(1);

		MockEndpoint tapped = super.getMockEndpoint("mock:tapped");
		MockEndpoint out = super.getMockEndpoint("mock:out");


		out.expectedBodiesReceived(cheese);
		out.setExpectedMessageCount(1);
		out.message(0).body().isEqualTo(cheese);
		out.message(0).expression(SimpleLanguage.simple("${body.age} == 1"));

		tapped.setExpectedMessageCount(1);
		// message changed
		tapped.message(0).expression(SimpleLanguage.simple("${body.age} == 2"));
		tapped.setResultWaitTime(1000);

		super.template.sendBody("direct:start", cheese);

		assertMockEndpointsSatisfied();

        final Cheese outCheese = out.getReceivedExchanges().get(0).getIn().getBody(Cheese.class);
        final Cheese tappedCheese = tapped.getReceivedExchanges().get(0).getIn().getBody(Cheese.class);

        LOG.info("cheese = {}; out = {}; tapped = {}", cheese, outCheese, tappedCheese);

        LOG.info("cheese == out = {}", (cheese == outCheese));
        LOG.info("cheese == tapped = {}", (cheese == tappedCheese));
        LOG.info("out == tapped = {}", (outCheese == tappedCheese));

        assertNotSame(outCheese, tappedCheese);
        assertSame(outCheese, cheese);
	}

	// WireTapStateLeaksTest.java

    /**
     *
     */
    private static final Logger LOG = LoggerFactory.getLogger(EntryPoint.class);

}
