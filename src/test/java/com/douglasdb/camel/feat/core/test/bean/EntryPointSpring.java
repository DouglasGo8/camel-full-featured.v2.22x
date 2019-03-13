package com.douglasdb.camel.feat.core.test.bean;

import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.w3c.dom.Document;

/**
 * 
 * @author DouglasDb
 *
 */
public class EntryPointSpring extends CamelSpringTestSupport {

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		// TODO Auto-generated method stub
		
		final String path = "META-INF/spring/bean/";
		
		final String subPath = "namespace/"; // bean
		
		return new ClassPathXmlApplicationContext(path.concat(subPath.concat("app-context.xml")));
	}
	
	@Override
	public void setUp() throws Exception {
		// TODO Auto-generated method stub
		//super.deleteDirectory("META-INF/order");
		super.setUp();
	}

	@Test
	@Ignore
	public void testHelloBean() {
		
		// Synchronous
		final String reply  = super.template
				.requestBody("direct:hello", "Camel in Action", String.class );
		
		assertEquals("Hello Camel in Action", reply);
		
	}
	
	@Test
	public void sendIncomingOrderWithNamespace() throws InterruptedException {
	
		MockEndpoint mock = super.getMockEndpoint("mock:queue:order");
		mock.expectedMessageCount(1);
		
		String body = "<order xmlns='http://camelinaction.com/order' customerId='4444'><item>Camel in action</item></order>";
		
		Document xml = super.context.getTypeConverter()
				.convertTo(Document.class, body);
		
		//System.out.println(xml.getElementsByTagName("order").item(0).getLocalName());
		
		template.sendBodyAndHeader("file://src/main/resources/META-INF/order/", 
				xml, Exchange.FILE_NAME, "order.xml");
		
		
		mock.assertIsSatisfied();
	}

}
