package com.douglasdb.camel.feat.core.extend;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.language.XPath;

/**
 * 
 * @author Administrator
 *
 */
public class MyPredicateBeanBindingRoute extends RouteBuilder {

	
	/**
	 * 
	 */
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub

		from("direct:start")
			.filter().method(this, "isWhatIWant") // must be public
			.to("mock:boston");

	}

	/**
	 * 
	 * @param city
	 * @return
	 */
	public boolean isWhatIWant(@XPath("/someXml/city/text()") String city) {
		return "Boston".equals(city);
	}

}
