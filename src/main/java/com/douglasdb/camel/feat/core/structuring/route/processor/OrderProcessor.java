package com.douglasdb.camel.feat.core.structuring.route.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * 
 * @author Administrator
 *
 */
public class OrderProcessor implements Processor {

	public OrderProcessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub

		final String body = exchange.getIn().getBody(String.class);
		//System.out.println("Really feel Good");
		exchange.getIn().setBody("Processed..." + body);
	}

}
