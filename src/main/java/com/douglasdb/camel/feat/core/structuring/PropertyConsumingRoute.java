package com.douglasdb.camel.feat.core.structuring;

import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author Administrator
 *
 */
public class PropertyConsumingRoute extends RouteBuilder {

	public PropertyConsumingRoute() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub

		from("{{start.endpoint}}")
			.transform()
				.simple("{{transform.message}}: ${body}")
			.log("Set message to ${body}")
			.to("{{end.endpoint}}")
			.end();
	}

}
