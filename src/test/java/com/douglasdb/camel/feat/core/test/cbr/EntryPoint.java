package com.douglasdb.camel.feat.core.test.cbr;

import com.douglasdb.camel.feat.core.cbr.OrderWithStopOtherwiseComplexRoute;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.jms.JmsComponent;
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
	@Produce(uri = "direct:in")
	private ProducerTemplate in;

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

		ctx.addComponent("acmq", JmsComponent.jmsComponentAutoAcknowledge
				(new ActiveMQConnectionFactory("tcp://localhost:61616")));

		return ctx;
	}

	/**
	 *
	 */
	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new OrderWithStopOtherwiseComplexRoute();
		// OrderWithOtherwiseRoute();
		// OrderRouterOtherwiseFullRoute();
	}

	/**
	 * @throws InterruptedException
	 */
	@Test
	@Ignore
	public void testPlacingOrdersFull() throws InterruptedException {

		super.getMockEndpoint("mock:xml").setExpectedMessageCount(1);
		super.getMockEndpoint("mock:csv").setExpectedMessageCount(2);
		super.getMockEndpoint("mock:bad").setExpectedMessageCount(1);

		assertMockEndpointsSatisfied();

	}

	/**
	 * @throws InterruptedException
	 */
	@Test
	@Ignore
	public void testPlacingOrdersSimple() throws InterruptedException {

		super.getMockEndpoint("mock:xml").setExpectedMessageCount(1);
		super.getMockEndpoint("mock:csv").setExpectedMessageCount(1);

		assertMockEndpointsSatisfied();

	}

	/**
	 * @throws InterruptedException
	 */
	@Test
	public void testPlacingOrderWithStop() throws InterruptedException {

		super.getMockEndpoint("mock:xml").setExpectedMessageCount(1);
		super.getMockEndpoint("mock:csv").setExpectedMessageCount(2);
		super.getMockEndpoint("mock:bad").setExpectedMessageCount(1);
		super.getMockEndpoint("mock:continued").expectedMessageCount(3);

		assertMockEndpointsSatisfied();

	}
}
