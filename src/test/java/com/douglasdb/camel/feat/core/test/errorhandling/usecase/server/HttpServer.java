package com.douglasdb.camel.feat.core.test.errorhandling.usecase.server;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class HttpServer extends CamelTestSupport {


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("jetty:http://localhost:8765/rider")
                        .log("${body}")
                        .process(exchange -> {
                            final String body = exchange.getIn().getBody(String.class);
                            System.out.println("Received message: " + body);

                            if( body != null && body.contains("Kabom")) {
                                throw new Exception("ILLEGAL DATA");
                            }

                            exchange.getOut().setBody("OK");
                        });
            }
        };
    }

    @Test
    public void setUpHttpServer() throws InterruptedException {

        System.out.println("Starting HttpServer... press ctrl + c to stop it");



        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }


}
