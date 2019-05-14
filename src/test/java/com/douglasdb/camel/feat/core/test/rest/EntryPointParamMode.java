package com.douglasdb.camel.feat.core.test.rest;

import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.douglasdb.camel.feat.core.rest.ParamModeRoute;

/**
 * 
 * @author Administrator
 *
 */
public class EntryPointParamMode extends CamelTestSupport {
	
	
	private final int port1 = AvailablePortFinder.getNextAvailable();
	
	
	
	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new ParamModeRoute(port1);
	}
	
	
	@Test
	@Ignore
	public void testGet() {
		
		
		final String out = 
			super.fluentTemplate()
					.to("undertow:http://localhost:" + port1 + "/say/hello")
					.withHeader(Exchange.HTTP_METHOD, "GET")
					.request(String.class);
		
		assertEquals("Hello World", out);
	}

	@Test
	@Ignore
	public void testGetPathParam() {

		final String out = 
			super.fluentTemplate()
					.to("undertow:http://localhost:" + port1 + "/say/hello/Douglas")
					.withHeader(Exchange.HTTP_METHOD, "GET")
					.request(String.class);
		
		assertEquals("Hello Douglas", out);
	}


	@Test
	@Ignore
	public void testGetQueryParam() {
		
		String out = 
			super.fluentTemplate()
					.to("undertow:http://localhost:" + port1 + "/say/hello/query/Douglas")
					.withHeader(Exchange.HTTP_METHOD, "GET")
					.request(String.class);

		assertEquals("Yo Douglas", out);

		out = super.fluentTemplate()
					.to("undertow:http://localhost:" + port1 + "/say/hello/query/Ketty?verbose=false")
					.withHeader(Exchange.HTTP_METHOD, "GET")
					.request(String.class);
		
		assertEquals("Yo Ketty", out);
		
		out = super.fluentTemplate()
					.to("undertow:http://localhost:" + port1 + "/say/hello/query/Douglas?verbose=true")
					.withHeader(Exchange.HTTP_METHOD, "GET")
					.request(String.class);

		assertEquals("Hello there Douglas! How are u today?", out);

	}

	@Test
	public void testPost() throws Exception {
		
		final String json = "{ \"name\": \"ddb\" }";

		final MockEndpoint mock = super.getMockEndpoint("mock:ddb");

		mock.expectedBodiesReceived(json);

		String out = super.fluentTemplate()
			.to("undertow:http://localhost:" + port1 + "/say/bye/ddb")
			.withHeader(Exchange.HTTP_METHOD, "POST")
			.withHeader(Exchange.CONTENT_ENCODING, "application/json")
			.withBody(json)
				.request(String.class);

		
		log.info(out);

		assertEquals(json, out);

		assertMockEndpointsSatisfied();
	}


	final Logger log = LoggerFactory.getLogger(EntryPointParamMode.class);

}
