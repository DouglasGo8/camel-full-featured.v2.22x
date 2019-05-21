package com.douglasdb.camel.feat.core.routing.inaction;

import org.apache.camel.Exchange;
import org.apache.camel.Header;

/**
 * 
 * @author Administrator
 *
 */
public class DynamicRouterBean {
	
	/**
	 * 
	 * @param body
	 * @param previous
	 * @return
	 */
	public String router(String body, @Header(Exchange.SLIP_ENDPOINT) String previous) {
		
		return whereToGo(body, previous);
		
		
	}
	
	
	
	  /**
     * Method which computes where to go next
     */
    private String whereToGo(String body, String previous) {
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
