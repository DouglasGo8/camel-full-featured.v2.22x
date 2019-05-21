package com.douglasdb.camel.feat.core.routing.cookbook;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author douglasdias
 *
 */
public class LoadBalancerFailoverRoute extends RouteBuilder {


	public LoadBalancerFailoverRoute() {}


	/**
	 *
	 * @throws Exception
	 */
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
	
		from("direct:start")
			.loadBalance()
				.failover(-1,
						false, true)
				.to("mock:first")
				.to("direct:second")
				.to("mock:third")
			.end()
			.to("mock:out");
		
		from("direct:second")
			.throwException(new IllegalStateException("Die Motherfucker!!!"));
	}
	

}
