package com.douglasdb.camel.feat.core.test.loadbalancer;

import java.util.function.Consumer;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Ignore;
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
		final String subPath = "sticky";
			// "random";
			// "roundrobin";
			// "inheriterrorhandler";
			// "custom";
			// "circuit-breaker";

		return new ClassPathXmlApplicationContext(springPath.concat(subPath.concat("/app-context.xml")));

	}

	/**
	 * @throws InterruptedException
	 */

	@Test
	@Ignore
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

	@Override
	public boolean isUseAdviceWith() {
		return false;
	}

	@Test
	@Ignore
	public void testLoadBalancerWithInheritError() throws Exception {

		super.context.getRouteDefinition("start").adviceWith(super.context, new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				super.interceptSendToEndpoint("direct:a")
						// .log("Intercepting body with ${body}")
						.choice().when(body().contains("Kaboom")).throwException(new IllegalArgumentException("Damn"))
						.end().end();
			}
		});

		super.context.start();

		// A should get the 1st
		MockEndpoint a = getMockEndpoint("mock:a");
		a.expectedBodiesReceived("Hello");

		// B should get the 2nd
		MockEndpoint b = getMockEndpoint("mock:b");
		b.expectedBodiesReceived("Kaboom");

		// send in 2 messages
		super.template.sendBody("direct:start", "Hello");
		super.template.sendBody("direct:start", "Kaboom");

		assertMockEndpointsSatisfied();
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void testLoadBalancerWithFailover() throws Exception {
		// A should get the 1st
		MockEndpoint a = getMockEndpoint("mock:a");
		a.expectedBodiesReceived("Hello", "Cool", "Bye");

		// B should get the 2nd
		MockEndpoint b = getMockEndpoint("mock:b");
		b.expectedBodiesReceived("Kaboom");

		// send in 2 messages
		super.template.sendBody("direct:start", "Hello");
		super.template.sendBody("direct:start", "Kaboom");
		super.template.sendBody("direct:start", "Cool");
		super.template.sendBody("direct:start", "Bye");

		assertMockEndpointsSatisfied();
	}

	@Test
	@Ignore
	public void testLoadBalancerRoundRobinWithFailover() throws InterruptedException {
		// A should get the 1st
		MockEndpoint a = getMockEndpoint("mock:a");
		a.expectedBodiesReceived("Hello", "Boom");

		// B should get the 2nd
		MockEndpoint b = getMockEndpoint("mock:b");
		b.expectedBodiesReceived("Bye", "Kaboom");

		// send in 2 messages
		super.template.sendBody("direct:start", "Hello");
		super.template.sendBody("direct:start", "Boom");
		super.template.sendBody("direct:start", "Bye");
		super.template.sendBody("direct:start", "Kaboom");

		assertMockEndpointsSatisfied();
	}

	@Test
	@Ignore
	public void testLoadBalancer() throws InterruptedException {
		// A should get the 1st
		MockEndpoint a = getMockEndpoint("mock:a");
		a.expectedBodiesReceived("Hello", "Cool");

		// B should get the 2nd
		MockEndpoint b = getMockEndpoint("mock:b");
		b.expectedBodiesReceived("Camel Rocks", "Bye");

		// send in 2 messages
		super.template.sendBody("direct:start", "Hello");
		super.template.sendBody("direct:start", "Camel Rocks");

		super.template.sendBody("direct:start", "Cool");
		super.template.sendBody("direct:start", "Bye");

		assertMockEndpointsSatisfied();
	}

	@Test
	@Ignore
	public void testLoadBalancerWithRandom() throws InterruptedException {
		// A should get the 1st

		super.template.sendBody("direct:start", "Hello");
		super.template.sendBody("direct:start", "Camel Rocks");
		super.template.sendBody("direct:start", "Cook");
		super.template.sendBody("direct:start", "Bye");
		super.template.sendBody("direct:start", "Egma - Love Is");
		

		assertMockEndpointsSatisfied();
	}
	
	
	@Test
	
	public void testLoadBalancerWithSticky() throws InterruptedException {
		// A should get the 1st
		MockEndpoint a = getMockEndpoint("mock:a");
		a.expectedBodiesReceived("Hello", "Bye");

		// B should get the 2nd
		MockEndpoint b = getMockEndpoint("mock:b");
		b.expectedBodiesReceived("Camel Rocks", "Cool");

		// send in 2 messages
		super.template.sendBodyAndHeader("direct:start", "Hello", "type", "gold");
		super.template.sendBodyAndHeader("direct:start", "Camel Rocks", "type", "silver");
		super.template.sendBodyAndHeader("direct:start", "Cool", "type", "silver");
		super.template.sendBodyAndHeader("direct:start", "Bye", "type", "gold");

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
