package com.douglasdb.camel.feat.core.routing;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author Administrator
 *
 */
public class ThrottlerRoute extends RouteBuilder {

	
	public ThrottlerRoute() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 
	 */
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from ("direct:start")
			.to("mock:unthrottled")
				.throttle(5)
					.timePeriodMillis(10000)
				.to("mock:throttled")
				.end()
			.to("mock:after");
	}

}
