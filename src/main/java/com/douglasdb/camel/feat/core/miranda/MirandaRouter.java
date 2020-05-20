package com.douglasdb.camel.feat.core.miranda;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.util.StringHelper;

public class MirandaRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jetty://http://localhost:9080/service/order")
                .process(exchange -> {
                    final String id = exchange.getIn().getHeader("id", String.class);
                    exchange.getIn().setBody("ID=" + id);
                }).to("mock:miranda")
                .process(exchange -> {
                    final String body = exchange.getIn().getBody(String.class);
                    final String reply = StringHelper.after(body, "STATUS=");
                    exchange.getIn().setBody(reply);
                })
                .end();

        from("jetty://http://localhost:9081/service/order")
                .transform().message(m -> "ID=" + m.getHeader("id"))
                .to("mock:miranda")
                .transform().body(String.class, b -> StringHelper.after(b, "STATUS="));

    }


}
