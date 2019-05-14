package com.douglasdb.camel.feat.core.bean.bean;

import com.douglasdb.camel.feat.core.domain.bean.HelloBean;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author DouglasDb
 */
public class InvokeWithProcessorRoute extends RouteBuilder {

    @Override
    public void configure() {

        from("direct:hello")
            .process((exchange) -> {
                final String name = exchange.getIn().getBody(String.class);
                HelloBean hello = new HelloBean();
                String answer = hello.hello(name);
                //
                exchange.getOut().setBody(answer);
            });

    }

}