package com.douglasdb.camel.feat.core.errorhandling.dlc;

import com.douglasdb.camel.feat.core.errorhandling.shared.FlakyProcessor;
import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class DlcRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        from("direct:start")
                .routeId("myDlcRoute")
                .setHeader("myHeader")
                    .constant("changed")
                .bean(FlakyProcessor.class)
                .to("mock:result");
    }
}
