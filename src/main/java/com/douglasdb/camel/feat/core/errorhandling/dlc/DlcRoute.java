package com.douglasdb.camel.feat.core.errorhandling.dlc;

import com.douglasdb.camel.feat.core.errorhandling.shared.FlakyProcessor;
import org.apache.camel.builder.RouteBuilder;

/**
 *
 */
public class DlcRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        errorHandler(deadLetterChannel("seda:error"));

        from("direct:start")
                .routeId("myDlcRoute")
                .setHeader("myHeader")
                    .constant("changed")
                .bean(FlakyProcessor.class)
                .to("mock:result");

        from("direct:multiroute")
                .routeId("myDlcMultistepRoute")
                .setHeader("myHeader", constant("multistep"))
                .inOut("seda:flakyroute")
                    .setHeader("myHeader").constant("changed")
                .to("mock:result");

        from("direct:multirouteOriginal")
                .routeId("myDlcMultistepOriginalRoute")
                .setHeader("myHeader", constant("multistep"))
                .inOut("seda:flakyrouteOriginal")
                .setHeader("myHeader", constant("changed"))
                .to("mock:result");

        from("seda:flakyrouteOriginal")
                .routeId("myFlakyRouteOriginal")
                .errorHandler(deadLetterChannel("seda:error").useOriginalMessage())
                .setHeader("myHeader").constant("flaky")
                .bean(FlakyProcessor.class);

        from("seda:flakyroute")
                .routeId("myFlakyRoute")
                .setHeader("myHeader", constant("flaky"))
                .bean(FlakyProcessor.class);

        from("direct:useOriginal")
                .routeId("myDlcOriginalRoute")
                .errorHandler(deadLetterChannel("seda:error").useOriginalMessage())
                .setHeader("myHeader").constant("changed")
                .bean(FlakyProcessor.class)
                .to("mock:result");

        from("direct:routeSpecific")
                .routeId("myDlcSpecificRoute")
                .errorHandler(deadLetterChannel("seda:error"))
                .bean(FlakyProcessor.class)
                .to("mock:result");

        from("seda:error")
                .routeId("myDlcChannelRoute")
                .to("log:dlc?showAll=true&multiline=true")
                .to("mock:error");


    }
}
