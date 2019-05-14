package com.douglasdb.camel.feat.core.extend;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author Administrator
 *
 */
public class MyProcessorInlineRoute extends RouteBuilder {

	
	public MyProcessorInlineRoute() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		
		from("direct:start")
		
			.process((exchange) -> {
			
				String result = "Unknown language";
				
				final Message inMessage = exchange.getIn();
				final String body = inMessage.getBody(String.class);
				final String lang = inMessage.getHeader("lang", String.class);
				
				if ("en".equals(lang))
					result = "Hello " + body;
				else if ("fr".equals(lang))
					result = "Bonjour " + body;
				
				inMessage.setBody(result); })
			
			.log("${body}")
			.to("mock:result");
		
	}

	
	
}
