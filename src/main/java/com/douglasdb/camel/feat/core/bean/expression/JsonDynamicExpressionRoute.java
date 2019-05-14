package com.douglasdb.camel.feat.core.bean.expression;

import org.apache.camel.builder.RouteBuilder;


/**
 * 
 */
public class JsonDynamicExpressionRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("file://src/main/resources/META-INF/order")
            .log("${body}")
            .setHeader("region", method(CustomerService.class, "region"))
            .toD("mock:queue:${header.region}")
        .end();

    }
    
}