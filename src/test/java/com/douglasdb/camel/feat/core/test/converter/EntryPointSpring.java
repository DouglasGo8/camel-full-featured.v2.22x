package com.douglasdb.camel.feat.core.test.converter;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.douglasdb.camel.feat.core.domain.jaxb.PurchaseOrderJaxb;

/**
 * 
 * @author Administrator
 *
 */
public class EntryPointSpring extends CamelSpringTestSupport {

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		// TODO Auto-generated method stub
		return new ClassPathXmlApplicationContext("META-INF/spring/converter/jaxb/app-context.xml");
	}

	
	/**
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testJaxb() throws InterruptedException {

		MockEndpoint mock = super.getMockEndpoint("mock:order");

		mock.setExpectedMessageCount(1);
		mock.message(0).body().isInstanceOf(PurchaseOrderJaxb.class);
		
		super.template.sendBody("direct:order", new PurchaseOrderJaxb() {
			{
				this.setName("Camel in action");
				this.setPrice(6999d);
				this.setAmount(1);
			}
		});

		assertMockEndpointsSatisfied();

	}

}
