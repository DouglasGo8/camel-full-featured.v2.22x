package com.douglasdb.camel.feat.core.transform.enrich;

import org.apache.camel.builder.RouteBuilder;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EnrichWithAggregatorRoute extends RouteBuilder {

	private MergeInReplacementText myMerger;

	@Override
	public void configure() throws Exception {

		from("direct:start")
			.bean(myMerger, "setup")
			.log("The body is...: ${body}")
			.enrich("direct:expander", myMerger);

	}

}
