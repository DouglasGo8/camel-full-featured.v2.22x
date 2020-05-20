package com.douglasdb.camel.feat.core.test.errorhandling.simulating;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.ConnectException;

public class EntryPoint extends CamelSpringTestSupport {

    @EndpointInject(ref = "fileEndpoint")
    private Endpoint file;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/errorhandling/simulating/app-context.xml");
    }


    @Test
    public void testSimulateErrorUsingInterceptors() throws Exception {

        final RouteDefinition route = super.context.getRouteDefinitions().get(0);

        route.adviceWith(super.context, new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                interceptSendToEndpoint("http://*")
                        .skipSendToOriginalEndpoint()
                        .process(exchange -> {
                            throw new ConnectException("Simulated connection error");
                        });

                interceptSendToEndpoint("ftp://*")
                        .skipSendToOriginalEndpoint()
                        .log("Send to Mock!!!")
                        .to("mock:ftp");
            }
        });

        final MockEndpoint mock = super.getMockEndpoint("mock:ftp");
        mock.expectedBodiesReceived("Camel rocks");

        // start the test by creating a file that gets picked up by the route
        super.template.sendBodyAndHeader(file, "Camel rocks", Exchange.FILE_NAME, "hello.txt");

        assertMockEndpointsSatisfied();
    }
}
