package com.douglasdb.camel.feat.core.test.transform;

import java.io.File;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import com.douglasdb.camel.feat.core.transform.OrderToCsvBeanRoute;

/**
 * 
 * @author Administrator
 *
 */
public class EntryPoint extends CamelTestSupport {
	
	
	
	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new OrderToCsvBeanRoute();
	}
	
	
	@Test
	public void testOrderToCsvBean() {
		
		String inHouse = "0000005555000001144120091209  2319@1108";
		
		super.template.sendBodyAndHeader("direct:start", inHouse, "Date", "20181112");
		final File file = new File("D:/.camel/data/inbox/minimal/report-20091209.csv");
		assertTrue("File should exist", file.exists());
		
		String body = super.context.getTypeConverter().convertTo(String.class, file);
		
		
		assertEquals("0000005555,20091209,0000011441,2319,1108", body);
		
		
	}
	
	

}
