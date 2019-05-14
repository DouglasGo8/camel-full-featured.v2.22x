package com.douglasdb.camel.feat.core.structuring.route.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * 
 * @author douglasdias
 *
 */
public class RouteStoppingProcessor implements Processor {

	public RouteStoppingProcessor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub

		final String routeName = exchange.getIn().getBody(String.class);

		final CamelContext ctxMsg = exchange.getContext();

		new Thread(() -> {
			try {
				ctxMsg.stopRoute(routeName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new RuntimeException();
			}
		}).start();

	}

}
