package com.douglasdb.camel.feat.core.rest;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestParamType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Administrator
 *
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ParamModeRoute extends RouteBuilder {

	private final int port1;

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		restConfiguration()
			.component("undertow")
				.host("localhost")
				.port(this.port1);
		
		rest("/say")
			.get("/hello")
				.route()
				.transform()
					.simple("Hello World")
			.endRest()
			.get("/hello/{name}")
				.route()
				.transform()
					.simple("Hello ${header.name}")
			.endRest()
			.get("/hello/query/{name}?verbose={verbose}")
				.param()
					.name("verbose")
					.type(RestParamType.query)
						// ===================
						.defaultValue("false")
						// ===================
				.endParam()
				.to("direct:hello")
			.post("/bye/{name}")
				.toD("mock:${header.name}");

		// ===========================================================
		from("direct:hello")
			.log("${body}")
			.choice()
				.when(header("verbose").isEqualTo(true))
					.transform()
						.simple("Hello there ${header.name}! How are u today?")
			.endChoice()
			.otherwise()
				.transform()
					.simple("Yo ${header.name}");

			
	}

}
