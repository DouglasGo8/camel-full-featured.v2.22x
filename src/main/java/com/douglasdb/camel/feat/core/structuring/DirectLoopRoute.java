package com.douglasdb.camel.feat.core.structuring;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author douglasdias
 *
 */
public class DirectLoopRoute extends RouteBuilder {

	public DirectLoopRoute() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub

		from("direct:in")
			.log("${body}")
			.setHeader("loopCount")
			.constant(0)
			.to("direct:loop")
			.to("mock:out")
		.end();

		from("direct:loop")
			.log("Loop..: ${header[loopCount]}")
				.choice()
					.when(simple("${header[loopCount]} < 10"))
						.process((exchange) -> {
							Message in = exchange.getIn();
							in.setHeader("loopCount", 
									in.getHeader("loopCount", Integer.class) + 1);
						})
						.to("direct:loop")
					.otherwise()
						.log("Exiting loop...")
		.end();
	}

}
