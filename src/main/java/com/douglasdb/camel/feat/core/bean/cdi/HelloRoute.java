package com.douglasdb.camel.feat.core.bean.cdi;

import javax.inject.Singleton;

import org.apache.camel.builder.RouteBuilder;

import lombok.NoArgsConstructor;

/**
 * 
 */
@Singleton
@NoArgsConstructor
public class HelloRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {

        from("timer:foo?period=5s")
            .log("${body}");
    }

}