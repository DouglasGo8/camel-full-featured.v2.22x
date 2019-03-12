package com.douglasdb.camel.feat.core.test.bean;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import com.douglasdb.camel.feat.core.bean.InvokeWithProcessorRoute;

/**
 * 
 * @author Administrador
 *
 */
public class EntryPoint extends CamelTestSupport {

	
	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new InvokeWithProcessorRoute(); 
			//InvokeWithBeanRoute();
	}
	
	@Test
	@Ignore
	public void testHelloBean() {
		final String reply = super.template()
				.requestBody("direct:hello", "Camel in action", String.class);
		
		assertEquals("Hello Camel in action", reply);
		
		
	}


	@Test
	public void testHelloBeanWithProcessor() {
		String reply = super.template()
			.requestBody("direct:hello", "Camel in action", String.class);

		assertEquals("Hello Camel in action", reply);
	}

	
	
}
