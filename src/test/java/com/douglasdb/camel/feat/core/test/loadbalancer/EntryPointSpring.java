package com.douglasdb.camel.feat.core.test.loadbalancer;

import java.util.function.Consumer;

import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 */
public class EntryPointSpring extends CamelSpringTestSupport {

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		//
		final String springPath = "META-INF/spring/loadbalancer/";
		//
		final String subPath = "circuit-breaker";

		return new ClassPathXmlApplicationContext(springPath.concat(subPath.concat("/app-context.xml")));

	}

	/**
	 * 
	 * @throws InterruptedException
	 */

	@Test
	public void testLoadBalancerWithCircuitBreakerStrategy() throws InterruptedException {

		MockEndpoint mock_a = super.getMockEndpoint("mock:a");

		mock_a.setExpectedMessageCount(1);
		mock_a.expectedBodiesReceived("Got through!");

		// send in 4 messages
		this.sendMessage("direct:start", ar -> ar.getIn().setBody("Kaboom"));
		this.sendMessage("direct:start", ar -> ar.getIn().setBody("Kaboom"));

		// circuit should break here as we've had 2 exception occur when accessing
		// remote service

		// this call should fail as blocked by circuit breaker
		this.sendMessage("direct:start", ar -> ar.getIn().setBody("Blocked"));

		// wait so circuit breaker will timeout and go into half-open state
		Thread.sleep(5000);

		// should success
		this.sendMessage("direct:start", ar -> ar.getIn().setBody("Got through!"));

		assertMockEndpointsSatisfied();

	}

	/**
	 * @param endpoint
	 * @param param
	 */
	private void sendMessage(final String endpoint, Consumer<Exchange> param) {
		super.template.send(endpoint, (exchange) -> param.accept(exchange));
	}

}
