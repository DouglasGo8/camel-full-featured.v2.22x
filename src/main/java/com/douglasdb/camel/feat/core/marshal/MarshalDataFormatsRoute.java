package com.douglasdb.camel.feat.core.marshal;

import org.apache.camel.builder.RouteBuilder;

public class MarshalDataFormatsRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("direct:in").convertBodyTo(byte[].class, "utf-16")
                .bean(MyByteArrayBean.class)
                .to("mock:result");

    }
}
