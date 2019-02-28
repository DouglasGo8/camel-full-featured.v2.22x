package com.douglasdb.camel.feat.core.rest;

import org.apache.camel.builder.RouteBuilder;

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
			.endRest();
			
	}

}
