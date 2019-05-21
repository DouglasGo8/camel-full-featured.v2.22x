package com.douglasdb.camel.feat.core.test.routing.inaction;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import com.douglasdb.camel.feat.core.routing.inaction.DynamiAnnotationBeanRouter;

/**
 * 
 * @author Administrator
 *
 */
public class EntryPoint extends CamelTestSupport {

	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new DynamiAnnotationBeanRouter();
	}
	
	
	/**
	 * 
	 */
	@Test
	public void testDynamicRouter() throws Exception {
		
		super.getMockEndpoint("mock:a").expectedBodiesReceived("Camel");
		super.getMockEndpoint("mock:result").expectedBodiesReceived("Bye Camel");
		
		super.template.sendBody("direct:start", "Camel");
		
		assertMockEndpointsSatisfied();
	}
	
}
