package com.douglasdb.camel.feat.core.test.rest;

import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import com.douglasdb.camel.feat.core.rest.HelloWorldRoute;

/**
 * 
 * @author Administrator
 *
 */
public class EntryPoint extends CamelTestSupport {

	private final int port1 = AvailablePortFinder.getNextAvailable();

	/**
	 * 
	 */
	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new HelloWorldRoute(port1);
	}

	/**
	 * 
	 */
	@Test
	@Ignore
	public void testHelloGet() {

		final String out = super.fluentTemplate().to("undertow:http://localhost:" + port1 + "/say/hello")
				.withHeader(Exchange.HTTP_METHOD, "GET").request(String.class);

		assertEquals("Hello World", out);

	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@Ignore
	public void testPostBye() throws InterruptedException {

		final String json = "{ \"name\": \"Scott\" }";

		MockEndpoint update = super.getMockEndpoint("mock:update");

		update.expectedBodiesReceived(json);

		super.fluentTemplate().to("undertow:http://localhost:" + port1 + "/say/bye")
				.withHeader(Exchange.HTTP_METHOD, "POST").withHeader(Exchange.CONTENT_TYPE, "application/json")
				.withBody(json).send();

		assertMockEndpointsSatisfied();
	}

	@Test
	public void testGetAll() {
		assertEquals("foo", "foo");
	}

}
