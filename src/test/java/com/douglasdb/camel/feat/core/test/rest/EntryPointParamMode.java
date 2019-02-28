package com.douglasdb.camel.feat.core.test.rest;

import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

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
	public void testGet() {
		
		
		final String out = super.fluentTemplate()
						.to("undertow:http://localhost:" + port1 + "/say/hello")
						.withHeader(Exchange.HTTP_METHOD, "GET")
						.request(String.class);
		
		assertEquals("Hello World", out);
	}

}
