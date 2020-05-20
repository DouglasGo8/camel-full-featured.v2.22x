package com.douglasdb.camel.feat.core.routing.inaction;

import org.apache.camel.DynamicRouter;
import org.apache.camel.Exchange;
import org.apache.camel.Header;


/**
 * 
 * @author Administrator
 *
 */
public class DynamicAnnotationBean {

	@DynamicRouter
	public String route(String body, @Header(Exchange.SLIP_ENDPOINT) String previous) {
		if (previous == null) {
			// 1st time
			return "mock://a";
		} else if ("mock://a".equals(previous)) {
			// 2nd time - transform the message body using the simple language
			return "language://simple:Bye ${body}";
		} else {
			// no more, so return null to indicate end of dynamic router
			return null;
		}
	}

}
