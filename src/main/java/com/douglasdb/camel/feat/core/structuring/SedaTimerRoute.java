package com.douglasdb.camel.feat.core.structuring;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 * @author douglasdias
 *
 */
public class SedaTimerRoute extends RouteBuilder {

	
	private final static int TIMER_PERIOD = 200;
	
	
	public SedaTimerRoute() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("timer:ping?period=" + TIMER_PERIOD)
			.startupOrder(2)
			.transform(constant("Ping"))
			.to("seda:longRunningPhase");
		
		from("seda:longRunningPhase?concurrentConsumers=15")
			.process((exchange) -> {
				
				Thread.sleep(3000);
				exchange
					.getIn()
					.setBody("Long running process finished");
				
			}).to("mock:out");
	}

}
