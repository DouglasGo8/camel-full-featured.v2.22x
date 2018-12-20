package com.douglasdb.camel.feat.core.routing;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author douglasdias
 *
 */
public class MyDynamicRouter {

	private static final Logger LOG = LoggerFactory.getLogger(MyDynamicRouter.class);

	private static final String PROPERTY_NAME_INVOKED = "invoked";

	public MyDynamicRouter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param body
	 * @param props
	 */
	public String routeMe(String body, @ExchangeProperties Map<String, Object> props) {

		LOG.info("Exchange.SLIP_ENDPOINT = {}, invoked = {}", 
				props.get(Exchange.SLIP_ENDPOINT),
				props.get(PROPERTY_NAME_INVOKED));

		int invoked = 0;

		Object current = props.get(PROPERTY_NAME_INVOKED);

		if (null != current) {
			invoked = Integer.valueOf(current.toString());
		}

		invoked++;
		props.put(PROPERTY_NAME_INVOKED, invoked);

		switch (invoked) {
		case 1:
			return "mock:a";
		case 2:
			return "mock:b,mock:c";
		case 3:
			return "direct:other";
		case 4:
			return "mock:result";
		default:
			return null;
		}

	}
}
