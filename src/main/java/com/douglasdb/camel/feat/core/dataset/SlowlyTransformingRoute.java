package com.douglasdb.camel.feat.core.dataset;

import lombok.Setter;
import org.apache.camel.builder.RouteBuilder;

public class SlowlyTransformingRoute extends RouteBuilder {

    @Setter
    private String sourceUri;
    @Setter
    private String targetUri;

    @Override
    public void configure() throws Exception {

        from(sourceUri).to("seda:transformBody");

        from("seda:transformBody?concurrentConsumers=15").transform(simple("Modified: ${body}"))
                .delay(100).to("seda:sendTransformed");

        from("seda:sendTransformed").resequence().simple("${header.mySequenceId}").stream()
                .to(targetUri);
    }
}
