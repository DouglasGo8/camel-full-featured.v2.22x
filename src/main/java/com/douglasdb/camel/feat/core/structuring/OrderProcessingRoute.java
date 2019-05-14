package com.douglasdb.camel.feat.core.structuring;

import javax.annotation.PostConstruct;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.Validate;

import com.douglasdb.camel.feat.core.structuring.route.processor.OrderFileNameProcessor;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author douglasdias
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderProcessingRoute extends RouteBuilder {

	private String id;
	private String inputDirectory;
	private String outputDirectory;
	private OrderFileNameProcessor orderFileNameProcessor;

	public OrderProcessingRoute() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void validateProperties() {
		Validate.notEmpty(this.id, "id is empty");
		Validate.notEmpty(this.inputDirectory, "inputDirectory is empty");
		Validate.notEmpty(this.outputDirectory, "outputDirectory is empty");
		Validate.notNull(this.orderFileNameProcessor, "orderFileNameProcess is empty");
	}

	/**
	 * 
	 */
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub

		fromF("file://%s", this.inputDirectory)
			// defines RouteId
			.routeId(this.id)
			.split(bodyAs(String.class)
				.tokenize("\n"))
			.process(this.orderFileNameProcessor)
				.log("Write file: ${header.CamelFileName}")
			.toF("file://%s", this.outputDirectory)
		.end();
	}
}
