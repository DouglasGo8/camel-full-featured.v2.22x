package com.douglasdb.camel.feat.core.extend;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;

/**
 * 
 * @author Administrator
 *
 */
public class ProducePojo {

	@Produce
	private ProducerTemplate template;

	/**
	 * 
	 * @param name
	 * @return
	 */
	public String sayHello(String name) {
		// System.out.println(name);
		return template.requestBody("activemq:queue:sayHello", name, String.class);
	}
}
