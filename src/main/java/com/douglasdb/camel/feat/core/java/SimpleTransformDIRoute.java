package com.douglasdb.camel.feat.core.java;

import lombok.Setter;
import org.apache.camel.builder.RouteBuilder;

public class SimpleTransformDIRoute extends RouteBuilder {

    @Setter
    private String sourceUri;
    @Setter
    private String targetUri;

    @Override
    public void configure() throws Exception {
        from(sourceUri).transform(simple("Modified: ${body}")).to(targetUri);
    }

}
