package com.douglasdb.camel.feat.core.test.extend;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.douglasdb.camel.feat.core.extend.ProducePojo;

/**
 * 
 * @author Administrator
 *
 */
public class EntryPointSpring extends CamelSpringTestSupport {

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		// TODO Auto-generated method stub
		
		final String defPath = "META-INF/spring/extend/";
		final String defFile = "app-context.xml";
		
		final String routerPath = "producer/"; 
				// "consumer/"; 
				// "binding/";
		
		return new ClassPathXmlApplicationContext(defPath.concat(routerPath).concat(defFile));
	}
	
	@Test
	@Ignore
	public void testSayCamelHello() {

		final String response = super.template.requestBody("direct:normal", "Scott", String.class);

		assertEquals("Hello Scott", response);
	}

	@Test
	@Ignore
	public void testSayHelloHipster() {

		final String response = super.template.requestBody("direct:hipster", "Scott", String.class);

		assertEquals("Yo Scott", response);
	}

	@Test
	@Ignore
	public void testSayHelloUndecided() throws Exception {
		
		String response = super.template
				.requestBodyAndHeader("direct:undecided", "Scott", "hipster", true, String.class);

		assertEquals("Yo Scott", response);

		response = template.requestBodyAndHeader("direct:undecided", "Scott", "hipster", false, String.class);

		assertEquals("Hello Scott", response);
	}
	
	
	@Test
	@Ignore
	public void testPojoConsume() {

		final String response = super.template.requestBody("activemq:queue:sayhello", "Scott", String.class);
		
		System.out.println(response);
		assertEquals("Hello Scott", response);
	}
	
	@Test
	@Ignore
	public void testPojoProduce() {
	
		final ProducePojo producer = super.context.getRegistry()
											.lookupByNameAndType("producer", ProducePojo.class);
		
		final String response = producer.sayHello("Scott");
		
		assertEquals("Hello Scott", response);
	}
	
	
}
