package com.douglasdb.camel.feat.core.test.rest;

import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author Administrator
 *
 */
public class EntryPointSpring extends CamelSpringTestSupport {

	
	/**
	 * 
	 */
	private final int port1 = AvailablePortFinder.getNextAvailable();
	
	@Override
	protected AbstractApplicationContext createApplicationContext() {
		// TODO Auto-generated method stub
		
		System.setProperty("port1", String.valueOf(port1));
		
		final String file = "app-context.xml";
		final String path = "META-INF/spring/rest/hello/";
		
		return new ClassPathXmlApplicationContext(path.concat(file));
	}
	
	@Test
	@Ignore
	public void testHelloGet() {
		
		final String out = super
				.fluentTemplate()
					.to("undertow:http://localhost:" + port1 + "/say/hello")
					.withHeader(Exchange.HTTP_METHOD, "GET")
					.request(String.class);
		
		assertEquals("Hello World", out);
	}
	
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testHelloPost() throws InterruptedException {
		
		final String json = "{ \"name\": \"Scott\" }";
		
		MockEndpoint update = super.getMockEndpoint("mock:update");
		
		update.expectedBodiesReceived(json);
		
		super.fluentTemplate()
			.to("undertow:http://localhost:" + port1 + "/say/bye")
			.withHeader(Exchange.HTTP_METHOD, "POST")
			.withHeader(Exchange.CONTENT_TYPE, "application/json")
			.withBody(json)
			.send();
		
		assertMockEndpointsSatisfied();
	}
	

}
