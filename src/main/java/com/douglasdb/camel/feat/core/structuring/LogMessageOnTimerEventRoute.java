package com.douglasdb.camel.feat.core.structuring;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author douglasdias
 *
 */
public class LogMessageOnTimerEventRoute extends RouteBuilder {

	
	public LogMessageOnTimerEventRoute() {
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from("timer:logMessageTimer?period=1s")
			.to("myLogger:insideTheRoute?showHeaders=true")
			//.to("mock:out")
			.log("Event triggered by ${property.CamelTimerName} at ${header.CamelTimerFiredTime}");
		
	}
}
