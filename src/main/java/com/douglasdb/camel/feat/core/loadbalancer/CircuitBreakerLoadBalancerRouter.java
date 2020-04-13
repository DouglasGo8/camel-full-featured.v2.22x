package com.douglasdb.camel.feat.core.loadbalancer;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 * @author Administrator
 *
 */
public class CircuitBreakerLoadBalancerRouter extends RouteBuilder {

	
	@Override
	@SuppressWarnings("deprecation")
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("direct:start")
			//.log("don't listen")
			.loadBalance()
				.circuitBreaker(2, 2000L, Exception.class)
			.to("direct:a");
		
		
		from("direct:a")
			.log("A received: ${body}")
			.choice()
				.when(body().contains("Kaboom"))
					.throwException(new IllegalArgumentException("Damn God"))
				.end()
			.end()
			.to("mock:a");

	}

}
