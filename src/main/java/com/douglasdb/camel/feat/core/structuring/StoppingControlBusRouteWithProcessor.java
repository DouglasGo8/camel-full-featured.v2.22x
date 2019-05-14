package com.douglasdb.camel.feat.core.structuring;

import org.apache.camel.builder.RouteBuilder;

import com.douglasdb.camel.feat.core.structuring.route.processor.RouteStoppingProcessor;

/**
 * 
 * @author douglasdias
 *
 */
public class StoppingControlBusRouteWithProcessor extends RouteBuilder {

	public StoppingControlBusRouteWithProcessor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub

		from("direct:in")
			.routeId("mainRoute")
			.log("Stopping route")
			.process(new RouteStoppingProcessor())
			//.to("controlbus:route?routeId=mainRoute&action=stop&async=true").log("Signalled to stop route")
		.to("mock:out");

		from("timer:statusChecker")
			.log("Status checking...")
			.routeId("statusChecker")
			.to("controlbus:route?routeId=mainRoute&action=status")
			.filter(simple("${body} == 'Stopped'"))
		.to("mock:stopped");

	}

}
