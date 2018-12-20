package com.douglasdb.camel.feat.core.test.converter;

import java.math.BigDecimal;
import java.util.List;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Ignore;
import org.junit.Test;

import com.douglasdb.camel.feat.core.converter.PurchaseOrderBindyRoute;
import com.douglasdb.camel.feat.core.converter.PurchaseOrderServiceBean;
import com.douglasdb.camel.feat.core.domain.PurchaseOrderCsv;
import com.douglasdb.camel.feat.core.domain.PurchaseOrderDefault;
import com.douglasdb.camel.feat.core.domain.jaxb.PurchaseOrderJaxb;

/**
 * 
 * @author Administrator
 *
 */
public class EntryPoint extends CamelTestSupport {
	
	
	
	
	//@Override
	protected JndiRegistry createRegistry() throws Exception {
		// TODO Auto-generated method stub
		
		JndiRegistry jndi = super.createRegistry();
		jndi.bind("purchaseOrderService", PurchaseOrderServiceBean.class);
		
		return jndi;
	}

	/**
	 * 
	 */
	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		// TODO Auto-generated method stub
		return new PurchaseOrderBindyRoute();				
				// PurchaseOrderUnmarshalBindyRoute();
				// PurchaseOrderVelocityRoute();
				// PurchaseOrderJSONRoute(); 
				// PurchaseOrderCsvConverterRoute(); 
				// PurchaseOrderConverterRoute();
	}
	
	/**
	 * 
	 */
	@Test
	@Ignore
	public void testPurchaseOrderConverterDef() {
		
		byte[] payload = "##START##AKC4433   179.95    3##END##".getBytes();
		
		PurchaseOrderDefault order = super.template
				.requestBody("direct:start", payload, PurchaseOrderDefault.class);
		
		assertNotNull(order);
		
		System.out.println(order);
		
        assertEquals("AKC4433", order.getName());
        assertEquals("179.95", order.getPrice().toString());
        assertEquals(3, order.getAmount());
		
		
		
	}
	
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@Ignore
	public void testCsv() throws InterruptedException {
		
		
		MockEndpoint mock = super.getMockEndpoint("mock:csv");
		
		mock.expectedMessageCount(2);
		
		
		assertMockEndpointsSatisfied();
		
		
		@SuppressWarnings("unchecked")
		List<Object> line1 = mock.
			getReceivedExchanges()
				.get(0)
				.getIn()
				.getBody(List.class);
		
		
        assertEquals("Camel in Action", line1.get(0));
        assertEquals("6999", line1.get(1));
        assertEquals("1", line1.get(2));
        
        @SuppressWarnings("unchecked")
        List<Object> line2 = mock
        	.getReceivedExchanges()
        	.get(1)
        	.getIn()
        	.getBody(List.class);
        
        assertEquals("Activemq in Action", line2.get(0));
        assertEquals("4495", line2.get(1));
        assertEquals("2", line2.get(2));
		
		
	}
	
	
	/**
	 * 
	 */
	@Test
	@Ignore
	public void testJSON() {
		  
		final String result = template
				.requestBody("jetty:http://localhost:10222/order/service?id=123",
						null, String.class);
		
		System.out.println(result);
		
		assertNotNull(result);
		assertTrue(result.contains("Camel in Action"));
		     
	}
	
	/**
	 * @throws InterruptedException 
	 * 
	 */
	@Test
	@Ignore
	public void testVelocity() throws InterruptedException {
		
		MockEndpoint mock = super.getMockEndpoint("mock:mail");
		
		mock.expectedMessageCount(1);
		
		mock.message(0).header("Subject").isEqualTo("Thanks for ordering");
	    mock.message(0).header("From").isEqualTo("donotreply@riders.com");
	    
	    mock.message(0).body()
	    	.contains("Thank you for ordering 1.0 piece(s) of Camel in action at a cost of 6999.0.");

		
		
		super.template.sendBody("direct:mail", new PurchaseOrderJaxb() {
			{
				this.setName("Camel in action");
				this.setPrice(6999d);
				this.setAmount(1);
			}
		});
		
		
		assertMockEndpointsSatisfied();
		
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testCsvBindy() throws InterruptedException {
		
		
		MockEndpoint mock = super.getMockEndpoint("mock:result");
		
		
		mock.expectedMessageCount(1);
		mock.expectedBodiesReceived("Camel in Action,39.95,1\n");
		
		
		final PurchaseOrderCsv order = new PurchaseOrderCsv();
		//
		// mandatory to bindy setFields as did below
		// can't be new PurchaseOrderCsv() {{ fields }}
		//
		order.setAmount(1);
		order.setPrice(new BigDecimal("39.95"));
		order.setName("Camel in Action");
		
		super.template.sendBody("direct:toCsv", order);
		
		assertMockEndpointsSatisfied();
		
		
		
		
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@Ignore
	public void testUnmarshalBindyMultipleRows() throws InterruptedException {
		
		MockEndpoint mock = super.getMockEndpoint("mock:result");
		
		mock.expectedMessageCount(1);

		super.template.sendBody("direct:toObject", "Camel in Action,39.95,1\nActiveMQ in Action,39.95,1");
		
		@SuppressWarnings("unchecked")
		List<PurchaseOrderCsv> rows = mock.getReceivedExchanges()
				.get(0)
				.getIn()
				.getBody(List.class);
		
	    PurchaseOrderCsv order = (PurchaseOrderCsv) rows.get(0);
	    assertNotNull(order);
	   
	    PurchaseOrderCsv order2 = (PurchaseOrderCsv) rows.get(1);
	    assertNotNull(order2);  
		
		
		 
		assertMockEndpointsSatisfied();
		
	    assertEquals("Camel in Action", order.getName());
	    assertEquals("39.95", order.getPrice().toString());
	    assertEquals(1, order.getAmount());
	    //
	    assertEquals("ActiveMQ in Action", order2.getName());
	    assertEquals("39.95", order2.getPrice().toString());
	    assertEquals(1, order2.getAmount());
	 
	 
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	@Test
	@Ignore
	public void testUnmarshalBindy() throws InterruptedException {

		MockEndpoint mock = super.getMockEndpoint("mock:result");
		
		mock.expectedMessageCount(1);

		super.template.sendBody("direct:toObject", "Camel in Action,39.95,1");
		
		
		assertMockEndpointsSatisfied();
		
		PurchaseOrderCsv order = mock.getReceivedExchanges()
				.get(0)
				.getIn()
				.getBody(PurchaseOrderCsv.class);
		
	    assertNotNull(order);
	   
	    
		 

		
	    assertEquals("Camel in Action", order.getName());
	    assertEquals("39.95", order.getPrice().toString());
	    assertEquals(1, order.getAmount());
	    
	}
	


}
